package com.pulapirata.core;
import java.util.*;

import playn.core.PlayN;

import react.IntValue;

/**
 * A reactive pet attribute. It can be wired to listeners to do all sorts of
 * game logic stuff.
 */
public class PetAttribute extends IntValue {

    public PetAttribute(String name, int startVal, int min, int max, int passive, int passiveBeats) {
        super(startVal);
        set(name, startVal, min, max, passive, passiveBeats);
        assert inv();
    }

    /**
     * start with easily identifiable dummy default values
     */
    public PetAttribute(String name) {
      set(name, -6969, -696969, 696969, 6969, 9696);
    }

    public int val() { return get(); }
    public int min() { return min_; }
    public int max() { return max_; }
    public int passive() { return passive_; }
    // the speed in 'beatsCoelhoHora'
    public int passiveBeats() { return passiveBeats_; }
    public String name() { return name_; }

    public void set(int v) {
        updateInt(v);
        assert inv();
    }

    public void set(String name, int startVal, int min, int max, int passive, int passiveBeats) {
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
    protected void set(String name, int min, int max, int passive, int passiveBeats) {
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
      if (min_ > val_ || val_ > max_) {
          System.out.println("Inconsistency in attribute " + name_);
          PlayN.log().error("Inconsistency in attribute " + name_);
      }
      return (min_ <= val() && val_ <= max_);
    }

    public void sum(int v) { incrementClamp(v, min_, max_); }
    public void sub(int v) { sum(-v); }
    public void sumPassive() { sum(passive()); }
    public void subPassive() { sub(passive()); }
    public void setMin(int v) { min_ = v; assert inv(); }
    public void setMax(int v) { max_ = v; assert inv(); }
    public void setPassive(int p) { passive_ = p; assert inv(); }
    public void setPassiveBeats(int b) { passiveBeats_ = (b>1)? b:1; }
    // TODO updatePassivo();

    public void print() {
        System.out.println("name: " + name_ + " val: " + val_
          + " min: " + min_ + " max: " + max_ + " passive: " + passive_
          + " passiveBeats: " + passiveBeats_);
    }

    protected String name_;
    protected int min_;
    protected int max_;
    protected int passive_;
    protected int passiveBeats_;
}
