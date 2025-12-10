package sprites;

import visual.dynamic.described.RuleBasedSprite;
import visual.statik.described.TransformableContent;

/**
 * BallReflection for BowlingBall.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BallReflection extends RuleBasedSprite
{
  private double originX, originY;

  /**
   * Constructor for BallReflection.
   *
   * @param reflection
   *          content to be used
   */
  public BallReflection(final TransformableContent reflection)
  {
    super(reflection);
  }

  /**
   * Sets the location.
   *
   * @param x
   *          coordinate to set
   * @param y
   *          coordinate to set
   */
  public void setOrigin(final double x, final double y)
  {
    originX = x;
    originY = y;
    setLocation(originX, originY);
  }

  @Override
  public void handleTick(final int millis)
  {
    double scale = 1.0 - 0.8 * (originY / 650.0); // adjust factor
    setScale(scale);
    setLocation(originX, originY);
  }

}
