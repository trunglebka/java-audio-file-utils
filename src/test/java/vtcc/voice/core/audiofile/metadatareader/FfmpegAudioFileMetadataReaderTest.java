package vtcc.voice.core.audiofile.metadatareader;

import org.junit.jupiter.api.Test;
import vtcc.voice.core.audiofile.AudioFile;
import vtcc.voice.core.audiofile.AudioFileType;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 15:46 <br>
 */
class FfmpegAudioFileMetadataReaderTest {
  private AudioFileMetadataReader audioFileMetadataReader;

  public FfmpegAudioFileMetadataReaderTest() throws IOException {
    audioFileMetadataReader = FfmpegAudioFileMetadataReader.fromDefaultFFmpegProbe();
  }

  @Test
  void readWavMetadata() throws Exception {
    Path wavPath = Paths.get("src/test/resources/audio/8k.wav");
    long wavDuration = 430; // ms
    AudioFile wavFile = audioFileMetadataReader.readFromFile(wavPath);
    // rounding to 0.01s
    assertEquals(AudioFileType.WAV, wavFile.getFileType());
    assert wavDuration >= wavFile.getDuration() - 100 || wavDuration <= wavFile.getDuration() + 100;
  }

  @Test
  void readM4aMetadata() throws Exception {
    Path wavPath = Paths.get("src/test/resources/audio/8k.m4a");
    long wavDuration = 430; // ms
    AudioFile wavFile = audioFileMetadataReader.readFromFile(wavPath);
    // rounding to 0.01s
    assertEquals(AudioFileType.M4A, wavFile.getFileType());
    assert wavDuration >= wavFile.getDuration() - 100 || wavDuration <= wavFile.getDuration() + 100;
  }

  @Test
  void readMp3Metadata() throws Exception {
    Path wavPath = Paths.get("src/test/resources/audio/8k.mp3");
    long wavDuration = 430; // ms
    AudioFile wavFile = audioFileMetadataReader.readFromFile(wavPath);
    // rounding to 0.01s
    assertEquals(AudioFileType.MP3, wavFile.getFileType());
    assert wavDuration >= wavFile.getDuration() - 100 || wavDuration <= wavFile.getDuration() + 100;
  }
}
