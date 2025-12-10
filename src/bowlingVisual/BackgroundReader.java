package bowlingVisual;

import java.awt.image.BufferedImage;
import java.io.IOException;
import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;

/**
 * Loads the background image for the end screen of the game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BackgroundReader
{
  private ResourceFinder finder;

  /**
   * Explicit Value Constructor.
   *
   * @param finder
   *          The ResourceFinder to use
   */
  public BackgroundReader(final ResourceFinder finder)
  {
    this.finder = finder;
  }

  /**
   * Reads and returns the background image.
   *
   * @return the loaded image
   * @throws IOException
   *           if the file cannot be read
   */
  public BufferedImage read() throws IOException
  {
    ImageFactory imageFactory = new ImageFactory(finder);
    BufferedImage image = imageFactory.createBufferedImage("bern.jpg", 4);
    return image;
  }

}
