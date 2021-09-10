package vtcc.voice.core.audiofile;

import lombok.Getter;

/**
 * Created by Trung Le. <br>
 * Email: trungle.bka@gmail.com <br>
 * Date: 09/09/2021 <br>
 * Time: 14:59 <br>
 */
@Getter
public class AudioFormat {
  private static final int DEFAULT_NUMBER_VAL = -1;
  // user should not depend on the value of this variable (ex. via some get method)
  private static final String DEFAULT_STRING_VAL = "SOME_RANDOM_VALUE";

  private final int sampleRate;
  private final long bitrate;
  private final int numChannels;
  private final String sampleFormat;

  private AudioFormat(int sampleRate, long bitrate, int numChannels, String sampleFormat) {
    this.sampleRate = sampleRate;
    this.bitrate = bitrate;
    this.numChannels = numChannels;
    this.sampleFormat = sampleFormat;
  }

  public static AudioFormat newEmptyFormat() {
    return new AudioFormat(
        DEFAULT_NUMBER_VAL, DEFAULT_NUMBER_VAL, DEFAULT_NUMBER_VAL, DEFAULT_STRING_VAL);
  }

  /** @return Builder with field set as default value (unset) of corresponding type */
  public static AudioFormatBuilder builder() {
    return new AudioFormatBuilder();
  }

  public boolean isSampleRateSpecified() {
    return sampleRate != DEFAULT_NUMBER_VAL;
  }

  public boolean isBitRateSpecified() {
    return bitrate != DEFAULT_NUMBER_VAL;
  }

  public boolean isNumChannelsSpecified() {
    return numChannels != DEFAULT_NUMBER_VAL;
  }

  public boolean isSampleFormatSpecified() {
    return !DEFAULT_STRING_VAL.equals(sampleFormat);
  }

  public AudioFormatBuilder toBuilder() {
    return new AudioFormatBuilder()
        .sampleRate(this.sampleRate)
        .bitrate(this.bitrate)
        .numChannels(this.numChannels)
        .sampleFormat(this.sampleFormat);
  }

  public static class AudioFormatBuilder {
    private int sampleRate = DEFAULT_NUMBER_VAL;
    private long bitrate = DEFAULT_NUMBER_VAL;
    private int numChannels = DEFAULT_NUMBER_VAL;
    private String sampleFormat = DEFAULT_STRING_VAL;

    AudioFormatBuilder() {}

    public AudioFormatBuilder sampleRate(int val) {
      if (val <= 0) {
        String errMsg = "Sample rate must larger than 0";
        throw new IllegalArgumentException(errMsg);
      }
      this.sampleRate = val;
      return this;
    }

    public AudioFormatBuilder bitrate(long val) {
      if (val <= 0) {
        String errMsg = "bitrate must larger than 0";
        throw new IllegalArgumentException(errMsg);
      }
      this.bitrate = val;
      return this;
    }

    public AudioFormatBuilder numChannels(int val) {
      if (val <= 0) {
        String errMsg = "number of channels must larger than 0";
        throw new IllegalArgumentException(errMsg);
      }
      this.numChannels = val;
      return this;
    }

    public AudioFormatBuilder sampleFormat(String sampleFormat) {
      if (sampleFormat == null || sampleFormat.isEmpty()) {
        String errMsg = "Sample format must not empty";
        throw new IllegalArgumentException(errMsg);
      }
      this.sampleFormat = sampleFormat;
      return this;
    }

    public AudioFormat build() {
      return new AudioFormat(sampleRate, bitrate, numChannels, sampleFormat);
    }

    public String toString() {
      return "AudioFormat.AudioFormatBuilder(sampleRate="
          + this.sampleRate
          + ", bitRate="
          + this.bitrate
          + ", numChannels="
          + this.numChannels
          + ", sampleFormat="
          + this.sampleFormat
          + ")";
    }
  }
}
