package gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.*;
import javax.swing.SwingUtilities;
import app.BowlingApplication;
import bowling.*;
import bowlingVisual.*;
import io.ResourceFinder;
import music.ClipAudio;
import visual.dynamic.described.*;
import visual.statik.described.*;
import resources.Marker;
import sprites.*;

/**
 * BowlingScreen for the main bowling gameplay.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingScreen extends Stage implements BowlingBallController
{
  private static final int PIN_RESET_DELAY_TICKS = 20;
  private int pinResetCounter = -1;
  private ArrayList<BowlingPin> pins;
  private GameTheme theme;
  private GameState gameState;
  private BowlingBall ball;
  private BowlingSuperImpositions currentMessage = null;
  private EndGamePopup endGamePopup = null;
  private BowlingApplication app;
  private ClipAudio bgMusic;
  private ClipAudio pinHitSound;
  private ClipAudio strikeSound;
  private ClipAudio spareSound;
  private ClipAudio gutterSound;

  /**
   * Creates the gameplay screen.
   *
   * @param timeStep
   *          the tick rate
   * @param theme
   *          the selected theme
   * @param app
   *          the main application
   */
  public BowlingScreen(final int timeStep, final GameTheme theme, final BowlingApplication app, String username)
  {
    super(timeStep);
    this.theme = theme;
    this.app = app;
    this.gameState = new GameState();
    this.gameState.setBowlingScreen(this);

    // build visuals
    Background bg = buildBackground();
    add(bg);
    BowlingSide side = buildSide();
    add(side);
    BowlingLane lane = buildLane();
    add(lane);
    BowlingGutter gutter = buildGutter();
    add(gutter);
    ScoreBoard scoreboard = buildScoreBoard();
    add(scoreboard);
    gameState.addObserver(scoreboard);
    this.pins = buildPins();
    this.ball = buildBall();
    this.ball.setGameState(gameState);
    this.gameState.setUserName(username);
    // add pins, set antagonists
    for (int i = 0; i < pins.size(); i++)
    {
      gameState.addPin(i, pins.get(i));
    }
    for (BowlingPin pin : pins)
    {
      pin.setGameState(gameState);
      ball.addAntagonist(pin);
      for (BowlingPin other : pins)
      {
        if (other != pin)
          pin.addAntagonist(other);
      }
      add(pin);
    }
    add(ball);
    ball.setMetronome(getMetronome());
    getView().addKeyListener(ball);
    getView().setFocusable(true);
    SwingUtilities.invokeLater(() -> getView().requestFocusInWindow());
    // audio
    buildMusic();
    if (bgMusic != null)
      bgMusic.playLoop();
    gameState.setBallController(this);
    // add reflection sprite
    add(ball.reflectionSprite);
  }

  /**
   * Loads all audio clips.
   */
  private void buildMusic()
  {
    ResourceFinder finder = ResourceFinder.createInstance(new Marker());
    try
    {
      bgMusic = new ClipAudio(finder, "bowlingscreen_bg_music.wav");
      pinHitSound = new ClipAudio(finder, "pin_strike.wav");
      strikeSound = new ClipAudio(finder, "strike_sound.wav");
      spareSound = new ClipAudio(finder, "spare_sound.wav");
      gutterSound = new ClipAudio(finder, "gutter_sound.wav");
    }
    catch (IOException | UnsupportedAudioFileException | LineUnavailableException e)
    {
      e.printStackTrace();
    }
  }

  /**
   * Plays the pin hit sound.
   */
  public void playPinHitSound()
  {
    if (pinHitSound != null)
      pinHitSound.playOnce();
  }

  /**
   * Plays the strike sound.
   */
  public void playStrikeSound()
  {
    if (strikeSound != null)
      strikeSound.playOnce();
  }

  /**
   * Plays the spare sound.
   */
  public void playSpareSound()
  {
    if (spareSound != null)
      spareSound.playOnce();
  }

  /**
   * Plays the gutter sound.
   */
  public void playGutterSound()
  {
    if (gutterSound != null)
      gutterSound.playOnce();
  }

  /**
   * Stops all audio.
   */
  private void stopAllAudio()
  {
    if (bgMusic != null)
      bgMusic.stop();
    if (spareSound != null)
      spareSound.stop();
  }

  /**
   * Builds the scoreboard.
   *
   * @return the scoreboard object
   */
  private ScoreBoard buildScoreBoard()
  {
    GameState gameState = this.gameState;
    ResourceFinder finder = ResourceFinder.createInstance(new Marker());
    ScoreBoardReader reader = new ScoreBoardReader(finder);
    Point2D location = new Point2D.Double(177, 40);
    try
    {
      return new ScoreBoard(gameState, reader.read(), Color.blue, location);
    }
    catch (IOException e)
    {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Builds the gutter.
   *
   * @return the gutter object
   */
  private BowlingGutter buildGutter()
  {
    Rectangle2D s = new Rectangle2D.Double(0, 0, 1, 1);
    TransformableContent content = new Content(s, null, null, null);
    BowlingGutter gutter = new BowlingGutter(content);
    getView().addKeyListener(gutter);
    return gutter;
  }

  /**
   * Builds the bowling ball.
   *
   * @return the ball
   */
  private BowlingBall buildBall()
  {
    Ellipse2D outer = new Ellipse2D.Double(-30, -30, 70, 70);
    Color oc = theme.ballOuterColor;
    TransformableContent outerContent = new Content(outer, oc, oc, null);
    Ellipse2D inner = new Ellipse2D.Double(-10, -10, 10, 10);
    Color ic = theme.ballInnerColor;
    TransformableContent innerContent = new Content(inner, ic, ic, null);
    return new BowlingBall(outerContent, null, innerContent);
  }

  /**
   * Builds all pins.
   *
   * @return list of pins
   */
  private ArrayList<BowlingPin> buildPins()
  {
    Rectangle2D frontPin = new Rectangle2D.Double(0, 0, 20, 60);
    Polygon topPin = new Polygon();
    topPin.addPoint(0, 0);
    topPin.addPoint(20, 0);
    topPin.addPoint(15, -3);
    topPin.addPoint(5, -3);
    double[][] positions = {{490, 210}, {455, 195}, {525, 195}, {435, 180}, {490, 180}, {545, 180},
        {415, 170}, {465, 170}, {515, 170}, {565, 170}};
    ArrayList<BowlingPin> pins = new ArrayList<>();
    double radius = 10;
    for (int i = positions.length - 1; i >= 0; i--)
    {
      CompositeContent content = new CompositeContent();
      Color outline = (theme.getType() == GameTheme.ThemeType.BASIC) ? Color.WHITE : Color.BLACK;
      content.add(new Content(topPin, outline, theme.pinColor, null));
      content.add(new Content(frontPin, outline, theme.pinColor, null));
      BowlingPin pin = new BowlingPin(content, positions[i][0], positions[i][1], radius);
      pins.add(pin);
      add(pin);
    }
    return pins;
  }

  /**
   * Builds the side art.
   *
   * @return the side object
   */
  private BowlingSide buildSide()
  {
    return new BowlingSide();
  }

  /**
   * Builds the lane art.
   *
   * @return the lane
   */
  private BowlingLane buildLane()
  {
    return new BowlingLane(theme.laneColor);
  }

  /**
   * Builds the background.
   *
   * @return the background
   */
  private Background buildBackground()
  {
    return new Background(theme.backgroundColor);
  }

  /**
   * Schedules a pin reset.
   */
  public void schedulePinReset()
  {
    pinResetCounter = PIN_RESET_DELAY_TICKS;
  }

  @Override
  public void handleTick(final int delay)
  {
    super.handleTick(delay);
    if (pinResetCounter >= 0)
    {
      pinResetCounter--;
      if (pinResetCounter <= 0)
      {
        resetPins();
        pinResetCounter = -1;
      }
    }
    if (currentMessage != null)
    {
      currentMessage.tick();
      if (currentMessage.isExpired())
      {
        remove(currentMessage);
        currentMessage = null;
      }
    }
  }

  @Override
  public void startRoll(final double angle)
  {
    ball.startRoll(angle);
  }

  @Override
  public void resetBall()
  {
    ball.resetBall();
  }

  @Override
  public void resetPins()
  {
    for (BowlingPin p : pins)
    {
      p.resetPin();
    }
  }

  /**
   * Shows a temporary overlay message.
   *
   * @param message
   *          the message to show
   */
  public void showMessage(final String message)
  {
    Point2D center = new Point2D.Double(500, 350);
    int durationTicks = 60;
    currentMessage = new BowlingSuperImpositions(message, center, durationTicks);
    add(currentMessage);
  }

  /**
   * Shows the end game popup.
   *
   * @param finalScore
   *          the final game score
   */
  public void showEndGamePopup(final int finalScore)
  {
    if (endGamePopup == null)
    {
      endGamePopup = new EndGamePopup();
      add(endGamePopup);
      this.getView().addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(final MouseEvent e)
        {
          if (endGamePopup != null && endGamePopup.clicked(e.getPoint()))
          {
            stopAllAudio();
            app.launchEndScreen(finalScore);
          }
        }
      });
    }
  }

  /**
   * Popup shown after the game ends.
   */
  private class EndGamePopup implements visual.statik.SimpleContent
  {
    private Rectangle2D box = new Rectangle2D.Double(300, 250, 400, 120);

    @Override
    public void render(final Graphics g)
    {
      Graphics2D g2 = (Graphics2D) g;
      g2.setColor(new Color(0, 0, 0, 150));
      g2.fill(new Rectangle2D.Double(0, 0, 1000, 700));
      g2.setColor(Color.WHITE);
      g2.fill(box);
      g2.setColor(Color.BLACK);
      g2.draw(box);
      g2.setFont(new Font("Arial", Font.BOLD, 36));
      g2.drawString("END GAME", 390, 320);
      g2.setFont(new Font("Arial", Font.PLAIN, 20));
      g2.drawString("Click to continue", 420, 350);
    }

    /**
     * Checks if the popup was clicked.
     *
     * @param p
     *          the click point
     * @return true if clicked
     */
    public boolean clicked(final Point2D p)
    {
      return box.contains(p);
    }
  }

}
