package bowlingVisual;

import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import visual.statik.SimpleContent;

/**
 * Displays a fading message over the lane.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingSuperImpositions implements SimpleContent
{
  private String message;
  private Point2D location;
  private Font font;
  private int remainingTicks;
  private float opacity = 0f; // current opacity (0–1)
  private int fadeInTicks = 20; // first 20 ticks fade in
  private int fadeOutTicks = 20; // last 20 ticks fade out
  private int totalDuration;

  /**
   * Creates a fading overlay message.
   *
   * @param message
   *          the message text
   * @param location
   *          the display location
   * @param durationTicks
   *          total display time
   */
  public BowlingSuperImpositions(final String message, final Point2D location,
      final int durationTicks)
  {
    this.message = message;
    this.location = location;
    this.font = new Font("SansSerif", Font.BOLD, 50);
    this.remainingTicks = durationTicks;
    this.totalDuration = durationTicks;
  }

  /**
   * Checks if the message is finished.
   *
   * @return true if expired
   */
  public boolean isExpired()
  {
    return remainingTicks <= 0;
  }

  /**
   * Updates fade timing each tick.
   */
  public void tick()
  {
    remainingTicks--;
    int elapsed = totalDuration - remainingTicks;
    // Fade in
    if (elapsed < fadeInTicks)
    {
      opacity = (float) elapsed / fadeInTicks;
    }
    // Fade out
    else if (remainingTicks < fadeOutTicks)
    {
      opacity = (float) remainingTicks / fadeOutTicks;
    }
    // Fully visible
    else
    {
      opacity = 1f;
    }
  }

  @Override
  public void render(final Graphics g)
  {
    if (remainingTicks <= 0 || message == null || message.isEmpty())
      return;
    Graphics2D g2 = (Graphics2D) g.create();
    g2.setFont(font);
    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
    TextLayout layout = new TextLayout(message, font, g2.getFontRenderContext());
    Shape outline = layout.getOutline(null);
    Rectangle2D bounds = outline.getBounds2D();
    double x = location.getX() - bounds.getWidth() / 2;
    double y = location.getY() + bounds.getHeight() / 2;
    g2.translate(x, y);
    g2.setColor(Color.BLACK);
    g2.draw(outline);
    g2.setColor(Color.YELLOW);
    g2.fill(outline);
    g2.dispose();
  }

}
