package com.pulapirata.core;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import com.pulapirata.core.sprites.Pingo;

public class Pet implements Game {
  private GroupLayer layer;
  private List<Pingo> pingos = new ArrayList<Pingo>(0);

  @Override
  public void init() {

    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);
    
    // create and add background image layer
    Image bgImage = assets().getImage("pet/images/cenario_quarto.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.add(bgLayer);

    Pingo pingo = new Pingo(layer, graphics().width() / 2, graphics().height() / 2);
    pingos.add(pingo);
  }


  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the sprites group layer automatically
    // so no need to do anything here!
  }

  @Override
  public void update(float delta) {
    for (Pingo pingo : pingos) {
      pingo.update(delta);
    }
  }

  @Override
  public int updateRate() {
    return 100;
  }
}
