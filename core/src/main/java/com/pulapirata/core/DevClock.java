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

import playn.core.util.Clock;

/**
 * A {@link Clock} implementation that works nicely with {@link com.pulapirata.core.DevGame}.
 * This is a simple change to playn.core.util.Clock to make updateRate non-final
 * and publically settable, so that it can work with dynamic game speeds.
 */
public class DevClock implements Clock {
  private int updateRate;
  private int elapsed;
  private float current, paintTime, paintDelta, alpha;

  public DevClock(int updateRate) {
    this.updateRate = updateRate;
  }

  public void setUpdateRate(int updateRate) {
    this.updateRate = updateRate;
  }

  @Override
  public float time() {
    return current;
  }

  @Override
  public float dt() {
    return paintDelta;
  }

  @Override
  public float alpha() {
    return alpha;
  }

  /** Call this from {@link playn.core.Game.Default#update}. */
  public void update(int delta) {
    elapsed += delta;
    current = elapsed;
  }

  /** Call this from {@link playn.core.Game.Default#paint}. */
  public void paint(float alpha) {
    float newCurrent = elapsed + alpha * updateRate;
    paintDelta = newCurrent - paintTime;
    paintTime = newCurrent;
    current = newCurrent;
    this.alpha = alpha;
  }
}
