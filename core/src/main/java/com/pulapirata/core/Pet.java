package com.pulapirata.core;

import java.util.ArrayList;
import java.util.List;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.CanvasImage;
import com.pulapirata.core.sprites.Pingo;
import com.pulapirata.core.sprites.PingoMorto;

public class Pet implements Game {
  private GroupLayer layer;
  private List<Pingo> pingos = new ArrayList<Pingo>(0);
  private List<PingoMorto> pingosmortos = new ArrayList<PingoMorto>(0);
  private int BEAT = 0;
  private int BEATS_COELHODIA = 10; // Em 30 coelho-dias, pingo morre

  @Override
  public void init() {


    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);
    
    // create and add the status title layer using drawings for faster loading
    CanvasImage bgtile = graphics().createImage(480, 119);
    bgtile.canvas().setFillColor(0xFFFFFFFF);
    bgtile.canvas().fillRect(0, 0, 480, 119);
    bgtile.canvas().setFillColor(0xFF333366);
    bgtile.canvas().fillRect(4, 4, 472, 112);

    ImageLayer statlayer = graphics().createImageLayer(bgtile);
    statlayer.setWidth(graphics().width());
    statlayer.setHeight(120);
    layer.add(statlayer);

    // create and add background image layer
    Image bgImage = assets().getImage("pet/images/cenario_quarto.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.addAt(bgLayer,0,120);

    // sprites
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
      BEAT = BEAT + 1;
      if( BEAT / BEATS_COELHODIA >=30 ){
          // pingo morre
          BEAT = BEAT; // pass
          //pingos.del(pingo);
        PingoMorto pingomorto = new PingoMorto(layer, graphics().width() / 2, graphics().height() / 2);
        pingosmortos.add(pingomorto);

      }
    }
    for (PingoMorto pingomorto : pingosmortos) {
      pingomorto.update(delta);
    }

  }

  @Override
  public int updateRate() {
    return 100;
  }
}
