package bowling;

import java.util.*;

/**
 * Stores and ranks bowling score entries.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class Leaderboard
{
  private final List<Entry> entries = new ArrayList<>();

  /**
   * A single score entry.
   */
  public static class Entry
  {
    public final String user;
    public final int score;

    /**
     * Creates a leaderboard entry.
     *
     * @param user
     *          the player's name
     * @param score
     *          the player's score
     */
    public Entry(final String user, final int score)
    {
      this.user = user;
      this.score = score;
    }
  }

  /**
   * Adds a score to the leaderboard.
   *
   * @param user
   *          the player's name
   * @param score
   *          the player's score
   */
  public void addEntry(final String user, final int score)
  {
    entries.add(new Entry(user, score));
    entries.sort(Comparator.comparingInt(e -> -e.score));
  }

  /**
   * Gets the ranked entries.
   *
   * @return an unmodifiable list of entries
   */
  public List<Entry> getEntries()
  {
    return Collections.unmodifiableList(entries);
  }

}
