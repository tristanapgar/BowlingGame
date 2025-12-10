package sprites;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import bowling.GameState;
import event.Metronome;
import java.util.ArrayList;
import visual.dynamic.described.*;
import visual.statik.described.*;

/**
 * BowlingBall for the Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingBall extends RuleBasedSprite implements KeyListener
{
  private double rollingAngle; // in radians
  private double aimOffset; // horizontal offset while aiming
  private boolean rolling;
  private Metronome metronome;
  private boolean showArrow;
  private GameState gameState;
  private boolean waitingForPins = false; // ball is moving, independent of pins
  private boolean inGutter = false;
  private boolean gutterLeft = false;
  private double gutterSpeedY = 6.0;
  private boolean gutterFinished = false;
  private double x;
  private double y;
  protected ArrayList<Integer> keyTimes;
  protected ArrayList<Point2D> locations;
  protected ArrayList<Double> rotations;
  protected ArrayList<Double> scalings;
  public BallReflection reflectionSprite;

  /**
   * BowlingBall Constructor.
   *
   * @param content
   *          to be used
   * @param speed
   *          of the ball
   * @param reflection
   *          of the ball
   */
  public BowlingBall(final TransformableContent content, final Double speed,
      final TransformableContent reflection)
  {
    super(content);
    this.gameState = null;
    this.rollingAngle = 0.0;
    this.aimOffset = 0;
    this.keyTimes = new ArrayList<Integer>();
    this.locations = new ArrayList<Point2D>();
    this.rotations = new ArrayList<Double>();
    this.scalings = new ArrayList<Double>();
    this.x = 495;
    this.y = 650;
    this.reflectionSprite = new BallReflection(reflection);
    setLocation(x, y);
  }

  /**
   * Sets the metronome.
   *
   * @param m
   *          the Metronome to use
   */
  public void setMetronome(final Metronome m)
  {
    this.metronome = m;
  }

  /**
   * Sets the GameState.
   *
   * @param gameState
   *          to use.
   */
  public void setGameState(final GameState gameState)
  {
    this.gameState = gameState;
  }

  /**
   * Handles the tick updates from the metronome.
   * 
   * @param time
   *          between ticks.
   */
  public void handleTick(final int time)
  {
    if (reflectionSprite != null)
    {
      reflectionSprite.setOrigin(x, y); // offset to look like a reflection
      reflectionSprite.handleTick(time);
    }
    if (inGutter)
    {
      updateGutterMovement();
      return;
    }
    if (rolling && !keyTimes.isEmpty())
    {
      int i = 0;
      while (i < keyTimes.size() - 1 && time > keyTimes.get(i + 1))
      {
        i++;
      }
      if (i < keyTimes.size() - 1)
      {
        int t0 = keyTimes.get(i);
        int t1 = keyTimes.get(i + 1);
        Point2D p0 = locations.get(i);
        Point2D p1 = locations.get(i + 1);
        double r0 = rotations.get(i);
        double r1 = rotations.get(i + 1);
        double s0 = scalings.get(i);
        double s1 = scalings.get(i + 1);
        double t = (time - t0) / (double) (t1 - t0);
        if (p0 != null && p1 != null)
        {
          x = lerp(p0.getX(), p1.getX(), t);
          y = lerp(p0.getY(), p1.getY(), t);
        }
        double rotation = lerp(r0, r1, t);
        setRotation(rotation);
        double scale = lerp(s0, s1, t);
        setScale(scale);
      }
      setLocation(x, y);
      if (rolling && checkGutterCollision())
      {
        inGutter = true;
        gutterFinished = false;
        rolling = false;
        updateGutterMovement();
        return;
      }
      if (time >= keyTimes.get(keyTimes.size() - 1))
      {
        rolling = false;
      }
    }
    for (Sprite s : antagonists)
    {
      if (s instanceof BowlingPin)
      {
        BowlingPin pin = (BowlingPin) s;
        if (intersects(pin) && !pin.isKnocked())
        {
          pin.hitByBall(this);
        }
      }
    }
    if (!rolling && waitingForPins && gameState != null)
    {
      boolean allPinsStopped = true;
      for (GameState.PinData pd : gameState.getPins())
      {
        BowlingPin pin = pd.getPin();
        if (pin.isHit() && !pin.isKnocked() && (Math.abs(pin.getVelocityX()) > 0.1
            || Math.abs(pin.getVelocityY()) > 0.1 || Math.abs(pin.getAngularVelocity()) > 0.001))
        {
          allPinsStopped = false;
          break;
        }
      }
      if (allPinsStopped)
      {
        waitingForPins = false;
        gameState.ballStopped(); // now scores update correctly
      }
    }
  }

  /**
   * Updates the Ball after hitting Gutter.
   */
  private void updateGutterMovement()
  {
    // slide upward
    y -= gutterSpeedY;
    if (y < 220)
    {
      y = 220; // clamp to top of lane
    }
    // compute lane geometry (same as BowlingGutter)
    int screenW = 1000;
    int screenH = 900;
    int backWallY = 100;
    int laneTopWidth = 180;
    int laneBottomWidth = 1100;
    int wallBottomY = backWallY + 120;
    int laneTopY = wallBottomY;
    int laneBottomY = screenH;
    double t = (y - laneBottomY) / (laneTopY - laneBottomY);
    t = Math.max(0, Math.min(1, t));
    double laneTopLeftX = (screenW - laneTopWidth) / 2.0;
    double laneTopRightX = laneTopLeftX + laneTopWidth;
    double laneBottomLeftX = (screenW - laneBottomWidth) / 2.0;
    double laneBottomRightX = laneBottomLeftX + laneBottomWidth;
    double targetX;
    if (gutterLeft)
    {
      targetX = laneBottomLeftX + t * (laneTopLeftX - laneBottomLeftX);
      x = targetX - 20;
    }
    else
    {
      targetX = laneBottomRightX + t * (laneTopRightX - laneBottomRightX);
      x = targetX + 20;
    }
    setLocation(x, y);
    if (inGutter)
    {
      double scale = 1.0 - 0.7 * t;
      setScale(scale);
    }
    else
    {
      double scale = 1.0 - 0.6 * t;
      setScale(scale);
    }
    if (y <= laneTopY && !gutterFinished)
    {
      gutterFinished = true;
      inGutter = false;
      if (gameState != null)
      {
        if (!gameState.anyPinsHit())
        {
          gameState.ballStopped();
        }
        else
        {
          waitingForPins = true;
        }
      }
    }
  }

  /**
   * Checks the collision of the ball and gutter.
   *
   * @return true or false if ball hits gutter
   */
  private boolean checkGutterCollision()
  {
    double yPos = this.y;
    // lane geometry (MATCHES BowlingGutter)
    int screenW = 1000;
    int screenH = 900;
    int backWallY = 100;
    int laneTopWidth = 180;
    int laneBottomWidth = 1100;
    int wallBottomY = backWallY + 120;
    // interpolation factor
    double t = (yPos - wallBottomY) / (screenH - wallBottomY);
    t = Math.max(0, Math.min(1, t));
    double halfTop = laneTopWidth / 2.0;
    double halfBottom = laneBottomWidth / 2.0;
    double halfLane = halfTop + (halfBottom - halfTop) * t;
    double laneCenter = screenW / 2.0;
    // Left gutter
    if (x < laneCenter - halfLane)
    {
      gutterLeft = true;
      return true;
    }
    // Right gutter
    if (x > laneCenter + halfLane)
    {
      gutterLeft = false;
      return true;
    }
    return false;
  }

  /**
   * Gets the x position.
   *
   * @return the x coordinate
   */
  public double getX()
  {
    return x;
  }

  /**
   * Gets the y position.
   *
   * @return the y coordinate
   */
  public double getY()
  {
    return y;
  }

  @Override
  public void render(final Graphics g)
  {
    super.render(g);
    if (showArrow)
    {
      int arrowX = (int) x + 5; // x coord
      int arrowY = (int) y - 50; // distance from ball
      int arrowLength = 50; // arrow line length
      int tipx = arrowX + (int) (arrowLength * Math.sin(rollingAngle));
      int tipY = arrowY - (int) (arrowLength * Math.cos(rollingAngle));
      g.setColor(Color.BLACK);
      g.drawLine(arrowX, arrowY, tipx, tipY); // draw line
      // plot and draw triangular arrowhead
      Polygon arrowHead = new Polygon();
      g.setColor(Color.RED);
      double angle = Math.atan2(tipY - arrowY, tipx - arrowX);
      int headSize = 10;
      arrowHead.addPoint(tipx, tipY);
      arrowHead.addPoint(tipx - (int) (headSize * Math.cos(angle + Math.PI / 6)),
          tipY - (int) (headSize * Math.sin(angle + Math.PI / 6)));
      arrowHead.addPoint(tipx - (int) (headSize * Math.cos(angle - Math.PI / 6)),
          tipY - (int) (headSize * Math.sin(angle - Math.PI / 6)));
      g.fillPolygon(arrowHead);
      TransformableContent arrowContent = new Content(arrowHead, Color.BLACK, Color.BLACK, null);
      arrowContent.render(g);
    }
  }

  /**
   * The linear Interpolation to use.
   *
   * @param a
   *          starting value
   * @param b
   *          ending value
   * @param t
   *          interpolation factor
   * @return interpolated value between value a and b
   */
  private double lerp(final double a, final double b, final double t)
  {
    return a + (b - a) * t;
  }

  @Override
  public void keyPressed(final KeyEvent e)
  {
    int code = e.getKeyCode();
    if (code == KeyEvent.VK_LEFT)
    {
      if (showArrow)
      {
        aimOffset -= 10;
        updateArrowAngle();
      }
      else
      {
        double newX = x - 10;
        if (!wouldBeInGutter(newX, y))
        {
          x = newX;
        }
      }
    }
    else if (code == KeyEvent.VK_RIGHT)
    {
      if (showArrow)
      {
        aimOffset += 10;
        updateArrowAngle();
      }
      else
      {
        double newX = x + 10;

        if (!wouldBeInGutter(newX, y))
        {
          x = newX;
        }
      }
    }
    else if (code == KeyEvent.VK_SPACE)
    {
      if (gameState != null)
      {
        if (!gameState.isAiming())
        {
          gameState.startAiming();
          showArrow = true;
        }
        else
        {
          gameState.playerRollRequested(rollingAngle);
          showArrow = false;
        }
      }
    }
    setLocation(x, y);
  }

  /**
   * Updates the arrow angle for rolling.
   */
  private void updateArrowAngle()
  {
    rollingAngle = Math.atan2(aimOffset, 420);
  }

  /**
   * Starts the rolling process.
   *
   * @param angle
   *          of the ball for interpolation.
   */
  public void startRoll(final double angle)
  {
    this.rollingAngle = angle;
    rolling = true; // animation starts
    waitingForPins = true;
    initiateRoll();
  }

  /**
   * 
   * @param futureX
   * @param yPos
   * @return
   */
  private boolean wouldBeInGutter(final double futureX, final double yPos)
  {
    int screenW = 1000;
    int screenH = 900;
    int backWallY = 100;
    int laneTopWidth = 180;
    int laneBottomWidth = 1100;
    int wallBottomY = backWallY + 120;
    double t = (yPos - wallBottomY) / (screenH - wallBottomY);
    t = Math.max(0, Math.min(1, t));
    double halfTop = laneTopWidth / 2.0;
    double halfBottom = laneBottomWidth / 2.0;
    double halfLane = halfTop + (halfBottom - halfTop) * t;
    double laneCenter = screenW / 2.0;
    double radius = 35;
    if (futureX - radius < laneCenter - halfLane)
    {
      return true;
    }
    if (futureX + radius > laneCenter + halfLane)
    {
      return true;
    }
    return false;
  }

  /**
   * Resets the ball for next roll.
   */
  public void resetBall()
  {
    this.x = 495;
    this.y = 650;
    setLocation(x, y);
    setRotation(0);
    setScale(1.0);
    showArrow = false;
    aimOffset = 0;
    rollingAngle = 0;
    setVisible(true);
    keyTimes.clear();
    locations.clear();
    rotations.clear();
    scalings.clear();
    rolling = false;
    waitingForPins = false;
  }

  /**
   * Add key times for future interpolation.
   *
   * @param keyTime
   *          time for key
   * @param location
   *          of the key time
   * @param rotation
   *          of the shape at time
   * @param scaling
   *          of the shape at time
   * @return the index at which the key time was inserted
   */
  public int addKeyTime(final int keyTime, final Point2D location, final Double rotation,
      final Double scaling)
  {
    int existingKT = -1;
    int i = 0;
    boolean keepLooking = true;
    while ((i < keyTimes.size()) && keepLooking)
    {
      existingKT = keyTimes.get(i);
      if (existingKT >= keyTime)
      {
        keepLooking = false;
      }
      else
      {
        i++;
      }
    }
    if ((existingKT == i) && !keepLooking)
    {
      i = -1;
    }
    else
    {
      keyTimes.add(i, keyTime);
      locations.add(i, location);
      rotations.add(i, rotation);
      scalings.add(i, scaling);
    }
    return i;
  }

  /**
   * Initiate the actual roll of the ball.
   */
  private void initiateRoll()
  {
    if (metronome != null)
    {
      metronome.reset();
      metronome.start();
    }
    rolling = true;
    keyTimes.clear();
    locations.clear();
    rotations.clear();
    scalings.clear();
    showArrow = false;
    double totalDistanceY = 460;
    int endTick = 1500;
    double totalDistanceX = Math.tan(rollingAngle) * totalDistanceY;
    addKeyTime(0, new Point2D.Double(x, y), 0.0, 1.0);
    addKeyTime(endTick, new Point2D.Double(x + totalDistanceX, y - totalDistanceY), 0.0, .4);
  }

  /**
   * Intersection of the bounding bodies of the ball and other shapes.
   *
   * @param Sprite
   *          to check for intersection
   * @return true or false if ball intersects another shape
   */
  public boolean intersects(final Sprite s)
  {
    boolean retval;
    Rectangle2D r;
    retval = true;
    r = getBounds2D(true);
    double minx = r.getX();
    double miny = r.getY();
    double maxx = minx + r.getWidth();
    double maxy = miny + r.getHeight();
    r = s.getBounds2D(true);
    double minxO = r.getX();
    double minyO = r.getY();
    double maxxO = minxO + r.getWidth();
    double maxyO = minyO + r.getHeight();
    if ((maxx < minxO) || (minx > maxxO) || (maxy < minyO) || (miny > maxyO))
    {
      retval = false;
    }
    return retval;
  }

  @Override
  public void keyTyped(final KeyEvent e)
  {
  }

  @Override
  public void keyReleased(final KeyEvent e)
  {
  }

}
