package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import app.BowlingApplication;
import bowling.GameTheme;
import visual.dynamic.described.Stage;
import visual.statik.SimpleContent;

/**
 * Theme selection screen for the game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class ThemeScreen extends Stage
{
  private BowlingApplication app;
  private Rectangle2D basicBox = new Rectangle2D.Double(300, 200, 300, 60);
  private Rectangle2D jmuBox = new Rectangle2D.Double(300, 290, 300, 60);
  private Rectangle2D pinkBox = new Rectangle2D.Double(300, 380, 300, 60);
  private Rectangle2D redBlueBox = new Rectangle2D.Double(300, 470, 300, 60);

  /**
   * Creates a theme screen.
   *
   * @param timeStep
   *          the timestep for animation
   * @param app
   *          the application controller
   */
  public ThemeScreen(final int timeStep, final BowlingApplication app)
  {
    super(timeStep);
    this.app = app;
    add(new ThemeMenuContent());
    getView().addMouseListener(new MouseHandler());
  }

  /**
   * Handles mouse clicks on theme options.
   */
  private class MouseHandler extends MouseAdapter
  {
    @Override
    public void mouseClicked(final MouseEvent e)
    {
      Point2D p = e.getPoint();

      if (basicBox.contains(p))
      {
        app.setTheme(GameTheme.ThemeType.BASIC);
        app.launchStartScreen();
      }
      else if (jmuBox.contains(p))
      {
        app.setTheme(GameTheme.ThemeType.JMU);
        app.launchStartScreen();
      }
      else if (pinkBox.contains(p))
      {
        app.setTheme(GameTheme.ThemeType.PINK);
        app.launchStartScreen();
      }
      else if (redBlueBox.contains(p))
      {
        app.setTheme(GameTheme.ThemeType.REDBLUE);
        app.launchStartScreen();
      }
    }
  }

  /**
   * Draws the theme menu.
   */
  private class ThemeMenuContent implements SimpleContent
  {

    @Override
    public void render(final Graphics g)
    {
      Graphics2D g2 = (Graphics2D) g;
      Rectangle2D full = new Rectangle2D.Double(0, 0, 1015, 715);
      g2.setColor(Color.DARK_GRAY);
      g2.fill(full);
      g2.draw(full);
      g2.setColor(Color.WHITE);
      g2.setFont(new Font("Arial", Font.BOLD, 36));
      g2.drawString("Select Theme", 360, 150);
      drawButton(g2, basicBox, "BASIC", Color.LIGHT_GRAY);
      drawButton(g2, jmuBox, "JMU", new Color(69, 0, 132));
      drawButton(g2, pinkBox, "PINK", new Color(255, 150, 180));
      drawButton(g2, redBlueBox, "RED & BLUE", new Color(80, 80, 200));
    }

    /**
     * Draws a single menu button.
     *
     * @param g2
     *          the graphics object
     * @param r
     *          the button rectangle
     * @param label
     *          the button label
     * @param fill
     *          the fill color
     */
    private void drawButton(final Graphics2D g2, final Rectangle2D r, final String label,
        final Color fill)
    {
      g2.setColor(fill);
      g2.fill(r);
      g2.setColor(Color.BLACK);
      g2.draw(r);
      g2.setFont(new Font("Arial", Font.BOLD, 24));
      g2.drawString(label, (int) (r.getX() + 20), (int) (r.getY() + 38));
    }
  }

}
