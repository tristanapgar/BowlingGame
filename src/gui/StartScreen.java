package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import javax.sound.sampled.*;
import app.BowlingApplication;
import bowlingVisual.*;
import io.ResourceFinder;
import music.ClipAudio;
import resources.Marker;
import visual.dynamic.described.Stage;

/**
 * Shows the start screen and intro animation.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class StartScreen extends Stage
{
  private BowlingStart startContent;
  private ClipAudio introMusic;
  private ThemeButton themeButton;
  private boolean themeButtonAdded = false;
  private BowlingApplication app;

  /**
   * Creates the start screen.
   *
   * @param timeStep
   *          the tick rate
   * @param app
   *          the main application
   */
  public StartScreen(final int timeStep, final BowlingApplication app)
  {
    super(timeStep);
    this.app = app;
    Background bg = buildBackground();
    add(bg);
    startContent = buildStart();
    add(startContent);
    buildMusic();
    getView().addMouseListener(new StartScreenClickListener(this));
  }

  /**
   * Builds the background.
   *
   * @return the background object
   */
  private Background buildBackground()
  {
    return new Background(Color.BLACK);
  }

  /**
   * Builds the title animation.
   *
   * @return the title object
   */
  private BowlingStart buildStart()
  {
    return new BowlingStart();
  }

  /**
   * Loads and plays the intro sound.
   */
  private void buildMusic()
  {
    ResourceFinder finder = ResourceFinder.createInstance(new Marker());
    try
    {
      introMusic = new ClipAudio(finder, "intro_sound.wav");
      introMusic.playOnce();
    }
    catch (IOException | UnsupportedAudioFileException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void handleTick(final int time)
  {
    startContent.handleTick(time);
    if (!themeButtonAdded && startContentIsFinished())
    {
      themeButton = new ThemeButton();
      add(themeButton);
      themeButtonAdded = true;
    }
    getView().repaint();
  }

  /**
   * Checks if the title animation is finished.
   *
   * @return true if finished
   */
  private boolean startContentIsFinished()
  {
    return startContent.isFinished();
  }

  /**
   * Handles user clicking the screen.
   *
   * @param point
   *          the click location
   */
  public void handleMouseClick(final Point2D point)
  {
    if (themeButtonAdded && themeButton.clicked(point))
    {
      if (introMusic != null)
        introMusic.stop();
      app.launchThemeScreen();
      return;
    }
    if (startContent.isStartClicked(point))
    {
      if (introMusic != null)
        introMusic.stop();
      String name = javax.swing.JOptionPane.showInputDialog(null, "Enter your name:", "Player Name",
          javax.swing.JOptionPane.PLAIN_MESSAGE);
      if (name == null || name.trim().isEmpty())
        name = "PLAYER";
      app.setUsername(name.trim());
      app.launchBowlingScreen();
    }
  }

  /**
   * Mouse listener for the start screen.
   */
  private static class StartScreenClickListener extends MouseAdapter
  {
    private final StartScreen screen;

    /**
     * Creates the listener.
     *
     * @param screen
     *          the start screen
     */
    public StartScreenClickListener(final StartScreen screen)
    {
      this.screen = screen;
    }

    @Override
    public void mouseClicked(final MouseEvent e)
    {
      screen.handleMouseClick(e.getPoint());
    }
  }

}
