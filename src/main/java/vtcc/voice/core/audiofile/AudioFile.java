package vtcc.voice.core.audiofile;

import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 15:08 <br>
 */
public interface AudioFile {
  // used to specify default number that not have been set
  int UNSET_NUMBER_VALUE = -1;

  Path getFilePath();

  /** @return duration of this audio file or {@value #UNSET_NUMBER_VALUE} if it has not been set */
  long getDuration();

  AudioFileType getFileType();

  AudioFormat getAudioFormat();
}
