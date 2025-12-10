package gui;

import java.awt.*;
import java.awt.geom.*;
import visual.statik.SimpleContent;

/**
 * Button that leads user to the end screen.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class EndScreenButton implements SimpleContent
{
  private Rectangle2D bounds;
  private String label;

  /**
   * Creates a end screen box.
   *
   * @param label
   *          the message text
   * @param x
   *          the x location
   * @param y
   *          the y location
   * @param w
   *          width
   * @param h
   *          the height
   */
  public EndScreenButton(final String label, final int x, final int y, final int w, final int h)
  {
    this.label = label;
    this.bounds = new Rectangle2D.Double(x, y, w, h);
  }

  /**
   * Determine if clicked.
   *
   * @param p
   *          the point
   */
  public boolean clicked(final Point2D p)
  {
    return bounds.contains(p);
  }

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(Color.DARK_GRAY);
    g2.fill(bounds);
    g2.setColor(Color.WHITE);
    g2.draw(bounds);
    g2.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 28));
    FontMetrics fm = g2.getFontMetrics();
    int tx = (int) (bounds.getX() + bounds.getWidth() / 2 - fm.stringWidth(label) / 2);
    int ty = (int) (bounds.getY() + bounds.getHeight() / 2 + fm.getAscent() / 2 - 4);
    g2.drawString(label, tx, ty);
  }

}
