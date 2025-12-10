package bowling;

/**
 * BowlingObserver for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public interface BowlingObserver
{
  /**
   * Reset observers if notified.
   */
  public abstract void reset();

  /**
   * Update after being notified.
   */
  public abstract void update();
}
