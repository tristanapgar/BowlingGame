package bowling;

/**
 * BowlingBallController for BowlingScreen.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public interface BowlingBallController
{
  /**
   * Starts the roll at angle.
   *
   * @param angle
   *          to be used for roll
   */
  void startRoll(final double angle);

  /**
   * resets the ball for next roll.
   */
  void resetBall();

  /**
   * resets the pins for next roll.
   */
  void resetPins();

  /**
   * Shows the message for super imposition.
   *
   * @param message
   *          to display
   */
  void showMessage(final String message);
}
