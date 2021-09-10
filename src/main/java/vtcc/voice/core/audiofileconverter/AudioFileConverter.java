package vtcc.voice.core.audiofileconverter;

import vtcc.voice.core.audiofile.AudioFile;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 11:25 <br>
 */
public interface AudioFileConverter {
  void convertFile(AudioFile inputAudioFile, AudioFile outputAudioFile) throws Exception;

  boolean isSupportedFileTypes(String fileType);

  interface MaxConvertTimeCalculator {
    long expectedMaxConvertTime(AudioFile inputAudioFile, AudioFile outputAudioFile);
  }
}
