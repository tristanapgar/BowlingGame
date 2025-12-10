package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.List;
import app.BowlingApplication;
import bowling.LeaderboardEntry;
import visual.dynamic.described.Stage;
import visual.statik.SimpleContent;

/**
 * Displays the leaderboard screen.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class LeaderboardScreen extends Stage
{
  private Rectangle2D playAgainBox;
  private Rectangle2D quitBox;
  private List<LeaderboardEntry> entries;
  private BowlingApplication app;

  /**
   * Creates the leaderboard screen.
   *
   * @param timestep
   *          the tick rate
   * @param app
   *          the main application
   * @param entries
   *          the list of leaderboard entries
   */
  public LeaderboardScreen(final int timestep, final BowlingApplication app,
      final List<LeaderboardEntry> entries)
  {
    super(timestep);
    this.app = app;
    this.entries = entries;
    playAgainBox = new Rectangle2D.Double(250, 600, 220, 60);
    quitBox = new Rectangle2D.Double(550, 600, 220, 60);
    add(new LeaderboardContent());
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
   * Handles button clicks.
   *
   * @param p
   *          the click location
   */
  private void handleClick(final Point2D p)
  {
    if (playAgainBox.contains(p))
      app.launchStartScreen();
    else if (quitBox.contains(p))
      System.exit(0);
  }

  /**
   * Renders the leaderboard contents.
   */
  private class LeaderboardContent implements SimpleContent
  {
    @Override
    public void render(final Graphics g)
    {
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(Color.BLACK);
      g2.fillRect(0, 0, 1015, 715);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Arial", Font.BOLD, 60));
      g2.drawString("LEADERBOARD", 275, 100);
      g2.setFont(new Font("Arial", Font.BOLD, 32));
      g2.drawString("USER", 200, 160);
      g2.drawString("SCORE", 650, 160);
      g2.setFont(new Font("Arial", Font.PLAIN, 28));
      int y = 220;
      int rank = 1;
      for (LeaderboardEntry e : entries)
      {
        g2.drawString(rank + ". " + e.username, 180, y);
        g2.drawString(Integer.toString(e.score), 700, y);
        y += 40;
        rank++;
      }
      drawButton(g2, playAgainBox, "PLAY AGAIN");
      drawButton(g2, quitBox, "QUIT");
    }

    /**
     * Draws a button.
     *
     * @param g2
     *          the graphics object
     * @param r
     *          the button rectangle
     * @param text
     *          the button text
     */
    private void drawButton(final Graphics2D g2, final Rectangle2D r, final String text)
    {
      g2.setColor(Color.DARK_GRAY);
      g2.fill(r);
      g2.setColor(Color.WHITE);
      g2.draw(r);
      g2.setFont(new Font("Arial", Font.BOLD, 28));
      FontMetrics m = g2.getFontMetrics();
      int tx = (int) (r.getX() + r.getWidth() / 2 - m.stringWidth(text) / 2);
      int ty = (int) (r.getY() + r.getHeight() / 2 + m.getAscent() / 2 - 5);
      g2.drawString(text, tx, ty);
    }
  }

}
