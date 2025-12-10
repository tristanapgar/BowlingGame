package app;

import java.awt.event.*;
import java.util.*;
import bowling.GameTheme;
import bowling.LeaderboardEntry;
import gui.*;

/**
 * Main Application for the Bowling Game.
 *
 * @author Tristan Apgar & Jacob Noel
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingApplication extends JApplication implements ActionListener
{
  public static final int WIDTH = 1000;
  public static final int HEIGHT = 700;
  private GameTheme currentTheme = new GameTheme(GameTheme.ThemeType.BASIC);
  private StartScreen startScreen;
  private BowlingScreen bowlingScreen;
  private List<LeaderboardEntry> leaderboard = new ArrayList<>();
  private String currentUsername = "PLAYER";

  /**
   * Creates the bowling application.
   *
   * @param args
   *          command-line arguments
   */
  public BowlingApplication(final String[] args)
  {
    super(WIDTH, HEIGHT);
  }

  /**
   * Gets the current theme.
   *
   * @return current theme
   */
  public GameTheme getTheme()
  {
    return currentTheme;
  }

  /**
   * Adds a score to the leaderboard.
   *
   * @param name
   *          player name
   * @param score
   *          player score
   */
  public void addScore(final String name, final int score)
  {
    leaderboard.add(new LeaderboardEntry(name, score));
    leaderboard.sort((a, b) -> b.score - a.score); // highest first
  }

  /**
   * Sets the active theme.
   *
   * @param t
   *          theme type
   */
  public void setTheme(final GameTheme.ThemeType t)
  {
    this.currentTheme = new GameTheme(t);
  }

  /**
   * Gets the active username.
   *
   * @param name
   *          username
   */
  public String getUsername()
  {
    return currentUsername;
  }

  /**
   * Sets the active username.
   *
   * @param name
   *          username
   */
  public void setUsername(String name)
  {
    this.currentUsername = name;
  }

  @Override
  public void init()
  {
    startScreen = new StartScreen(17, this);
    startScreen.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(startScreen.getView());
    startScreen.start();
    getContentPane().revalidate();
    getContentPane().repaint();
  }

  /**
   * Switches to the bowling gameplay screen.
   */
  public void launchBowlingScreen()
  {
    getContentPane().remove(startScreen.getView());
    bowlingScreen = new BowlingScreen(30, currentTheme, this, currentUsername);
    bowlingScreen.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(bowlingScreen.getView());
    bowlingScreen.start();
    getContentPane().revalidate();
    getContentPane().repaint();
    bowlingScreen.getView().requestFocusInWindow();
  }

  /**
   * Switches back to the start screen.
   */
  public void launchStartScreen()
  {
    getContentPane().removeAll();
    startScreen = new StartScreen(17, this);
    startScreen.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(startScreen.getView());
    startScreen.start();
    getContentPane().revalidate();
    getContentPane().repaint();
  }

  /**
   * Switches to the theme selection screen.
   */
  public void launchThemeScreen()
  {
    getContentPane().removeAll();
    ThemeScreen theme = new ThemeScreen(17, this);
    theme.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(theme.getView());
    theme.start();
    getContentPane().revalidate();
    getContentPane().repaint();
  }

  /**
   * Switches to the end screen.
   *
   * @param finalScore
   *          final game score
   */
  public void launchEndScreen(final int finalScore)
  {
    getContentPane().removeAll();
    EndScreen end = new EndScreen(17, this, finalScore);
    end.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(end.getView());
    end.start();
    getContentPane().revalidate();
    getContentPane().repaint();
  }

  /**
   * Switches to the leaderboard screen.
   */
  public void launchLeaderboardScreen()
  {
    getContentPane().removeAll();
    LeaderboardScreen ls = new LeaderboardScreen(17, this, leaderboard);
    ls.getView().setBounds(0, 0, WIDTH, HEIGHT);
    getContentPane().add(ls.getView());
    ls.start();
    getContentPane().revalidate();
    getContentPane().repaint();
  }

  @Override
  public void actionPerformed(final ActionEvent e)
  {
    // empty but potential for future implementation!
  }

  /**
   * Launches the application.
   *
   * @param args
   *          command-line arguments
   */
  public static void main(final String[] args)
  {
    JApplication app = new BowlingApplication(args);
    JApplication.invokeInEventDispatchThread(app);
  }

}
