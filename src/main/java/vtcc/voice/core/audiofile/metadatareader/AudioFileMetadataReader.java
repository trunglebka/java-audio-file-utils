package vtcc.voice.core.audiofile.metadatareader;

import vtcc.voice.core.audiofile.AudioFile;

import java.nio.file.Path;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 10/09/2021 <br>
 * Time: 15:14 <br>
 */
public interface AudioFileMetadataReader {
  AudioFile readFromFile(Path filePath) throws Exception;
}
