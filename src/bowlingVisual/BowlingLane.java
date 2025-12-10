package bowlingVisual;

import java.awt.*;
import java.awt.geom.*;
import visual.statik.SimpleContent;

/**
 * Draws the bowling lane and back wall.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingLane implements SimpleContent
{
  private final Color backWallColor = Color.BLACK; // back wall color
  private final int screenW = 1000;
  private final int screenH = 900;
  private final int backWallWidth = 215;
  private final int backWallHeight = 120;
  private final int laneTopWidth = 180;
  private final int laneBottomWidth = 1100;
  private final int numStrips = 9;
  private Color laneColor;

  /**
   * Creates a bowling lane.
   *
   * @param laneColor
   *          the main lane color
   */
  public BowlingLane(final Color laneColor)
  {
    this.laneColor = laneColor;
  }

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    int backWallX = (screenW - backWallWidth) / 2; // center back wall horizontally
    int backWallY = 100; // 240 down from top of screen
    // construct rectangle for back wall all black
    Rectangle2D backWall = new Rectangle2D.Double(backWallX, backWallY, backWallWidth,
        backWallHeight);
    g2.setColor(backWallColor);
    g2.fill(backWall);
    // top of lane sits under the back wall
    int laneTopY = backWallY + backWallHeight;
    int laneBottomY = screenH; // bottom aligns to bottom of screen
    // computer lane coords
    int laneTopLeftX = (screenW - laneTopWidth) / 2;
    int laneTopRightX = laneTopLeftX + laneTopWidth;
    int laneBottomLeftX = (screenW - laneBottomWidth) / 2;
    int laneBottomRightX = laneBottomLeftX + laneBottomWidth;
    // draw the lane points
    Polygon lane = new Polygon();
    lane.addPoint(laneBottomLeftX, laneBottomY);
    lane.addPoint(laneBottomRightX, laneBottomY);
    lane.addPoint(laneTopRightX, laneTopY);
    lane.addPoint(laneTopLeftX, laneTopY);
    g2.setColor(laneColor);
    g2.fill(lane);
    // lighter brown for 3D lines
    g2.setStroke(new BasicStroke(2f));
    g2.setColor(new Color(160, 90, 20));
    // draw 8 evenly spaced lines
    for (int i = 1; i < numStrips; i++)
    {
      double t = (double) i / numStrips;
      int xTop = (int) (laneTopLeftX + t * (laneTopWidth));
      int xBottom = (int) (laneBottomLeftX + t * (laneBottomWidth));
      Line2D strip = new Line2D.Double(xTop, laneTopY, xBottom, laneBottomY);
      g2.draw(strip);
    }
    // outline lane and back wall in black
    g2.setStroke(new BasicStroke(4f));
    g2.setColor(Color.BLACK);
    g2.draw(backWall);
    g2.draw(lane);
  }

}
