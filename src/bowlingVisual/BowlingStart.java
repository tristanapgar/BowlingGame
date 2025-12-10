package bowlingVisual;

import java.awt.*;
import java.awt.geom.*;
import visual.statik.SimpleContent;

/**
 * Displays the animated start screen title and start button.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingStart implements SimpleContent
{
  private final String title = "BOWLING"; // title screen text
  private String displayed = ""; // visible portion of title
  private int letterIndex = 0; // index of next char to reveal
  private int timer = 0; // ticking timer
  private boolean finishedTitle = false; // is title fully displayed

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    int screenW = 1000;
    int screenH = 900;
    // draw title letters in white
    g2.setColor(Color.WHITE);
    g2.setFont(new Font("SansSerif", Font.BOLD, 140));
    FontMetrics fm = g2.getFontMetrics();
    int titleX = (screenW - fm.stringWidth(displayed)) / 2;
    int titleY = screenH / 2 - 100;
    g2.drawString(displayed, titleX, titleY);
    // draw START button once finished with title
    if (finishedTitle)
    {
      int btnWidth = 240;
      int btnHeight = 80;
      int btnX = (screenW - btnWidth) / 2;
      int btnY = titleY + 150;
      // button background
      g2.setColor(Color.BLACK);
      g2.fillRect(btnX, btnY, btnWidth, btnHeight);
      // button border
      g2.setColor(Color.WHITE);
      g2.setStroke(new BasicStroke(4));
      g2.drawRect(btnX, btnY, btnWidth, btnHeight);
      // draw START label horizontally centered
      String text = "START";
      g2.setFont(new Font("SansSerif", Font.BOLD, 40));
      FontMetrics fmBtn = g2.getFontMetrics();
      int textX = btnX + (btnWidth - fmBtn.stringWidth(text)) / 2;
      int textY = btnY + ((btnHeight - fmBtn.getHeight()) / 2) + fmBtn.getAscent();
      g2.drawString(text, textX, textY);
    }
  }

  /**
   * Handles the title animation tick.
   *
   * @param time
   *          the tick count
   */
  public void handleTick(final int time)
  {
    timer++;
    // reveal one char every 15 ticks
    if (timer % 15 == 0 && letterIndex < title.length())
    {
      displayed += title.charAt(letterIndex++);
      // mark when animation is finished
      if (letterIndex == title.length())
        finishedTitle = true;
    }
  }

  /**
   * Checks if title animation is finished.
   *
   * @return true if finished
   */
  public boolean isFinished()
  {
    return finishedTitle;
  }

  /**
   * Checks if the start button was clicked.
   *
   * @param point
   *          the click location
   * @return true if inside start button
   */
  public boolean isStartClicked(final Point2D point)
  {
    // returns true when user clicks an area inside of start border
    if (!finishedTitle)
      return false;
    int btnWidth = 240;
    int btnHeight = 80;
    int btnX = (1000 - btnWidth) / 2;
    int btnY = (900 / 2 - 100) + 150;
    return point.getX() >= btnX && point.getX() <= btnX + btnWidth && point.getY() >= btnY
        && point.getY() <= btnY + btnHeight;
  }

}
