package com.pulapirata.android;

import playn.android.GameActivity;
import playn.core.PlayN;

import com.pulapirata.core.Pet;

public class PetActivity extends GameActivity {

  @Override
  public void main(){
    PlayN.run(new Pet());
  }
}
