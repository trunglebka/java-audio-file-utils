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
 * Time: 11:52 <br>
 */
@Slf4j
public class FfmpegOutputAudioFile implements AudioFile {
  private final Path filePath;
  private final AudioFileType fileType;
  private final AudioFormat audioFormat;

  FfmpegOutputAudioFile(Path filePath, AudioFileType fileType, AudioFormat audioFormat) {
    this.filePath = filePath;
    this.fileType = fileType;
    this.audioFormat = audioFormat;
  }

  public static FfmpegOutputAudioFileBuilder builder() {
    return new FfmpegOutputAudioFileBuilder();
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

  public static class FfmpegOutputAudioFileBuilder {
    private Path filePath;
    private AudioFileType fileType = AudioFileType.UNDEFINED;
    private AudioFormat audioFormat = AudioFormat.newEmptyFormat();

    FfmpegOutputAudioFileBuilder() {}

    public FfmpegOutputAudioFileBuilder filePath(Path filePath) {
      if (filePath == null) {
        throw new IllegalArgumentException("Output file path must be specified");
      }
      this.filePath = filePath;
      return this;
    }

    public FfmpegOutputAudioFileBuilder fileType(AudioFileType fileType) {
      if (fileType == AudioFileType.UNCHECKED || fileType == AudioFileType.UNDEFINED) {
        String errMsg = "Target output file type must be specified";
        throw new IllegalArgumentException(errMsg);
      }

      this.fileType = fileType;
      return this;
    }

    public FfmpegOutputAudioFileBuilder audioFormat(AudioFormat audioFormat) {
      this.audioFormat = audioFormat;
      return this;
    }

    public FfmpegOutputAudioFile build() {
      validateOrThrow();
      return new FfmpegOutputAudioFile(filePath, fileType, audioFormat);
    }

    public String toString() {
      return "FfmpegOutputAudioFile.FfmpegOutputAudioFileBuilder(filePath="
          + this.filePath
          + ", fileType="
          + this.fileType
          + ", audioFormat="
          + this.audioFormat
          + ")";
    }

    private void validateOrThrow() throws IllegalStateException {
      if (filePath == null) {
        throw new IllegalStateException("Output file path must be specified");
      }
      if (fileType == AudioFileType.UNCHECKED || fileType == AudioFileType.UNDEFINED) {
        String errMsg = "Target output file type must be specified";
        throw new IllegalStateException(errMsg);
      }
    }
  }
}
