package bowlingVisual;

import java.awt.image.BufferedImage;
import java.io.IOException;
import io.ResourceFinder;
import visual.statik.sampled.ImageFactory;

/**
 * ScoreBoardReader for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class ScoreBoardReader
{
  private ResourceFinder finder;

  /**
   * Explicit Value Constructor.
   *
   * @param finder
   *          The ResourceFinder to use
   */
  public ScoreBoardReader(final ResourceFinder finder)
  {
    this.finder = finder;
  }

  /**
   * Reads the image.
   *
   * @return the image read
   * @throws IOException
   *           if failed to read image
   */
  public BufferedImage read() throws IOException
  {
    ImageFactory imageFactory = new ImageFactory(finder);
    BufferedImage image = imageFactory.createBufferedImage("scoreSheet.png", 4);
    return image;
  }

}
