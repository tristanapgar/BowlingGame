package bowling;

import java.util.*;
import gui.BowlingScreen;
import sprites.BowlingPin;

/**
 * GameState for Bowling Game.
 *
 * @author Tristan Apgar and Jacob Noel
 * @version Fall 2025
 *
 *          Honor Statement: This code adheres to JMU Policy.
 */
public class GameState implements BowlingSubject
{
  private List<BowlingObserver> observers = new ArrayList<>();
  private BowlingBallController ballController;
  private List<Integer> rollScores = new ArrayList<>();
  private BowlingScreen screen;
  private int set; // 1–10
  private int rollInSet; // 1 or 2
  private String userName = "username";
  private int pinsStanding; // how many pins remain standing
  private int pinsDownThisRoll; // pins knocked only this roll
  private int pinsDownInSet; // total pins knocked in this frame
  private boolean ballIsRolling;
  private boolean waitingForBallToStop;
  private boolean collisionSoundPlayedThisRoll = false;
  private boolean waitingForPlayerAim;
  private Map<Integer, PinData> pins = new HashMap<>(); // Map pinID -> PinData
  private ArrayList<Integer> totalScore = new ArrayList<>();

  /**
   * PinData logic to use.
   */
  public static class PinData
  {
    private BowlingPin pin;
    private boolean knocked;

    /**
     * Constructor for PinData.
     *
     * @param pin
     *          to retrieve data
     */
    public PinData(final BowlingPin pin)
    {
      this.setPin(pin);
      this.knocked = false;
    }

    /**
     * Gets the pin stored.
     *
     * @return the bowling pin
     */
    public BowlingPin getPin()
    {
      return pin;
    }

    /**
     * Sets the pin.
     *
     * @param pin
     *          to be set
     */
    public void setPin(final BowlingPin pin)
    {
      this.pin = pin;
    }
  }

  /**
   * Constructor for GameState.
   */
  public GameState()
  {
    resetGame();
  }

  /**
   * Updates the totalScores for game.
   */
  private void updateTotalScores()
  {
    totalScore.clear();
    int runningTotal = 0;

    for (int setIndex = 0; setIndex < 10; setIndex++)
    { // max 10 sets
      int roll1Index = setIndex * 2;
      int roll2Index = roll1Index + 1;
      if (roll1Index >= rollScores.size())
        break; // no rolls yet
      int roll1 = rollScores.get(roll1Index);
      int roll2 = (roll2Index < rollScores.size()) ? rollScores.get(roll2Index) : 0;
      int setTotal = roll1 + roll2;
      // strike point bonus
      if (roll1 == 10)
      {
        int bonus1 = (roll2Index < rollScores.size()) ? rollScores.get(roll2Index) : 0;
        int bonus2 = (roll2Index + 1 < rollScores.size()) ? rollScores.get(roll2Index + 1) : 0;
        setTotal = 10 + bonus1 + bonus2;
      }
      // spare point bonus
      else if (roll1 + roll2 == 10)
      {
        int bonus = (roll2Index + 1 < rollScores.size()) ? rollScores.get(roll2Index + 1) : 0;
        setTotal = 10 + bonus;
      }
      runningTotal += setTotal;
      totalScore.add(runningTotal);
    }
  }

  /**
   * Sets the BowlingBallController.
   *
   * @param controller
   *          to set
   */
  public void setBallController(final BowlingBallController controller)
  {
    this.ballController = controller;
  }

  /**
   * Starts the aiming process.
   */
  public void startAiming()
  {
    if (ballIsRolling || waitingForBallToStop)
      return;
    waitingForPlayerAim = true;
  }

  /**
   * Gets the pins.
   *
   * @return the collection of pin values
   */
  public Collection<PinData> getPins()
  {
    return pins.values();
  }

  /**
   * Checks if aiming.
   *
   * @return true or false if aiming
   */
  public boolean isAiming()
  {
    return waitingForPlayerAim;
  }

  /**
   * Determines if roll can happen.
   *
   * @param angle
   *          to roll at
   */
  public void playerRollRequested(final double angle)
  {
    if (!waitingForPlayerAim || ballIsRolling || waitingForBallToStop)
      return;

    ballIsRolling = true;
    waitingForBallToStop = true;
    pinsDownThisRoll = 0;

    if (ballController != null)
      ballController.startRoll(angle);
  }

  /**
   * Updates knocked state after collisons.
   *
   * @param pin
   *          to update
   */
  public void pinKnocked(final BowlingPin pin)
  {
    for (PinData pd : pins.values())
    {
      if (pd.getPin() == pin && !pd.knocked)
      {
        pd.knocked = true;
        pinsDownThisRoll++;
        pinsStanding = Math.max(0, pinsStanding - 1);
        if (!collisionSoundPlayedThisRoll && screen != null)
        {
          collisionSoundPlayedThisRoll = true;
          screen.playPinHitSound();
        }

        break;
      }
    }
  }

  /**
   * Gets the total score.
   *
   * @return the total score
   */
  public ArrayList<Integer> getTotalScore()
  {
    return totalScore;
  }

  /**
   * Updates the game when ball is stopped.
   */
  public void ballStopped()
  {
    collisionSoundPlayedThisRoll = false;
    ballIsRolling = false;
    waitingForBallToStop = false;
    pinsDownInSet += pinsDownThisRoll;
    rollScores.add(pinsDownThisRoll);
    updateTotalScores();
    boolean isStrike = (rollInSet == 1 && pinsDownThisRoll == 10);
    boolean isSpare = (rollInSet == 2 && pinsDownInSet == 10);
    boolean isGutter = (pinsDownThisRoll == 0);
    // show message + play audio
    if (ballController instanceof BowlingScreen)
    {
      setBowlingScreen((BowlingScreen) ballController);
      if (isStrike)
      {
        screen.showMessage("Nice Strike!");
        screen.playStrikeSound(); // 🔥 NEW
      }
      else if (isSpare)
      {
        screen.showMessage("Nice Spare!");
        screen.playSpareSound(); // optional, if you add one
      }
      else if (isGutter)
      {
        screen.showMessage("Horrible Roll!");
        screen.playGutterSound();
      }
    }
    if (set == 10)
    {
      // after 2nd roll, stop the game
      if (rollInSet == 2)
      {
        waitingForPlayerAim = false;
        if (screen != null)
        {
          int finalScore = totalScore.isEmpty() ? 0 : totalScore.get(totalScore.size() - 1);
          screen.showEndGamePopup(finalScore);
        }
        notifyObservers();
        return; // stop further rolls
      }
      else
      {
        // first roll only, go to roll 2
        rollInSet = 2;
        waitingForPlayerAim = false;
        if (ballController != null)
          ballController.resetBall();
        notifyObservers();
        return;
      }
    }
    if (isStrike || isSpare || rollInSet == 2)
    {
      endFrameAndResetSet();
    }
    else
    {
      rollInSet = 2;
      waitingForPlayerAim = false;
      if (ballController != null)
        ballController.resetBall();
    }
    notifyObservers();
  }

  /**
   * Checks if any pins have been hit.
   *
   * @return true or false if any pins were hit
   */
  public boolean anyPinsHit()
  {
    for (PinData pd : pins.values())
    {
      if (pd.getPin().isHit())
        return true;
    }
    return false;
  }

  /**
   * ends the set and resets for next set.
   */
  private void endFrameAndResetSet()
  {
    set++;
    rollInSet = 1;
    pinsStanding = 10;
    pinsDownInSet = 0;
    pinsDownThisRoll = 0;
    waitingForPlayerAim = false;
    ballIsRolling = false;
    waitingForBallToStop = false;
    for (PinData pd : pins.values())
    {
      pd.knocked = false; // reset knocked state
      if (pd.getPin() != null)
        pd.getPin().resetPin(); // reset visual state
    }
    if (ballController != null)
    {
      if (ballController instanceof BowlingScreen)
      {
        ((BowlingScreen) ballController).schedulePinReset();
        ballController.resetBall();
      }
    }
    notifyObservers();
  }

  @Override
  public void addObserver(final BowlingObserver observer)
  {
    observers.add(observer);
  }

  @Override
  public void removeObserver(final BowlingObserver observer)
  {
    observers.remove(observer);
  }

  @Override
  public void notifyObservers()
  {
    for (BowlingObserver obs : observers)
      obs.update();
  }

  /**
   * Add pin to the map.
   *
   * @param id
   *          given to a pin
   * @param pin
   *          data to add to map.
   */
  public void addPin(final int id, final BowlingPin pin)
  {
    pins.put(id, new PinData(pin));
  }

  /**
   * Gets the roll scores of each roll.
   *
   * @return the array of rollScores.
   */
  public ArrayList<Integer> getRollScores()
  {
    return new ArrayList<>(rollScores);
  }

  /**
   * Resets the game.
   */
  public void resetGame()
  {
    set = 1;
    rollInSet = 1;
    pinsStanding = 10;
    pinsDownInSet = 0;
    pinsDownThisRoll = 0;
    waitingForPlayerAim = false;
    ballIsRolling = false;
    waitingForBallToStop = false;
    rollScores.clear();
    // reset all pins
    for (PinData pd : pins.values())
    {
      if (pd.getPin() != null)
      {
        pd.getPin().resetPin();
        pd.knocked = false;
      }
    }
    notifyObservers();
  }

  /**
   * Gets the set.
   *
   * @return the current set
   */
  public int getSet()
  {
    return set;
  }

  /**
   * Gets the roll in current set.
   *
   * @return the roll in set
   */
  public int getRollInSet()
  {
    return rollInSet;
  }

  /**
   * Gets the number of pins standing.
   *
   * @return the pins standing
   */
  public int getPinsStanding()
  {
    return pinsStanding;
  }

  /**
   * Gets the pins down in set (total game).
   *
   * @return the pins down in set
   */
  public int getPinsDownInSet()
  {
    return pinsDownInSet;
  }

  /**
   * Gets pins down this roll.
   *
   * @return the pins down this roll
   */
  public int getPinsDownThisRoll()
  {
    return pinsDownThisRoll;
  }

  /**
   * Gets the user name.
   *
   * @return the user name
   */
  public String getUserName()
  {
    return userName;
  }

  /**
   * Sets the bowling screen.
   *
   * @param otherScreen
   *          to be used
   */
  public void setBowlingScreen(final BowlingScreen otherScreen)
  {
    this.screen = otherScreen;
  }

  /**
   * Sets the user name.
   *
   * @param userName
   *          to be set
   */
  public void setUserName(final String userName)
  {
    this.userName = userName;
  }

}
