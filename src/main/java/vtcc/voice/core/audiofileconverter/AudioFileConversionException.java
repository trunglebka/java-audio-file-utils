package vtcc.voice.core.audiofileconverter;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 10:07 <br>
 */
public class AudioFileConversionException extends RuntimeException {
  public AudioFileConversionException(String msg) {
    super(msg);
  }

  public AudioFileConversionException(Throwable cause) {
    super(cause);
  }
}
