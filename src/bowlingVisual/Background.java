package bowlingVisual;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import visual.statik.SimpleContent;

/**
 * Draws the game background and instructions.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class Background implements SimpleContent
{
  private Color color;

  /**
   * Creates a background.
   *
   * @param color
   *          the fill color
   */
  public Background(final Color color)
  {
    this.color = color;
  }

  @Override
  public void render(final Graphics g)
  {
    // draw background
    Rectangle2D.Double rect = new Rectangle2D.Double(0, 0, 1015, 715);
    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(color);
    g2.fill(rect);
    g2.draw(rect);
    // on-screen instructions
    g2.setColor(Color.BLACK);
    g2.setFont(new Font("SansSerif", Font.BOLD, 20));
    // left of lane
    int leftX = 10;
    int leftY = 200;
    g2.drawString("           ROLLING     ", leftX, leftY - 50);
    g2.drawString("1: Move ← / → keys", leftX, leftY);
    g2.drawString("    to choose release point", leftX, leftY + 30);
    g2.drawString("    Press SPACE ONCE", leftX, leftY + 60);
    // right of lane
    int rightX = 750;
    int rightY = 200;
    g2.drawString("     INSTRUCTIONS     ", rightX, rightY - 50);
    g2.drawString("2: Move ← / → keys", rightX, rightY);
    g2.drawString("    to choose roll angle", rightX, rightY + 30);
    g2.drawString("    Press SPACE to roll", rightX, rightY + 60);
  }

}
