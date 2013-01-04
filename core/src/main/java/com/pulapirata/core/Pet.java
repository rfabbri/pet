package com.pulapirata.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;

import com.pulapirata.core.sprites;

public class Pet implements Game {
  private GroupLayer layer;
  private pingo = new Pingo();  

  @Override
  public void init() {

    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);
    
    // create and add background image layer
    Image bgImage = assets().getImage("pet/images/cenario_quarto.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.add(bgLayer);

    pingo = (graphics().width() / 2, graphics().height() / 2);
  }


  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the sprites group layer automatically
    // so no need to do anything here!
  }

  @Override
  public void update(float delta) {
    pingo.update(delta);
  }

  @Override
  public int updateRate() {
    return 25;
  }
}
