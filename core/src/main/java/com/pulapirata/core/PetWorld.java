package com.pulapirata.core;

import tripleplay.entity.Component;
import tripleplay.entity.Entity;
import tripleplay.entity.System;
import tripleplay.entity.World;


class PetWorld extends World {
  /*-------------------------------------------------------------------------------*/
  /** Misc variables */

  /*-------------------------------------------------------------------------------*/
  /** Components.
   * Components are bags of types, positions, and other properties shared among
   * playable entities in Pet (like the bunny itself and its droppings)
   */
  public final Component.IMask type = new Component.IMask(this);
  public final Component.XY pos = new Component.XY(this);
  public final Component.IScalar expires = new Component.IScalar(this);
  private final Randoms _rando = Randoms.with(new Random());

  /*-------------------------------------------------------------------------------*/
  /** Time data */
  private int beat_ = 0; // total number of updates so far

  @Override public void update (int delta) {
      beat_ += delta;
      super.update(delta);
  }



}
