package bowling;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import visual.statik.SimpleContent;

/**
 * ScoreBoard for Bowling Game.
 *
 * @author Jacob Noel and Tristan Apgar
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class ScoreBoard implements SimpleContent, BowlingObserver
{
  protected Color color;
  protected Image image;
  protected Point2D location;
  private GameState gameState;

  /**
   * Constructor for ScoreBoard.
   *
   * @param gameState
   *          to use
   * @param image
   *          to use
   * @param color
   *          to use
   * @param location
   *          to use
   * @throws IOException
   *           if image invalid
   */
  public ScoreBoard(final GameState gameState, final Image image, final Color color,
      final Point2D location) throws IOException
  {
    this.color = color;
    this.location = location;
    this.image = image;
    this.gameState = gameState;
    gameState.addObserver(this);
  }

  @Override
  public void reset()
  {
  }

  @Override
  public void update()
  {
  }

  @Override
  public void render(final Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;
    // draw background
    g2.setColor(color);
    g2.drawImage(image, 0, 0, 1000, 100, null);
    // draw current score
    g2.setColor(Color.white);
    ScoreboardWriter.renderScore(location, gameState.getUserName(), gameState.getRollScores(),
        gameState.getTotalScore(), g2);
  }

}
