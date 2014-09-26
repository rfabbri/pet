package com.pulapirata.core.utils;
import java.util.*;
import playn.core.PlayN;

public class PetAttribute {

  public PetAttribute(String name, int startVal, int min, int max, int passive, int passiveBeats) {
    set(name, startVal, min, max, passive, passiveBeats);
  }

  // start with easily identifiable dummy default values
  public PetAttribute(String name) {
    set(name, -6969, -696969, 696969, 6969, 9696);
  }

  public int val() { return val_; }
  public int min() { return min_; }
  public int max() { return max_; }
  public int passive() { return passive_; }
  // the speed in 'beatsCoelhoHora'
  public int passiveBeats() { return passiveBeats_; }
  public String name() { return name_; }

  public void set(int v) {
    val_ = v;
    assertConsistency();
  }

  public void set(String name, int startVal, int min, int max, int passive, int passiveBeats) {
    name_ = name;
    val_ = startVal;
    min_ = min;
    max_ = max;
    passive_ = passive;
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
  public void sumPassive() { sum(passive()); assertConsistency(); }
  public void subPassive() { sub(passive()); assertConsistency(); }
  public void setMin(int v) { min_ = v; assertConsistency(); } // TODO do some checking
  public void setMax(int v) { max_ = v; assertConsistency(); }
  public void setPassive(int p) { passive_ = p; assertConsistency(); }
  public void setPassiveBeats(int b) { passiveBeats_ = (b>1)? b:1; }
  // TODO updatePassivo();

  public void print() {
    System.out.println("name: " + name_ + " val: " + val_
      + " min: " + min_ + " max: " + max_ + " passive: " + passive_
      + " passiveBeats: " + passiveBeats_);
  }

  protected String name_;
  protected int val_;
  protected int min_;
  protected int max_;
  protected int passive_;
  protected int passiveBeats_;
}
