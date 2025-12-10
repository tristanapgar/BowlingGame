package sprites;

import java.awt.*;
import java.awt.event.*;
import visual.dynamic.described.RuleBasedSprite;
import visual.statik.described.TransformableContent;

/**
 * Bowling gutter graphics for the lane.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingGutter extends RuleBasedSprite implements KeyListener
{
  private final int screenW = 1000;
  private final int screenH = 900;
  private final int gutterWidth = 100;
  private final int backWallY = 100;
  private final int backWallHeight = 120;
  private final int laneTopWidth = 180;
  private final int laneBottomWidth = 1100;

  /**
   * Creates the gutter object.
   *
   * @param content
   *          the visual content
   */
  public BowlingGutter(final TransformableContent content)
  {
    super(content);
    setLocation(0, 0);
  }

  @Override
  public void handleTick(final int time)
  {
  }

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    // variables to help plot coords
    int wallBottomY = backWallY + backWallHeight;
    int laneTopY = wallBottomY;
    int laneBottomY = screenH;
    int laneTopLeftX = (screenW - laneTopWidth) / 2;
    int laneTopRightX = laneTopLeftX + laneTopWidth;
    int laneBottomLeftX = (screenW - laneBottomWidth) / 2;
    int laneBottomRightX = laneBottomLeftX + laneBottomWidth;
    double taper = (double) laneTopWidth / laneBottomWidth;
    int topGutterWidth = (int) (gutterWidth * taper);
    // plot left side gutter
    Polygon leftGutter = new Polygon();
    leftGutter.addPoint(laneBottomLeftX - gutterWidth, laneBottomY);
    leftGutter.addPoint(laneBottomLeftX, laneBottomY);
    leftGutter.addPoint(laneTopLeftX, laneTopY);
    leftGutter.addPoint(laneTopLeftX - topGutterWidth, laneTopY);
    // plot right side gutter
    Polygon rightGutter = new Polygon();
    rightGutter.addPoint(laneBottomRightX, laneBottomY);
    rightGutter.addPoint(laneBottomRightX + gutterWidth, laneBottomY);
    rightGutter.addPoint(laneTopRightX + topGutterWidth, laneTopY);
    rightGutter.addPoint(laneTopRightX, laneTopY);
    // fill gutter in gray with a black outline
    g2.setColor(new Color(60, 60, 60));
    g2.fill(leftGutter);
    g2.fill(rightGutter);
    g2.setColor(Color.BLACK);
    g2.setStroke(new BasicStroke(4f));
    g2.draw(leftGutter);
    g2.draw(rightGutter);
  }

  @Override
  public void keyPressed(final KeyEvent e)
  {
  }

  @Override
  public void keyReleased(final KeyEvent e)
  {
  }

  @Override
  public void keyTyped(final KeyEvent e)
  {
  }

}
