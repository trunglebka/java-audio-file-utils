package vtcc.voice.core.shutils;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 16:26 <br>
 */
@Builder
@Slf4j
public class ShellSubprocessRunner {
  private String command;
  private List<String> args;
  private long maxReturnSize;

  /**
   * @param timeout <=0 indicate no timeout
   * @param timeUnit the time unit of the timeout argument
   * @return ShellSubprocessResult that contain result of command
   * @throws Exception if any error occurred
   */
  public ShellSubprocessResult run(long timeout, TimeUnit timeUnit) throws Exception {
    ProcessBuilder processBuilder = createProcessBuilder();
    log.debug("Starting command: " + processBuilder.command());
    Process process = processBuilder.start();
    String stdout = readShellStream(process.getInputStream());
    String stderr = readShellStream(process.getErrorStream());

    if (timeout > 0) {
      boolean processCompleted = process.waitFor(timeout, timeUnit);
      if (!processCompleted) {
        String errStr =
            String.format(
                "Command <%s> cannot completed within %d (%s)",
                processBuilder.command(), timeout, timeUnit);
        throw new TimeoutException(errStr);
      }
    } else {
      process.waitFor();
    }
    log.debug("Command: <" + processBuilder.command() + "> completed");

    return ShellSubprocessResult.builder()
        .stdout(stdout)
        .stderr(stderr)
        .exitCode(process.exitValue())
        .build();
  }

  private ProcessBuilder createProcessBuilder() {
    List<String> commandArgs = new ArrayList<>(args.size() + 3);
    commandArgs.addAll(Arrays.asList("/bin/sh", "-c"));
    commandArgs.add(command+" "+ String.join(" ", args));
    return new ProcessBuilder(commandArgs);
  }

  private String readShellStream(InputStream inputStream) throws IOException {
    try (LimitedLengthInputStream li = new LimitedLengthInputStream(inputStream, maxReturnSize);
        BufferedReader br = new BufferedReader(new InputStreamReader(li))) {
      String line;
      StringBuilder sb = new StringBuilder();
      while ((line = br.readLine()) != null) {
        sb.append(line).append("\n");
      }
      return sb.toString();
    }
  }
}
