package vtcc.voice.core.audiofileconverter.ffmpegbased;

import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFileType;
import vtcc.voice.core.audiofile.AudioFormat;
import vtcc.voice.core.audiofile.metadatareader.AudioFileMetadataReader;
import vtcc.voice.core.audiofile.metadatareader.FfmpegAudioFileMetadataReader;
import vtcc.voice.core.audiofileconverter.AudioFileConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 10:09 <br>
 */
public class FfmpegAudioFileConverterTest {
  private Path tmpTestDir;
  private Path ffmpegPath = Paths.get("/usr/bin/ffmpeg");
  private AudioFileMetadataReader audioFileMetadataReader;

  public FfmpegAudioFileConverterTest() throws IOException {
    String envTmpdir = System.getProperty("java.io.tmpdir");
    if (envTmpdir.isEmpty()) {
      envTmpdir = "/tmp";
    }
    Path tmpDir = Paths.get(envTmpdir);
    tmpTestDir = tmpDir.resolve("audio-file-utils-test-tmp");
    if (Files.notExists(tmpTestDir)) {
      Files.createDirectories(tmpTestDir);
    }
    audioFileMetadataReader = FfmpegAudioFileMetadataReader.fromDefaultFFmpegProbe();
  }

  @org.junit.jupiter.api.Test
  void convertFileWavToM4a() throws Exception {
    Path wavFile = Paths.get("src/test/resources/audio/8k.wav");
    Path m4aFile = tmpTestDir.resolve("m4a-test-out.m4a");
    AudioFile inFile = FfmpegInputAudioFile.fromLocalFile(wavFile);
    AudioFile outFile =
        FfmpegOutputAudioFile.builder().filePath(m4aFile).fileType(AudioFileType.M4A).build();
    AudioFileConverter audioFileConverter =
        FfmpegWrappedAudioFileConverter.builder().ffmpegExecutable(ffmpegPath).build();
    audioFileConverter.convertFile(inFile, outFile);
  }

  @org.junit.jupiter.api.Test
  void convertFileM4aToWav() throws Exception {
    Path wavFile = Paths.get("src/test/resources/audio/8k.m4a");
    Path m4aFile = tmpTestDir.resolve("wav-test-out.wav");
    AudioFile inFile = FfmpegInputAudioFile.fromLocalFile(wavFile);
    AudioFormat targetFormat = AudioFormat.builder().numChannels(2).build();
    AudioFile outFile =
        FfmpegOutputAudioFile.builder()
            .filePath(m4aFile)
            .audioFormat(targetFormat)
            .fileType(AudioFileType.WAV)
            .build();
    AudioFileConverter audioFileConverter =
        FfmpegWrappedAudioFileConverter.builder().ffmpegExecutable(ffmpegPath).build();
    audioFileConverter.convertFile(inFile, outFile);
  }

  @org.junit.jupiter.api.Test
  void convertFileM4aToWavChangeFormat() throws Exception {
    Path wavFile = Paths.get("src/test/resources/audio/8k.m4a");
    Path m4aFile = tmpTestDir.resolve("wav-test-out.wav");
    AudioFile inFile = FfmpegInputAudioFile.fromLocalFile(wavFile);
    int targetChannel = 2;
    int targetRate = 4000;
    AudioFormat targetFormat =
        AudioFormat.builder().numChannels(targetChannel).sampleRate(targetRate).build();
    AudioFile outFile =
        FfmpegOutputAudioFile.builder()
            .filePath(m4aFile)
            .audioFormat(targetFormat)
            .fileType(AudioFileType.WAV)
            .build();
    AudioFileConverter audioFileConverter =
        FfmpegWrappedAudioFileConverter.builder().ffmpegExecutable(ffmpegPath).build();
    audioFileConverter.convertFile(inFile, outFile);
    AudioFile metadataLoadedAudioFile = audioFileMetadataReader.readFromFile(outFile.getFilePath());
    assertEquals(targetChannel, metadataLoadedAudioFile.getAudioFormat().getNumChannels());
    assertEquals(targetRate, metadataLoadedAudioFile.getAudioFormat().getSampleRate());
  }
}
