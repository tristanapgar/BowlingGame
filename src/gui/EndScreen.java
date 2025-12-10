package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.IOException;
import app.BowlingApplication;
import bowlingVisual.BackgroundReader;
import io.ResourceFinder;
import music.ClipAudio;
import resources.Marker;
import visual.dynamic.described.Stage;

/**
 * EndScreen shown after the game finishes.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class EndScreen extends Stage
{
  private BowlingApplication app;
  private int finalScore;
  private Rectangle2D playAgainBox;
  private Rectangle2D quitBox;
  private Rectangle2D leaderboardBox;
  private Image backgroundImage;
  private ClipAudio outroMusic;

  /**
   * Creates the end screen.
   *
   * @param timeStep
   *          the tick rate
   * @param app
   *          the main application
   * @param finalScore
   *          the user's final score
   */
  public EndScreen(final int timeStep, final BowlingApplication app, final int finalScore)
  {
    super(timeStep);
    this.app = app;
    this.finalScore = finalScore;
    String username = app.getUsername();
    app.addScore(username, finalScore);
    try
    {
      ResourceFinder finder = ResourceFinder.createInstance(new Marker());
      BackgroundReader reader = new BackgroundReader(finder);
      backgroundImage = reader.read();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    playAgainBox = new Rectangle2D.Double(150, 560, 220, 60);
    leaderboardBox = new Rectangle2D.Double(390, 560, 220, 60);
    quitBox = new Rectangle2D.Double(630, 560, 220, 60);
    add(new EndScreenContent());
    playOutroMusic();
    getView().addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(final MouseEvent e)
      {
        handleClick(e.getPoint());
      }
    });
  }

  /**
   * Plays the outro music once.
   */
  private void playOutroMusic()
  {
    try
    {
      ResourceFinder finder = ResourceFinder.createInstance(new Marker());
      outroMusic = new ClipAudio(finder, "outro_music.wav");
      outroMusic.playOnce();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Stops the outro music.
   */
  private void stopOutro()
  {
    if (outroMusic != null)
    {
      outroMusic.stop();
    }
  }

  /**
   * Handles button clicks on the end screen.
   *
   * @param p
   *          the click location
   */
  private void handleClick(final Point2D p)
  {
    stopOutro();
    if (playAgainBox.contains(p))
    {
      app.launchStartScreen();
    }
    else if (leaderboardBox.contains(p))
    {
      app.launchLeaderboardScreen();
    }
    else if (quitBox.contains(p))
    {
      System.exit(0);
    }
  }

  /**
   * Inner content class for rendering visuals.
   */
  private class EndScreenContent implements visual.statik.SimpleContent
  {
    @Override
    public void render(final Graphics g)
    {
      Graphics2D g2 = (Graphics2D) g;
      g2.drawImage(backgroundImage, 0, 0, 1015, 715, null);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Arial", Font.BOLD, 64));
      String scoreText = "FINAL SCORE: " + finalScore;
      int w = g2.getFontMetrics().stringWidth(scoreText);
      g2.drawString(scoreText, 500 - w / 2, 150);
      drawButton(g2, playAgainBox, "PLAY AGAIN", Color.DARK_GRAY);
      drawButton(g2, leaderboardBox, "LEADERBOARD", Color.DARK_GRAY);
      drawButton(g2, quitBox, "QUIT", Color.DARK_GRAY);
    }

    /**
     * Draws a menu button.
     *
     * @param g2
     *          the graphics object
     * @param r
     *          the button rectangle
     * @param label
     *          the text shown
     * @param fill
     *          the fill color
     */
    private void drawButton(final Graphics2D g2, final Rectangle2D r, final String label,
        final Color fill)
    {
      g2.setColor(fill);
      g2.fill(r);
      g2.setColor(Color.WHITE);
      g2.draw(r);
      g2.setFont(new Font("Arial", Font.BOLD, 28));
      FontMetrics m = g2.getFontMetrics();
      int tx = (int) (r.getX() + r.getWidth() / 2 - m.stringWidth(label) / 2);
      int ty = (int) (r.getY() + r.getHeight() / 2 + m.getAscent() / 2 - 5);
      g2.drawString(label, tx, ty);
    }
  }

}
