package com.pulapirata.core;
import java.util.*;
import playn.core.PlayN;

public class PetAttribute {
  protected String name_;
  protected int val_;
  protected int min_;
  protected int max_;
  protected int passive_; 
  protected int passiveDay_;
  protected int passiveNight_;
  protected int passiveBeats_;

  public PetAttribute(String name, int startVal, int min, int max, int passiveDay, int passiveNight, int passiveBeats) {
    set(name, startVal, min, max, passiveDay, passiveNight, passiveBeats);
  }

  // start with easily identifiable dummy default values
  public PetAttribute(String name) {
    set(name, -6969, -696969, 696969, 6969, 6969, 9696);
  }

  public int val() { return val_; }
  public int min() { return min_; }
  public int max() { return max_; }
  public int passive() { return passive_; }
  public int passiveDay() { return passiveDay_; }
  public int passiveNight() { return passiveNight_; }
  // the speed in 'beatsCoelhoHora'
  public int passiveBeats() { return passiveBeats_; }
  public String name() { return name_; }

  public void set(int v) {
    val_ = v;
    assertConsistency();
  }

  public void set(String name, int startVal, int min, int max, int passiveDay, int passiveNight, int passiveBeats) {
    name_ = name;
    val_ = startVal;
    min_ = min;
    max_ = max;
    passiveDay_ = passiveDay;
    passiveNight_ = passiveNight;
    passiveBeats_ = passiveBeats;
    assertConsistency();
  }

  // TODO: substitute by assertion
  void assertConsistency() {
    if (min_ > val_ || val_ > max_) {
      System.out.println("Inconsistency in attribute " + name_);
      PlayN.log().error("Inconsistency in attribute " + name_);
    }
  }

  public void sum(int v) { val_ += v; assertConsistency(); }
  public void sub(int v) { val_ -= v; assertConsistency(); }
  public void sumPassiveDay() { sum(passiveDay()); assertConsistency(); }
  public void subPassiveDay() { sub(passiveDay()); assertConsistency(); }
  public void sumPassiveNight() { sum(passiveNight()); assertConsistency(); }
  public void subPassiveNight() { sub(passiveNight()); assertConsistency(); }
  public void setMin(int v) { min_ = v; assertConsistency(); } // TODO do some checking
  public void setMax(int v) { max_ = v; assertConsistency(); }
//  public void setPassive(int p) { passive_ = p; assertConsistency(); }
  public void setPassiveBeats(int b) { passiveBeats_ = (b>1)? b:1; }
  public void setPassiveDay(int p) { passiveDay_ = p; }
  public void setPassiveNight(int p) { passiveNight_ = p; }
  // TODO updatePassivo();

  public void print() {
    System.out.println("name: " + name_ + " val: " + val_
      + " min: " + min_ + " max: " + max_ + " passiveDay: " + passiveDay_
      + " passiveNight: " + passiveNight_ + " passiveBeats: " + passiveBeats_);
  }
}
