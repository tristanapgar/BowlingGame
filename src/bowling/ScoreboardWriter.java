package bowling;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * ScoreboardWriter for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class ScoreboardWriter
{
  private static final Font FONT = new Font(Font.SANS_SERIF, Font.BOLD, 24);
  private static final int SPACING = 25;
  private static final int SET_SPACING = 24;
  private static final String GUTTER = "-";
  private static final String STRIKE = "X";
  private static final String SPARE = "/";

  /**
   * Renders the score on the score board.
   *
   * @param location
   *          to use
   * @param username
   *          to use
   * @param rollScores
   *          to use
   * @param totalScores
   *          to use
   * @param g2
   *          to use
   */
  public static void renderScore(final Point2D location, final String username,
      final ArrayList<Integer> rollScores, final ArrayList<Integer> totalScores,
      final Graphics2D g2)
  {
    g2.setFont(FONT);
    FontMetrics metrics = g2.getFontMetrics(FONT);
    int xStart = (int) location.getX();
    int y = (int) location.getY();
    g2.setColor(Color.BLACK);
    drawCentered(g2, username, 70, 50, metrics);
    int rollIndex = 0;
    // draw frames 1-9
    for (int set = 1; set <= 9; set++)
    {
      if (rollIndex >= rollScores.size())
      {
        break;
      }
      int xFrame = xStart + (set - 1) * (2 * SPACING + SET_SPACING);
      int r1 = rollScores.get(rollIndex);
      String rollText1 = (r1 == 10) ? STRIKE : (r1 == 0 ? GUTTER : Integer.toString(r1));
      drawCentered(g2, rollText1, xFrame, y, metrics);
      if (r1 == 10)
      {
        rollIndex += 1;
      }
      else
      {
        int r2 = (rollIndex + 1 < rollScores.size()) ? rollScores.get(rollIndex + 1) : -1;
        String rollText2;
        if (r2 < 0)
        {
          rollText2 = "";
        }
        else if (r1 + r2 == 10)
        {
          rollText2 = SPARE;
        }
        else if (r2 == 0)
        {
          rollText2 = GUTTER;
        }
        else
        {
          rollText2 = Integer.toString(r2);
        }
        drawCentered(g2, rollText2, xFrame + SPACING, y, metrics);
        rollIndex += 2;
      }
      if (set - 1 < totalScores.size())
      {
        String totalText = Integer.toString(totalScores.get(set - 1));
        drawCentered(g2, totalText, xFrame + SPACING / 2, y + metrics.getHeight(), metrics);
      }
    }
    // frame 10
    int frame10X = xStart + 9 * (2 * SPACING + SET_SPACING);
    if (rollIndex < rollScores.size())
    {
      int r1 = rollScores.get(rollIndex);
      int r2 = (rollIndex + 1 < rollScores.size()) ? rollScores.get(rollIndex + 1) : -1;
      drawCentered(g2, formatRoll10(r1, -1), frame10X, y, metrics); // roll 1
      if (r2 >= 0)
      {
        drawCentered(g2, formatRoll10(r2, r1), frame10X + SPACING, y, metrics); // roll 2
      }
      // total score for frame 10, off to the right
      if (totalScores.size() >= 10)
      {
        String totalText = Integer.toString(totalScores.get(9));
        int totalX = frame10X + 3 * SPACING + 20; // move to the right
        int totalY = y + metrics.getHeight() / 2;
        g2.drawString(totalText, totalX, totalY);
      }
    }
  }

  /**
   * Helper for drawing strings.
   *
   * @param g2
   *          to use
   * @param text
   *          to set
   * @param x
   *          position
   * @param y
   *          position
   * @param m
   *          metric to use
   */
  private static void drawCentered(final Graphics2D g2, final String text, final int x, final int y,
      final FontMetrics m)
  {
    int tx = x - m.stringWidth(text) / 2;
    int ty = y + m.getAscent() / 2;
    g2.drawString(text, tx, ty);
  }

  /**
   * Special format for roll.
   *
   * @param value
   *          to set
   * @param prevRoll
   *          to consider
   * @return the current score
   */
  private static String formatRoll10(final int value, final int prevRoll)
  {
    if (value == 10)
    {
      return STRIKE;
    }
    if (prevRoll >= 0 && prevRoll + value == 10)
    {
      return SPARE;
    }
    if (value == 0)
    {
      return GUTTER;
    }
    return Integer.toString(value);
  }

}
