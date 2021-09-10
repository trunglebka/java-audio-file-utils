package vtcc.voice.core.audiofileconverter.ffmpegbased;

import lombok.Builder;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFormat;
import vtcc.voice.core.audiofile.FfmpegFileTypeMappers;
import vtcc.voice.core.audiofileconverter.AudioFileConversionException;
import vtcc.voice.core.audiofileconverter.AudioFileConverter;
import vtcc.voice.core.shutils.ShellSubprocessResult;
import vtcc.voice.core.shutils.ShellSubprocessRunner;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 16:12 <br>
 */
@Builder
public class FfmpegAudioFileConverter implements AudioFileConverter {
  private final Path ffmpegExecutable;
  private MaxConvertTimeCalculator maxConvertTimeCalculator;

  @Override
  public void convertFile(AudioFile inputAudioFile, AudioFile outputAudioFile) throws Exception {
    List<String> ffmpegArgs = buildFfmpegArgs(inputAudioFile, outputAudioFile);
    ShellSubprocessRunner subprocessRunner =
        ShellSubprocessRunner.builder()
            .command(ffmpegExecutable + "")
            .args(ffmpegArgs)
            .maxReturnSize(-1)
            .build();
    long timeout = Long.MAX_VALUE;
    if (maxConvertTimeCalculator != null) {
      timeout = maxConvertTimeCalculator.expectedMaxConvertTime(inputAudioFile, outputAudioFile);
    }

    ShellSubprocessResult subprocessResult = subprocessRunner.run(timeout, TimeUnit.MILLISECONDS);
    if (subprocessResult.getExitCode() != 0) {
      String errMsg =
          String.format(
              "\n_______________________________FFMPEG ERROR REPORT BEGIN_______________________________\n"
                  + "Ffmpeg file conversion failed with:"
                  + "\nEXIT CODE=%d"
                  + "\nSTDOUT=%s"
                  + "\nSTDERR=%s"
                  + "\n_______________________________FFMPEG ERROR REPORT END_______________________________",
              subprocessResult.getExitCode(),
              subprocessResult.getStdout(),
              subprocessResult.getStderr());
      throw new AudioFileConversionException(errMsg);
    }
  }

  @Override
  public boolean isSupportedFileTypes(String fileType) {
    return true;
  }

  private static List<String> buildFfmpegArgs(AudioFile inputAudioFile, AudioFile outputAudioFile) {
    List<String> ffmpegArgs = new ArrayList<>();
    ffmpegArgs.add("-hide_banner");
    ffmpegArgs.add("-loglevel error");
    ffmpegArgs.add("-y"); // no interactive
    ffmpegArgs.add("-i " + inputAudioFile.getFilePath().toAbsolutePath());
    ffmpegArgs.add("-vn"); // no video

    AudioFormat targetFormat = outputAudioFile.getAudioFormat();
    if (targetFormat.isSampleRateSpecified()) {
      ffmpegArgs.add("-ar " + targetFormat.getSampleRate());
    }
    if (targetFormat.isBitRateSpecified()) {
      ffmpegArgs.add("-b:a " + targetFormat.getBitrate());
    }
    if (targetFormat.isNumChannelsSpecified()) {
      ffmpegArgs.add("-ac " + targetFormat.getNumChannels());
    }
    if (targetFormat.isSampleFormatSpecified()) {
      ffmpegArgs.add("-sample_fmts " + targetFormat.getSampleFormat());
    }
    ffmpegArgs.add("-f " + FfmpegFileTypeMappers.toFfmpegFormat(outputAudioFile.getFileType()));
    ffmpegArgs.add(outputAudioFile.getFilePath().toAbsolutePath() + "");
    return ffmpegArgs;
  }
}
