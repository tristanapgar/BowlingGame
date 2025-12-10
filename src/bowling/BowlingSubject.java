package bowling;

/**
 * BowlingSubject for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public interface BowlingSubject
{
  /**
   * Add observer to the subject.
   *
   * @param observer
   *          to be added
   */
  public void addObserver(final BowlingObserver observer);

  /**
   * Remove observer from the subject.
   *
   * @param observer
   *          to be removed
   */
  public void removeObserver(final BowlingObserver observer);

  /**
   * Notify the observers.
   */
  public void notifyObservers();
}
