package vtcc.voice.core.audiofile;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 11:06 <br>
 */
@Slf4j
public class FfmpegFileTypeMappers {
  private static final Map<AudioFileType, String> FILETYPE_FFMPEG_FORMAT_MAP = new HashMap<>();
  private static final Map<String, AudioFileType> FFMPEG_FORMAT_FILETYPE_MAP;

  static {
    FILETYPE_FFMPEG_FORMAT_MAP.put(AudioFileType.M4A, "IPOD");
    FFMPEG_FORMAT_FILETYPE_MAP =
        FILETYPE_FFMPEG_FORMAT_MAP.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
  }

  public static String toFfmpegFormat(AudioFileType fileType) {
    if (FILETYPE_FFMPEG_FORMAT_MAP.containsKey(fileType)) {
      return FILETYPE_FFMPEG_FORMAT_MAP.get(fileType);
    } else {
      return fileType + "";
    }
  }

  public static AudioFileType toAudioFileType(String ffmpegFormat) {
    String formatUpperCase = ffmpegFormat.toUpperCase();
    if (FFMPEG_FORMAT_FILETYPE_MAP.containsKey(ffmpegFormat)) {
      return FFMPEG_FORMAT_FILETYPE_MAP.get(formatUpperCase);
    } else {
      try {
        return AudioFileType.valueOf(formatUpperCase);
      } catch (Exception e) {
        log.warn(
            "Try to convert ffmpeg to AudioFileType that has not been defined: {}", ffmpegFormat);
        return AudioFileType.UNDEFINED;
      }
    }
  }
}
