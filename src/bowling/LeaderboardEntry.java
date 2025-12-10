package bowling;

/**
 * Stores a single leaderboard score.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class LeaderboardEntry
{
  public String username;
  public int score;

  /**
   * Creates a leaderboard entry.
   *
   * @param u
   *          the player's username
   * @param s
   *          the player's score
   */
  public LeaderboardEntry(final String u, final int s)
  {
    username = u;
    score = s;
  }

}
