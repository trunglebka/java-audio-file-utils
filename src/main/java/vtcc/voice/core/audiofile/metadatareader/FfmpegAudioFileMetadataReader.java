package vtcc.voice.core.audiofile.metadatareader;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegError;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFileType;
import vtcc.voice.core.audiofile.AudioFormat;
import vtcc.voice.core.audiofile.FfmpegFileTypeMappers;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 15:16 <br>
 */
public class FfmpegAudioFileMetadataReader implements AudioFileMetadataReader {
  private final FFprobe ffmpegProbe;

  private FfmpegAudioFileMetadataReader(Path ffmpegProbePath) throws IOException {
    if (ffmpegProbePath != null) {
      this.ffmpegProbe = new FFprobe(ffmpegProbePath + "");
    } else {
      this.ffmpegProbe = new FFprobe();
    }
  }

  public static FfmpegAudioFileMetadataReader fromDefaultFFmpegProbe() throws IOException {
    return new FfmpegAudioFileMetadataReader(null);
  }

  public static FfmpegAudioFileMetadataReader fromFFmpegProbe(Path ffmpegProbe) throws IOException {
    return new FfmpegAudioFileMetadataReader(ffmpegProbe);
  }

  @Override
  public AudioFile readFromFile(Path filePath) throws IOException {
    FFmpegProbeResult probeResult = ffmpegProbe.probe(filePath + "");
    if (probeResult.hasError()) {
      FFmpegError error = probeResult.error;
      String errMsg =
          String.format("ffprobe failed with code=%s, message=%s", error.code, error.string);
      throw new IOException(errMsg);
    }
    for (FFmpegStream stream : probeResult.streams) {
      if (stream.codec_type == FFmpegStream.CodecType.AUDIO) {
        AudioFormat audioFormat =
            AudioFormat.builder()
                .sampleRate(stream.sample_rate)
                .bitrate(stream.bit_rate)
                .numChannels(stream.channels)
                .build();

        long durationInMillis = (long) (stream.duration * 1000);
        AudioFileType fileType =
            FfmpegFileTypeMappers.toAudioFileType(
                selectBestMatchFormat(filePath, probeResult.format.format_name));
        return FilledMetadataAudioFile.builder()
            .filePath(filePath)
            .audioFormat(audioFormat)
            .duration(durationInMillis)
            .fileType(fileType)
            .build();
      }
    }
    // Audio stream not found
    return new AudioFile() {
      @Override
      public Path getFilePath() {
        return filePath;
      }

      @Override
      public long getDuration() {
        return 0;
      }

      @Override
      public AudioFileType getFileType() {
        return AudioFileType.UNDEFINED;
      }

      @Override
      public AudioFormat getAudioFormat() {
        return AudioFormat.newEmptyFormat();
      }
    };
  }

  private static String selectBestMatchFormat(Path filePath, String ffmpegFormatsStr) {
    String[] validFormats = ffmpegFormatsStr.split(", *");
    String fileExtension = getFileExtension(filePath);
    if (!fileExtension.isEmpty()) {
      for (String fmt : validFormats) {
        if (fmt.equalsIgnoreCase(fileExtension)) {
          return fmt;
        }
      }
    }
    return ffmpegFormatsStr;
  }

  private static String getFileExtension(Path filePath) {
    String filename = filePath.getFileName().toString();
    int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex != -1 && lastDotIndex != filename.length() - 1) {
      return filename.substring(lastDotIndex + 1);
    } else {
      return "";
    }
  }
}
