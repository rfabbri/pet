package com.pulapirata.core;

import playn.core.Game;


/**
 * An implementation of {@link Game} similar to Game.Default with a few
 * development features.
 * This class allows features that are useful for development, such as
 * the ability to dynamically change game speed, by making updateRate non-final and
 * by providing setters and getters to it. It is useful for development, hence
 * the name.
 */
static abstract class DevGame implements Game {

  /**
   * Creates an instance of the default game implementation.
   *
   * @param updateRate  the desired update rate of the main game loop, in ms.
   */
  public Default (int updateRate) {
    assert updateRate > 0 : "updateRate must be greater than zero.";
    this.updateRate = updateRate;
  }

  public void setUpdateRate(int updateRate) {
      this.updateRate = updateRate;
  }

  public int updateRate() { return this.updateRate; }


  /**
   * Called when at least {@link #updateRate} ms have elapsed since the last update call.
   * Input-handling, physics, and game logic should be performed in this method. It is also
   * appropriate to update the structure of the scene graph here (adding, removing layers). You
   * should not animate scene graph properties in update; that should happen in {@link #paint}.
   *
   * @param delta time (in ms) since the last update call. This will always be {@link
   * #updateRate}, unless the game is running slowly. In that case, multiple update calls will be
   * coalesced into a single update call and {@code delta} will be a multiple of {@link
   * #updateRate}. Your game should decide at that point whether to run one simulation update
   * with a larger time interval, or to run multiple simulation updates with the standard
   * interval. Beware that if your update calculations take longer than {@link #updateRate} to
   * complete then your game loop will fall further and further behind and will eventually fail.
   * Choose a long enough update interval to accommodate your processing.
   */
  public void update(int delta) {
  }

  /**
   * Called every time the backend refreshes the display (usually 60 times per second). Any
   * manual painting (e.g. {@link Surface#drawImage}) should be performed in this method.
   * Animating of scene graph elements (smoothly updating an element's transform or alpha, for
   * example) should also take place here. You should not run game logic in this method.
   *
   * @param alpha a value representing the fraction of time between the last call to {@link
   * #update} and the next scheduled call. For example if the previous update was scheduled to
   * happen at T=500ms and the next update at T=530ms (i.e. {@link #updateRate} returns 30) and
   * the actual time at which we are being rendered is T=517ms then alpha will be
   * (517-500)/(530-500) or 17/30. This is usually between 0 and 1, but if your game is running
   * slowly, it can exceed 1. For example, if an update is scheduled to happen at T=500ms and the
   * update actually happens at T=517ms, and the update call itself takes 20ms, the alpha value
   * passed to paint will be (537-500)/(530-500) or 37/30.
   */
  public void paint(float alpha) {
  }

  @Override
  public void tick(int elapsed) {
    // micro-optimization to avoid repeated field reads
    int nextUpdate = this.nextUpdate, updateRate = this.updateRate;
    int updates = 0;
    while (elapsed >= nextUpdate) {
      nextUpdate += updateRate;
      updates++;
    }
    if (updates > 0) {
      update(updates*updateRate);
      // calling update() may have taken > 1ms, so we need to re-read the clock to accurately
      // report the fraction of time that has elapsed between updates
      elapsed = PlayN.tick();
    }
    float alpha = 1 - (nextUpdate - elapsed) / (float)updateRate;
    paint(alpha);
    this.nextUpdate = nextUpdate;
  }

  private int updateRate;
  private int nextUpdate;
}
