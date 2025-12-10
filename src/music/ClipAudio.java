package music;

import javax.sound.sampled.*;
import java.io.*;
import io.ResourceFinder;

/**
 * ClipAudio that loads WAV files and converts unsupported formats into a safe 16-bit PCM format.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class ClipAudio
{
  private Clip clip;

  /**
   * Creates a clip from the given file.
   *
   * @param finder
   *          the resource finder
   * @param filename
   *          the wav file name
   * @throws IOException
   *           if the file cannot be read
   * @throws UnsupportedAudioFileException
   *           if the audio format is not valid
   * @throws LineUnavailableException
   *           if the clip cannot be opened
   */
  public ClipAudio(final ResourceFinder finder, final String filename)
      throws IOException, UnsupportedAudioFileException, LineUnavailableException
  {
    InputStream raw = finder.findInputStream(filename);
    BufferedInputStream buff = new BufferedInputStream(raw);
    AudioInputStream originalStream = AudioSystem.getAudioInputStream(buff);
    AudioFormat originalFormat = originalStream.getFormat();
    DataLine.Info info = new DataLine.Info(Clip.class, originalFormat);
    boolean directSupported = AudioSystem.isLineSupported(info);
    AudioInputStream convertedStream;
    if (directSupported)
    {
      convertedStream = originalStream;
    }
    else // format must be converted
    {
      AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
          originalFormat.getSampleRate(), 16, originalFormat.getChannels(),
          originalFormat.getChannels() * 2, originalFormat.getSampleRate(), false);
      convertedStream = AudioSystem.getAudioInputStream(targetFormat, originalStream);
    }
    AudioFormat finalFormat = convertedStream.getFormat();
    DataLine.Info finalInfo = new DataLine.Info(Clip.class, finalFormat);
    clip = (Clip) AudioSystem.getLine(finalInfo);
    clip.open(convertedStream);
  }

  /**
   * Plays the clip once from the start.
   */
  public void playOnce()
  {
    clip.stop();
    clip.setFramePosition(0);
    clip.start();
  }

  /**
   * Loops the clip continuously.
   */
  public void playLoop()
  {
    clip.stop();
    clip.setFramePosition(0);
    clip.loop(Clip.LOOP_CONTINUOUSLY);
  }

  /**
   * Stops the clip.
   */
  public void stop()
  {
    clip.stop();
  }

  /**
   * Checks if the clip is playing.
   *
   * @return true if clip is running
   */
  public boolean isRunning()
  {
    return clip.isRunning();
  }

}
