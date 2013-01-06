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
// TODO: we need a generic sprite class; or the layer could automatically update
// them

import react.UnitSlot;

import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Group;
import tripleplay.ui.Interface;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.AbsoluteLayout;


public class Pet implements Game {
  private GroupLayer layer;
  private List<Pingo> pingos = new ArrayList<Pingo>(0);
  private List<PingoMorto> pingosmortos = new ArrayList<PingoMorto>(0);
  private int beat = 0; // number of updates
  private int beats_coelhodia = 10; // Em 30 coelho-dias, pingo morre

  // FIXME graphics.width() is weird in html, not respecting #playn-root
  // properties. 
  public int width() { return 480; }
  public int height() { return 800; }

  
  private Interface iface;


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
    //
    //  statlayer.setWidth(graphics().width());
    // FIXME: problem with graphics.width not being set correctly in html;
    // it always seems to give 640
    //  
    statlayer.setHeight(120);
    layer.add(statlayer);

    // create and add background image layer
    Image bgImage = assets().getImage("pet/images/cenario_quarto.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.addAt(bgLayer, 0, 120);

    // sprites
    Pingo pingo = new Pingo(layer, width() / 2, height() / 2);
    pingos.add(pingo);

    // ------------------------------------------------------------------
    // main buttons
    // TODO
    //   - try the Selector class from tripleplay
    //   - try the click() trigger for the button
    //   - use a sprite implementing the Clicable class then insert it like I
    //   did the button below.
    
    // create our UI manager and configure it to process pointer events
    iface = new Interface();

//    Root root = iface.createRoot(AbsoluteLayout.at(0,564,width(),236), SimpleStyles.newSheet());
    Stylesheet petSheet = SimpleStyles.newSheet();
    petSheet.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root root = iface.createRoot(new AbsoluteLayout(), petSheet);
    root.setSize(width(), 354); // this includes the secondary buttons
//    root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));

    layer.addAt(root.layer, 0, 442);


//    Group iface = Group(new TableLayout(4).gaps(0, 0)).add(
//      label("", Background.image(testBg)),
//    );
//

    System.out.println("here ------ !");

    // TODO: provavelmente vamos precisar extender a classe Sprite e colocar
    // nela parte do codigo de Button, pois o Button nao permite mudar o BG.
    // 
    // Vou implementar um exemplo de ambas (Sprite, Button, Sprite+Button). 
    //
    // Tambem temos que olhar mais exemplos de codigo pra encontrar algo similar
    //
    Image but0bg = assets().getImage("pet/main-buttons/01_comida_principal.png");
    Button but0 = new Button (but0bg);

    Group buttons = new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.blank())
        );
    buttons.add(AbsoluteLayout.at(but0, 0, 0, 120, 120));
    root.add(AbsoluteLayout.at(buttons, 0, 118, width(), 236));
//    root.add(AbsoluteLayout.at(buttons, 0, 564, width(), 236));

    but0.clicked().connect(new UnitSlot() {
        @Override
        public void onEmit() {
          System.out.println("but0 clicked!");
        }
    });
  }


  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the sprites group layer automatically
    // so no need to do anything here!

    if (iface != null) {
      iface.paint(alpha);
    }
  }

  @Override
  public void update(float delta) {
    for (Pingo pingo : pingos) {
      pingo.update(delta);
      beat = beat + 1;
      if( beat / beats_coelhodia >= 30 ){
          // pingo morre
          // beat = beat; // pass
          //pingos.del(pingo);
        PingoMorto pingomorto = new PingoMorto(layer, width() / 2, height() / 2);
        pingosmortos.add(pingomorto);
      }
    }
    for (PingoMorto pingomorto : pingosmortos) {
      pingomorto.update(delta);
    }

    if (iface != null) {
      iface.update(delta);
    }
  }

  @Override
  public int updateRate() {
    return 100;
  }
}
