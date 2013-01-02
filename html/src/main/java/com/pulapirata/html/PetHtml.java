package com.pulapirata.html;

import playn.core.PlayN;
import playn.html.HtmlGame;
import playn.html.HtmlPlatform;

import com.pulapirata.core.Pet;

public class PetHtml extends HtmlGame {

  @Override
  public void start() {
    HtmlPlatform platform = HtmlPlatform.register();
    platform.assets().setPathPrefix("pet/");
    PlayN.run(new Pet());
  }
}
