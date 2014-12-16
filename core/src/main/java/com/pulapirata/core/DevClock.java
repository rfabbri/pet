package com.pulapirata.core;

import playn.core.util.Clock;

/**
 * A {@link Clock} implementation that works nicely with {@link com.pulapirata.core.DevGame}.
 * This is a simple change to playn.core.util.Clock to make updateRate non-final
 * and publically settable, so that it can work with dynamic game speeds.
 */
public static class DevClock implements Clock {
  private int updateRate;
  private int elapsed;
  private float current, paintTime, paintDelta, alpha;

  public Source(int updateRate) {
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
