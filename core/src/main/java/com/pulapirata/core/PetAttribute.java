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
import java.util.*;

import playn.core.PlayN;

import react.IntValue;

/**
 * A reactive pet attribute. It can be wired to listeners to do all sorts of
 * game logic stuff.
 */
public class PetAttribute extends IntValue {

    public PetAttribute(String name, int startVal, int min, int max, int passive, double passiveBeats) {
        super(startVal);
        set(name, min, max, passive, passiveBeats);
        assert inv();
    }

    /**
     * start with easily identifiable dummy default values
     */
    public PetAttribute(String name) {
      super(-6969);
      set(name, -696969, 696969, 6969, 9696);
      assert inv();
    }

    public int val() { return get(); }
    public int min() { return min_; }
    public int max() { return max_; }
    public boolean inRange(int v) { return v >= min() && v <= max(); }
    public int passive() { return passive_; }
    // the speed in 'beatsCoelhoHora', not an int as fractional counts matter
    // for time accuracy.
    public double passiveBeats() { return passiveBeats_; }
    public String name() { return name_; }

    public void set(int v) {
        updateInt(v);
        assert inv();
    }

    public void set(String name, int startVal, int min, int max, int passive, double passiveBeats) {
        name_ = name;
        min_ = min;
        max_ = max;
        passive_ = passive;
        passiveBeats_ = passiveBeats;
        set(startVal);
    }

    /**
     * Sets everything except start value
     */
    protected void set(String name, int min, int max, int passive, double passiveBeats) {
        name_ = name;
        min_ = min;
        max_ = max;
        passive_ = passive;
        passiveBeats_ = passiveBeats;
    }

    /**
     * Class invariant that min, max, val, etc are consistent.
     */
    boolean inv() {
      if (min_ > val() || val() > max_) {
          System.out.println("Inconsistency in attribute " + name_);
          PlayN.log().error("Inconsistency in attribute " + name_);
      }
      return (min_ <= val() && val() <= max_);
    }

    public void sum(int v) { incrementClamp(v, min_, max_); }
    public void sub(int v) { sum(-v);  }
    public void sumPassive() { sum(passive()); }
    public void subPassive() { sub(passive()); }
    public void setMin(int v) { min_ = v; assert inv(); }
    public void setMax(int v) { max_ = v; assert inv(); }
    public void setPassive(int p) { passive_ = p; assert inv(); }
    public void setPassiveBeats(double b) { passiveBeats_ = (b>1)? b:1; }

    /**
     * Updates attribute passively with time
     */
    public void updatePassive(int beat) {
        /*
        if (name().equals("Nutricao"))
        System.out.println(
                "haha-passive: " + passive() + " passiveBeats:  " + passiveBeats() + " beat: " + beat
                + " name: " + name() + " mod: " + (beat % passiveBeats())
                );
        */
        if (passive() != 0.0  &&  (int)(beat % passiveBeats()) == 0) {
            sumPassive();
        }
    }

    public void print() {
        System.out.println("[attrib] " +  name_ + ": " + val());
                /*
          + "\t\t\t\t\tmin: " + min_ + " max: " + max_ + " passive: " + passive_
          + " passiveBeats: " + passiveBeats_);*/
    }

    protected String name_;
    protected int min_;
    protected int max_;
    protected int passive_;
    protected double passiveBeats_;
}
