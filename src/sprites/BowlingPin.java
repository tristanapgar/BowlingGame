package sprites;

import java.util.*;
import bowling.GameState;
import visual.dynamic.described.*;
import visual.statik.described.*;

/**
 * BowlingPin for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class BowlingPin extends RuleBasedSprite
{
  private boolean hit = false; // touched by ball or other pin
  private boolean falling = false; // actively tipping
  private boolean knocked = false; // finished falling
  private double x, y;
  private double fallDirection = 1; // 1 = right, -1 = left
  private final double originalX, originalY;
  private double tiltBack = 0; // visual backward tilt angle
  private double tiltBackVelocity = 0;
  private double velocityX = 0;
  private double velocityY = 0;
  private double rotation = 0; // current rotation
  private double angularVelocity = 0; // rotation speed
  private final double radius;
  private ArrayList<Sprite> antagonists = new ArrayList<>();
  private GameState gameState;

  /**
   * Constructor for BowlingPin.
   *
   * @param content
   *          for the BowlingPin
   * @param startX
   *          position
   * @param startY
   *          position
   * @param radius
   *          of the bowling pin
   */
  public BowlingPin(final TransformableContent content, final double startX, final double startY,
      final double radius)
  {
    super(content);
    this.x = startX;
    this.y = startY;
    this.originalX = startX;
    this.originalY = startY;
    this.radius = radius;
    setLocation(x, y);
    setVisible(true);
  }

  /**
   * Sets the game state.
   *
   * @param gameState
   *          to be used
   */
  public void setGameState(final GameState gameState)
  {
    this.gameState = gameState;
  }

  /**
   * Response from getting hit by the ball.
   *
   * @param ball
   *          which hit pin
   */
  public void hitByBall(final BowlingBall ball)
  {
    if (hit)
    {
      return;
    }
    hit = true;
    // compute linear velocity away from ball
    double dx = x - ball.getX();
    fallDirection = (dx >= 0) ? 1 : -1; // ball hits left → fall right, ball hits right → fall left
    double dy = y - ball.getY();
    double length = Math.hypot(dx, dy);
    double speed = 3 + Math.random() * 2; // small random for realism
    velocityX = dx / length * speed;
    velocityY = dy / length * speed;
    falling = true; // start tipping over
    angularVelocity = 0.05 + Math.random() * 0.05;
    tiltBackVelocity = 0.05 + Math.random() * 0.03;
    tiltBackVelocity *= fallDirection; // apply left/right
  }

  /**
   * Pins movement from being hit by ball.
   *
   * @param vx
   *          position to move
   * @param vy
   *          position to move
   */
  public void startMoving(final double vx, final double vy)
  {
    if (!hit)
    {
      hit = true;
    }
    velocityX += vx;
    velocityY += vy;
    falling = true;
    if (angularVelocity == 0)
    {
      angularVelocity = 0.03 + Math.random() * 0.02;
    }
  }

  /**
   * Pins intersection with other pins.
   *
   * @param other
   *          pin to check intersection
   * @return true or false if pins intersect
   */
  public boolean intersectsPin(final BowlingPin other)
  {
    double dx = other.x - x;
    double dy = other.y - y;
    return Math.hypot(dx, dy) < (this.radius + other.radius);
  }

  /**
   * Pin logic between ticks.
   *
   * @param time
   *          between ticks
   */
  public void handleTick(final int time)
  {
    if (falling)
    {
      // linear motion
      x += velocityX;
      y += velocityY;
      velocityX *= 0.92; // friction
      velocityY *= 0.92;
      // rotation (tipping)
      rotation += angularVelocity * fallDirection;
      angularVelocity *= 0.95; // rotational friction
      if (Math.abs(angularVelocity) < 0.001)
      {
        angularVelocity = 0;
      }
      // small scale change to exaggerate fall
      tiltBack += tiltBackVelocity;
      tiltBackVelocity *= 0.95;
      if (tiltBackVelocity < 0.001)
      {
        tiltBackVelocity = 0;
      }
      setScale(1.0 + 0.1 * Math.sin(tiltBack));
      // stops when almost stationary
      if (Math.hypot(velocityX, velocityY) < 0.2 && angularVelocity < 0.2 && !knocked)
      {
        knocked = true;
        falling = false;
        setVisible(false);
        if (gameState != null)
        {
          gameState.pinKnocked(this);
        }
      }
      // collisions with other pins
      for (Sprite s : antagonists)
      {
        if (s instanceof BowlingPin)
        {
          BowlingPin other = (BowlingPin) s;
          if (other != this && !other.hit && intersectsPin(other))
          {
            double transfer = 0.7;
            other.startMoving(velocityX * transfer, velocityY * transfer);
          }
        }
      }
    }
    setLocation(x, y);
    setRotation(rotation); // visual tipping
  }

  /**
   * Resets pins for next roll.
   */
  public void resetPin()
  {
    hit = false;
    falling = false;
    knocked = false;
    velocityX = 0;
    velocityY = 0;
    angularVelocity = 0;
    tiltBack = 0; // visual backward tilt angle
    tiltBackVelocity = 0;
    rotation = 0;
    x = originalX;
    y = originalY;
    setLocation(x, y);
    setRotation(0);
    setVisible(true);
  }

  /**
   * add antagonists to current pin.
   *
   * @param s
   *          sprite to add as antagonist
   */
  public void addAntagonist(final Sprite s)
  {
    antagonists.add(s);
  }

  /**
   * Gets the x coordinate.
   *
   * @return the x coordinate
   */
  public double getX()
  {
    return x;
  }

  /**
   * Gets the y coordinate.
   *
   * @return the y coordinate
   */
  public double getY()
  {
    return y;
  }

  /**
   * Gets the radius.
   *
   * @return returns the radius
   */
  public double getRadius()
  {
    return radius;
  }

  /**
   * Whether the pin was knocked over.
   *
   * @return true or false if pin was knocked
   */
  public boolean isKnocked()
  {
    return knocked;
  }

  /**
   * Whether pin was hit or not.
   *
   * @return true or false if pin was hit
   */
  public boolean isHit()
  {
    return hit;
  }

  /**
   * Gets the x velocity of pin.
   *
   * @return the velocity in x direction
   */
  public double getVelocityX()
  {
    return velocityX;
  }

  /**
   * Gets the y velocity of pin.
   *
   * @return the velocity in y direction
   */
  public double getVelocityY()
  {
    return velocityY;
  }

  /**
   * Gets the angular velocity.
   *
   * @return the angular velocity
   */
  public double getAngularVelocity()
  {
    return angularVelocity;
  }

}
