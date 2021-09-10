package vtcc.voice.core.audiofileconverter.ffmpegbased;

import lombok.extern.slf4j.Slf4j;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFileType;
import vtcc.voice.core.audiofile.AudioFormat;

import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 11:29 <br>
 */
@Slf4j
public class FfmpegInputAudioFile implements AudioFile {
  private final Path filePath;
  // default, ffmpeg does not use this value
  private final AudioFileType fileType = AudioFileType.UNCHECKED;
  // default, ffmpeg does not use this value
  private final AudioFormat audioFormat = AudioFormat.builder().build();

  private FfmpegInputAudioFile(Path filePath) {
    this.filePath = filePath;
  }

  @Override
  public Path getFilePath() {
    return filePath;
  }

  @Override
  public long getDuration() {
    return UNSET_NUMBER_VALUE; // don't care
  }

  @Override
  public AudioFileType getFileType() {
    return fileType;
  }

  @Override
  public AudioFormat getAudioFormat() {
    return audioFormat;
  }

  public static FfmpegInputAudioFile fromLocalFile(Path filePath) {
    return new FfmpegInputAudioFile(filePath);
  }
}
