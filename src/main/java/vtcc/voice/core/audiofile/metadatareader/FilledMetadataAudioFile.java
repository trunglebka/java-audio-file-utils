package vtcc.voice.core.audiofile.metadatareader;

import lombok.Builder;
import lombok.NonNull;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFileType;
import vtcc.voice.core.audiofile.AudioFormat;

import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 15:22 <br>
 */
@Builder
class FilledMetadataAudioFile implements AudioFile {
  @NonNull private final Path filePath;
  @NonNull private final long duration;
  @NonNull private final AudioFileType fileType;
  @NonNull private final AudioFormat audioFormat;

  @Override
  public Path getFilePath() {
    return filePath;
  }

  @Override
  public long getDuration() {
    return duration;
  }

  @Override
  public AudioFileType getFileType() {
    return fileType;
  }

  @Override
  public AudioFormat getAudioFormat() {
    return audioFormat;
  }
}
