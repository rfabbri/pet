package com.pulapirata.java;

import playn.core.PlayN;
import playn.java.JavaPlatform;

import com.pulapirata.core.Pet;

public class PetJava {

  public static void main(String[] args) {
    // JavaPlatform platform = JavaPlatform.register();

    JavaPlatform.Config conf = new JavaPlatform.Config();
    conf.width = 480;
    conf.height = 800;

    JavaPlatform platform = JavaPlatform.register(conf);

    PlayN.run(new Pet());
  }
}
