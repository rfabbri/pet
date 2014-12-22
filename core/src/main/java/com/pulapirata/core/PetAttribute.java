package com.pulapirata.core;
import java.util.*;

import playn.core.PlayN;

import react.IntValue;

/**
 * A reactive pet attribute. It can be wired to listeners to do all sorts of
 * game logic stuff.
 */
public class PetAttribute extends IntValue {

    public PetAttribute(String name, int startVal, int min, int max, int passiveDay, int passiveNight, double passiveBeats) {
        super(startVal);
        set(name, min, max, passiveDay, passiveNight, passiveBeats);
        assert inv();
    }

    /**
     * start with easily identifiable dummy default values
     */
    public PetAttribute(String name) {
      super(-6969);
      set(name, -696969, 696969, 6969, 6969, 9696);
      assert inv();
    }

    public int val() { return get(); }
    public int min() { return min_; }
    public int max() { return max_; }
    public boolean inRange(int v) { return v >= min() && v <= max(); }
    public int passiveDay() { return passiveDay_; }
    public int passiveNight() { return passiveNight_; }
    // the speed in 'beatsCoelhoHora', not an int as fractional counts matter
    // for time accuracy.
    public double passiveBeats() { return passiveBeats_; }
    public String name() { return name_; }

    public void set(int v) {
        updateInt(v);
        assert inv();
    }

    public void set(String name, int startVal, int min, int max, int passiveDay, int passiveNight, double passiveBeats) {
        name_ = name;
        min_ = min;
        max_ = max;
        passiveDay_ = passiveDay;
	passiveNight_ = passiveNight;
        passiveBeats_ = passiveBeats;
        set(startVal);
    }

    /**
     * Sets everything except start value
     */
    protected void set(String name, int min, int max, int passiveDay, int passiveNight, double passiveBeats) {
        name_ = name;
        min_ = min;
        max_ = max;
        passiveDay_ = passiveDay;
	passiveNight_ = passiveNight;
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
    public void sumPassiveDay() { sum(passiveDay()); }
    public void sumPassiveNight() { sum(passiveNight()); }
    public void subPassiveDay() { sub(passiveDay()); }
    public void subPassiveNight() { sub(passiveNight()); } 
    public void setMin(int v) { min_ = v; assert inv(); }
    public void setMax(int v) { max_ = v; assert inv(); }
    public void setPassiveDay(int p) { passiveDay_ = p; assert inv(); }    
    public void setPassiveNight(int p) { passiveNight_ = p; assert inv(); }
    public void setPassiveBeats(double b) { passiveBeats_ = (b>1)? b:1; }

    /**
     * Updates attribute passively with time
     */
    public void updatePassiveDay(int beat) {
        /*
        if (name().equals("Nutricao"))
        System.out.println(
                "haha-passive: " + passiveDay() + " passiveBeats:  " + passiveBeats() + " beat: " + beat
                + " name: " + name() + " mod: " + (beat % passiveBeats())
                );
        */
        if (passiveDay() != 0.0 &&  (int)(beat % passiveBeats()) == 0) {
            sumPassiveDay();
        }	
    }

    public void updatePassiveNight(int beat) {
        /*
        if (name().equals("Nutricao"))
        System.out.println(
                "haha-passive: " + passiveNight() + " passiveBeats:  " + passiveBeats() + " beat: " + beat
                + " name: " + name() + " mod: " + (beat % passiveBeats())
                );
        */
        if (passiveNight() != 0.0 &&  (int)(beat % passiveBeats()) == 0) {
            sumPassiveNight();
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
    protected int passiveNight_;
    protected int passiveDay_;
    protected double passiveBeats_;
}
