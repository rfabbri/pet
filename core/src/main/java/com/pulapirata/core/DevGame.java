/**
 * Pet - a comic pet simulator game
 * Copyright (C) 2013-2015 Ricardo Fabbri and Edson "Presto" Correa
 *
 * This program is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version. A different license may be requested
 * to the copyright holders.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>
 *
 * NOTE: this file may contain code derived from the PlayN, Tripleplay, and
 * React libraries, all (C) The PlayN Authors or Three Rings Design, Inc.  The
 * original PlayN is released under the Apache, Version 2.0 License, and
 * TriplePlay and React have their licenses listed in COPYING-OOO. PlayN and
 * Three Rings are NOT responsible for the changes found herein, but are
 * thankfully acknowledged.
 */
package com.pulapirata.core;

import playn.core.PlayN;
import playn.core.Game;


/**
 * An implementation of {@link Game} similar to Game.Default with a few
 * development features.
 * This class allows features that are useful for development, such as
 * the ability to dynamically change game speed, by making updateRate non-final and
 * by providing setters and getters to it. It is useful for development, hence
 * the name.
 */
abstract class DevGame implements Game {

  /**
   * Creates an instance of the default game implementation.
   *
   * @param updateRate  the desired update rate of the main game loop, in ms.
   */
  public DevGame (int updateRate) {
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
