package vtcc.voice.core.shutils;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 16:26 <br>
 */
import lombok.Builder;
import lombok.Getter;

/** This class used to store output of shell command with small data returned */
@Builder
@Getter
public class ShellSubprocessResult {
  private final String stdout;
  private final String stderr;
  private final int exitCode;
}
