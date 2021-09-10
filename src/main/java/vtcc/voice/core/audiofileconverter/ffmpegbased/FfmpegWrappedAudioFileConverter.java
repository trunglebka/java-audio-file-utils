package vtcc.voice.core.audiofileconverter.ffmpegbased;

import lombok.Builder;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.builder.FFmpegOutputBuilder;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFormat;
import vtcc.voice.core.audiofile.FfmpegFileTypeMappers;
import vtcc.voice.core.audiofileconverter.AudioFileConverter;

import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 16:12 <br>
 */
@Builder
public class FfmpegWrappedAudioFileConverter implements AudioFileConverter {
  private final Path ffmpegExecutable;
  private MaxConvertTimeCalculator maxConvertTimeCalculator;

  @Override
  public void convertFile(AudioFile inputAudioFile, AudioFile outputAudioFile) throws Exception {
    FFmpeg ffmpeg;
    if (ffmpegExecutable != null) {
      ffmpeg = new FFmpeg(ffmpegExecutable + "");
    } else {
      ffmpeg = new FFmpeg();
    }
    FFmpegBuilder ffmpegBuilder = buildFfmpegCommand(inputAudioFile, outputAudioFile);
    FFmpegExecutor executor = new FFmpegExecutor(ffmpeg);
    executor.createJob(ffmpegBuilder).run();
  }

  @Override
  public boolean isSupportedFileTypes(String fileType) {
    return true;
  }

  private FFmpegBuilder buildFfmpegCommand(AudioFile inputAudioFile, AudioFile outputAudioFile) {
    FFmpegOutputBuilder fFmpegBuilder =
        new FFmpegBuilder()
            .setInput(inputAudioFile.getFilePath() + "")
            .overrideOutputFiles(true)
            .addOutput(outputAudioFile.getFilePath() + "")
            .setFormat(FfmpegFileTypeMappers.toFfmpegFormat(outputAudioFile.getFileType()))
            .disableSubtitle() // No subtiles
            .disableVideo()
            .setStrict(FFmpegBuilder.Strict.EXPERIMENTAL);
    AudioFormat targetAudioFormat = outputAudioFile.getAudioFormat();
    if (targetAudioFormat == null) {
      throw new IllegalStateException("Target audio format is null");
    }

    if (targetAudioFormat.isSampleRateSpecified()) {
      fFmpegBuilder.setAudioSampleRate(targetAudioFormat.getSampleRate());
    }

    if (targetAudioFormat.isBitRateSpecified()) {
      fFmpegBuilder.setAudioBitRate(targetAudioFormat.getBitrate());
    }

    if (targetAudioFormat.isNumChannelsSpecified()) {
      fFmpegBuilder.setAudioChannels(targetAudioFormat.getNumChannels());
    }

    if (targetAudioFormat.isSampleFormatSpecified()) {
      fFmpegBuilder.setAudioSampleFormat(targetAudioFormat.getSampleFormat());
    }

    return fFmpegBuilder.done();
  }
}
