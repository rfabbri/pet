package com.pulapirata.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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

import react.Function;
import react.UnitSlot;

import tripleplay.ui.Selector;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.ToggleButton;
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

    //    Stylesheet petSheet = SimpleStyles.newSheet();
    Stylesheet petSheet = PetStyles.newSheet();
    //petSheet.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root root = iface.createRoot(new AbsoluteLayout(), petSheet);

    root.setSize(width(), 354); // this includes the secondary buttons
    //    root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
    layer.addAt(root.layer, 0, 442);

    Group buttons = new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.blank()));

    // TODO we could use TableLayout in the future but I dont trust it now; 
    // I prefer pixel control for now.
    //
    //    Group iface = Group(new TableLayout(4).gaps(0, 0)).add(
    //      label("", Background.image(testBg)),
    //    );

    final ArrayList<Image> img_butt_solto = 
        new ArrayList<Image>(Arrays.asList(
        assets().getImage("pet/main-buttons/01_comida_principal.png"),
        assets().getImage("pet/main-buttons/02_diversao_principal.png"),
        assets().getImage("pet/main-buttons/03_social_principal.png"),
        assets().getImage("pet/main-buttons/04_higiene_principal.png"),
        assets().getImage("pet/main-buttons/05_obrigacoes_principal.png"),
        assets().getImage("pet/main-buttons/06_saude_principal.png"),
        assets().getImage("pet/main-buttons/07_lazer_principal.png"),
        assets().getImage("pet/main-buttons/08_disciplina_principal.png")
        ));

    final ArrayList<Image> img_butt_apertado = 
        new ArrayList<Image> (Arrays.asList(
        assets().getImage("pet/main-buttons/01_comida_principal_apertado.png"),
        assets().getImage("pet/main-buttons/02_diversao_principal_apertado.png"),
        assets().getImage("pet/main-buttons/03_social_principal_apertado.png"),
        assets().getImage("pet/main-buttons/04_higiene_principal_apertado.png"),
        assets().getImage("pet/main-buttons/05_obrigacoes_principal_apertado.png"),
        assets().getImage("pet/main-buttons/06_saude_principal_apertado.png"),
        assets().getImage("pet/main-buttons/07_lazer_principal_apertado.png"),
        assets().getImage("pet/main-buttons/08_disciplina_principal_apertado.png")
        ));

    ArrayList< ArrayList<Image> > s_img_butt_secondary = new ArrayList< ArrayList<Image> > (0);

    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/011_comida.png"),
              assets().getImage("pet/main-buttons/012_comida.png"),
              assets().getImage("pet/main-buttons/013_comida.png"),
              assets().getImage("pet/main-buttons/014_comida.png")
    )));

    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/021_diversao.png"),
              assets().getImage("pet/main-buttons/022_diversao.png"),
              assets().getImage("pet/main-buttons/023_diversao.png"),
              assets().getImage("pet/main-buttons/024_diversao.png")
    )));
    
    s_img_butt_secondary.add(
        new ArrayList<Image> (0)
    );

    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/041_higiene.png"),
              assets().getImage("pet/main-buttons/042_higiene.png"),
              assets().getImage("pet/main-buttons/043_higiene.png"),
              assets().getImage("pet/main-buttons/044_higiene.png")
    )));
    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/051_obrigacoes.png"),
              assets().getImage("pet/main-buttons/052_obrigacoes.png")
    )));
    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/061_saude.png"),
              assets().getImage("pet/main-buttons/062_saude.png")
    )));
    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/071_lazer.png"),
              assets().getImage("pet/main-buttons/072_lazer.png")
    )));
    s_img_butt_secondary.add(
        new ArrayList<Image> (Arrays.asList(
              assets().getImage("pet/main-buttons/081_disciplina.png"),
              assets().getImage("pet/main-buttons/082_disciplina.png"),
              assets().getImage("pet/main-buttons/083_disciplina.png"),
              assets().getImage("pet/main-buttons/084_disciplina.png")
    )));

    final ArrayList< ArrayList<Image> > img_butt_secondary = s_img_butt_secondary;

    final int[][] topleft = new int [][] {
      {0,0},
      {120,0},
      {240,0},
      {360,0},
      {0,120},
      {120,120},
      {240,120},
      {360,120},
    };

    final int[][] topleft_secondary = new int [][] {
      {0,0},
      {120,0},
      {240,0},
      {360,0},
    };

    final int num_main_butts = img_butt_solto.size();
    final ArrayList<Group> sbuttons = new ArrayList<Group>(0);

    for (int b =0; b < num_main_butts; ++b) {
      final int b_final = b;
      ToggleButton but = new ToggleButton (img_butt_solto.get(0));
      buttons.add(AbsoluteLayout.at(but, topleft[b][0], topleft[b][1], 120, 120));

      // add button b's secondary buttons TODO: use animated sheets for them
      
      sbuttons.add(new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.solid(0x55FFFFFF))));

      for (int s = 0; s < img_butt_secondary.get(b).size(); ++s) {
        Button sbut = new Button(img_butt_secondary.get(b).get(s));
        sbuttons.get(b).add(AbsoluteLayout.at(sbut, 
          topleft_secondary[s][0], topleft_secondary[s][1], 120, 120));
      }


      but.selected.map(new Function <Boolean, Image>() {
        public Image apply (Boolean selected) {
               if (selected) {
                  sbuttons.get(b_final).setVisible(true);
                  return img_butt_apertado.get(b_final);
               } else {
                  sbuttons.get(b_final).setVisible(false);
                  return img_butt_solto.get(b_final);
               }
      }}).connectNotify(but.icon.slot());

      // all secondary buttons are added; toggle visibility only
      root.add(AbsoluteLayout.at(sbuttons.get(b_final), 0, 0, width(), 120));
      sbuttons.get(b_final).setVisible(false);
    }

    Selector sel = new Selector(buttons, null);
    root.add(AbsoluteLayout.at(buttons, 0, 118, width(), 236));

    /*
    but0.clicked().connect(new UnitSlot() {
        @Override
        public void onEmit() {
          System.out.println("but0 clicked!");
          but0.icon.update(but0press);
        }
    });
    */
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
