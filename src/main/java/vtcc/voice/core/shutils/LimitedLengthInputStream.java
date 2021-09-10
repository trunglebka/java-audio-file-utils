package vtcc.voice.core.shutils;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 16:59 <br>
 */
public class LimitedLengthInputStream extends InputStream {
  private final InputStream internalStream;
  private final long maxLength;
  private long numByteRead;

  /**
   * Construct limited length input stream
   *
   * @param inputStream stream that need to be limited
   * @param maxLength maximum length, zero or negative value indicate unlimited
   */
  public LimitedLengthInputStream(InputStream inputStream, long maxLength) {
    this.internalStream = inputStream;
    this.maxLength = maxLength + 1; // add 1 to handle stream return -1 case
  }

  @Override
  public int read() throws IOException {
    if (maxLength > 1) { // no limit if maxLength is <=0
      numByteRead++;
      if (numByteRead > maxLength) {
        String errMsg = "Maximum number of bytes exceed";
        throw new IOException(errMsg);
      }
    }
    return internalStream.read();
  }
}
