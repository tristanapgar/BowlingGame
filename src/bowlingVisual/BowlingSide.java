package bowlingVisual;

import java.awt.*;
import visual.statik.SimpleContent;

/**
 * Draws the angled side edges of the lane.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingSide implements SimpleContent
{
  private final int screenW = 1000;
  private final int screenH = 900;
  private final int backWallY = 100;
  private final int backWallWidth = 220;
  private final int laneBottomWidth = 1100;
  private final int gutterWidth = 100;

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.BLACK);
    g2.setStroke(new BasicStroke(4f));
    // top corners of back wall
    int backWallTopY = backWallY;
    int backWallTopLeftX = (screenW - backWallWidth) / 2;
    int backWallTopRightX = backWallTopLeftX + backWallWidth;
    // bottom of the lane
    int laneBottomY = screenH;
    int laneBottomLeftX = (screenW - laneBottomWidth) / 2;
    int laneBottomRightX = laneBottomLeftX + laneBottomWidth;
    // black line on outside of gutters
    int gutterOuterLeftX = laneBottomLeftX - gutterWidth;
    int gutterOuterRightX = laneBottomRightX + gutterWidth;
    // draw left side line
    g2.drawLine(gutterOuterLeftX + 25, laneBottomY, backWallTopLeftX, backWallTopY);
    // draw right side line
    g2.drawLine(gutterOuterRightX - 25, laneBottomY, backWallTopRightX, backWallTopY);
  }

}
