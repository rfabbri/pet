package com.pulapirata.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.pulapirata.core.Pet;

public class PetJava {

  public static void main(String[] args) {
    JavaPlatform platform = JavaPlatform.register();
    platform.assets().setPathPrefix("com/pulapirata/resources");
    PlayN.run(new Pet());
  }
}
