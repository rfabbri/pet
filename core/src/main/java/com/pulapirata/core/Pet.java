package com.pulapirata.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Pointer;
import playn.core.CanvasImage;
import playn.core.util.Clock;
import playn.core.PlayN;
import playn.core.Font;

import com.pulapirata.core.sprites.Pingo;
import com.pulapirata.core.sprites.PingoMorto;
import com.pulapirata.core.sprites.PingoVomitando;
import com.pulapirata.core.sprites.PingoBebado;
import com.pulapirata.core.sprites.PingoComa;
import com.pulapirata.core.sprites.PingoPiscando;
// TODO: we need a generic sprite class; or the layer could automatically update
// them

import react.Function;
import react.UnitSlot;
import react.Slot;

//import tripleplay.ui.*;
import tripleplay.ui.Element;
import tripleplay.ui.Selector;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Icon;
import tripleplay.ui.Icons;
import tripleplay.ui.ToggleButton;
import tripleplay.ui.Label;
import tripleplay.ui.Group;
import tripleplay.ui.Interface;
import tripleplay.ui.Root;
import tripleplay.ui.SimpleStyles;
import tripleplay.ui.Style;
import tripleplay.ui.Styles;
import tripleplay.ui.Stylesheet;
import tripleplay.ui.layout.TableLayout;
import tripleplay.ui.layout.AbsoluteLayout;
import tripleplay.ui.layout.AxisLayout;
import tripleplay.util.Randoms;

import static tripleplay.ui.layout.TableLayout.COL;


public class Pet extends Game.Default {
  //------ Basic Game Properties
  private GroupLayer layer;
  private Group main_stat_; //< statusbar
  private Interface iface, statbar_iface;
  private Stylesheet petSheet;

  // FIXME graphics.width() is weird in html, not respecting #playn-root
  // properties. 
  public int width()  { return 480; }
  public int height() { return 800; }

  public static final int UPDATE_RATE = 100;  // ms 
  protected final Clock.Source _clock = new Clock.Source(UPDATE_RATE);

  //------ Pet properties
  private int beat = 0;   //< absolute number of updates
  // the following is not static so that we can dynamically speedup the game if desired
  private int beats_coelhodia = 600;  //< beats por 1 coelho dia.
  private double beats_coelhosegundo = (double)beats_coelhodia/(24.*60.*60.); 
  public int idade_coelhodias() { return beat / beats_coelhodia; }
  public int idade_coelhohoras() { return (int)((float)beat / ((float)beats_coelhodia/24f)); }
  public String idade_coelhodias_str() { 
    if (idade_coelhodias() == 0)
      return String.format(STAT_FILLER_1, idade_coelhohoras(), "h", alcool_, alcool_max_); 
    else
      return String.format(STAT_FILLER_1, idade_coelhodias(), " dias", alcool_, alcool_max_); 

  }

  private  static final String STAT_ALERT_1 = "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
  private  static final String STAT_FILLER_1 = "Idade: %d %s\nAlcool: %d/%d";

  private Pingo pingo = null;
  private PingoMorto pingomorto = null;
  private PingoVomitando pingovomitando = null;
  private PingoBebado pingobebado = null;
  private PingoComa pingocoma = null;
  private PingoPiscando pingopiscando = null;

  private int alcool_ = 0;
  private int alcool_passivo_ = -1;
  private int alcool_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.,1); //1 hora
  private int alcool_max_ = 10;
  private int alcool_min_ = 0;

  private int fome_ = 30;
  private int fome_passivo = -5;
  private int fome_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./4.,1); //15 min
  private int fome_max = 120;
  private int fome_min = -20;

  private int humor_ = 30;
  private int humor_passivo = -5;
  private int humor_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./3.,1); //20 min
  private int humor_max = 120;
  private int humor_min = -20;

  private int social_ = 30;
  private int social_passivo = -5;
  private int _passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.*2./3.,1); //40min
  private int social_max = 120;
  private int social_min = -20;

  private int higiene_ = 30;
  private int higiene_passivo = -5;
  private int higiene_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./2.,1); //30 min
  private int higiene_max = 120;
  private int higiene_min = -20;
 
  private int estudo_ = 0;
  private int estudo_passivo = -1;
  //private int estudo_passivo_beats_ = ;//? por dia a partir da matricula (colocar um valor inicial depois da matricula mudar)
  //(int) Math.max(beats_coelhosegundo*60.*60.*24.,1); //dia
  private int estudo_max = 10;
  private int estudo_min = -5;
  

  private int saude_ = 5;
  private int saude_passivo = -1;
  private int saude_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.*24.,1);//? por idade (em dias?)
  private int saude_max = 10;
  private int saude_min = -5;

  private int disciplina_ = 0;
  private int disciplina_max = 10;
  private int disciplina_min = -5;

  private final Randoms _rando = Randoms.with(new Random());//Para gerar numeros aleatorios
  private int r;
 
  //private boolean matricula = false;

  //--------------------------------------------------------------------------------
  public Pet() {
    super(UPDATE_RATE);
  }

  //--------------------------------------------------------------------------------
  private void make_statusbar() {
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

    // test: write something in white letters: Pet


    // ------ The text in the status bar as a tripleplay nested layout interface 
  
    // TODO: e o tal do gaps?
    final int mae = 20; // mae == margin on the sides of exlamation
    final int mte = 18; // mae == margin on top of exlamation
    
    // sm stands for statbar_margin
    TableLayout statbar_layout = new TableLayout(COL.minWidth(30).alignLeft(), COL.stretch()).gaps(mae,mae).alignTop();
    // the left status plus is the left column
    // the (!) icon plust the right text is the right column

    TableLayout rightpart_layout = new TableLayout(COL.fixed().minWidth(30), COL.alignLeft()).gaps(mae,mae).alignTop();

    Image exclamacao = assets().getImage("pet/images/exclamacao.png");

  

    // Cria um grupo para os caras da esquerda
    // Basicamente 2 labels: nome grandao e indicadores em fonte menor

    String age = idade_coelhodias_str(); 

    main_stat_ = new Group (AxisLayout.vertical()).add (
        new Label("PINGO").addStyles(Styles.make(
            Style.COLOR.is(0xFFFFFFFF),
            Style.HALIGN.left,
            Style.FONT.is(PlayN.graphics().createFont("Helvetica", Font.Style.PLAIN, 24))
        )),
        new Label(age).addStyles(Styles.make(
            Style.COLOR.is(0xFFFFFFFF),
            Style.HALIGN.left
        ))
    ).addStyles(Styles.make(Style.HALIGN.left));


    final Group statbar = new Group (statbar_layout).add (
        main_stat_,
        new Group(rightpart_layout).add (
          new Button(Icons.image(exclamacao)), // FIXME an icon goes here or else blank space w icon's size
          // TODO in future this button will actually be an animation sprite
          new Label(STAT_ALERT_1).addStyles(Styles.make(
            Style.COLOR.is(0xFFFFFFFF),
            Style.TEXT_WRAP.is(true),
            Style.HALIGN.left
            ))
        )
    ).addStyles(Style.VALIGN.top);

    // create our UI manager and configure it to process pointer events
    statbar_iface = new Interface();
   
    //petSheet.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root statbar_root = statbar_iface.createRoot(new AbsoluteLayout(), petSheet);

    statbar_root.setSize(width(), 120); // this includes the secondary buttons

    layer.addAt(statbar_root.layer, 0, 0);
    statbar_root.add(AbsoluteLayout.at(statbar,mae,mte,width()-mae,120-mte));
  }

  //--------------------------------------------------------------------------------
  private void make_background() {
    Image bgImage = assets().getImage("pet/images/cenario_quarto.png");
    ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    layer.addAt(bgLayer, 0, 120);
  }

  //--------------------------------------------------------------------------------
  private void make_buttons() {
    // create our UI manager and configure it to process pointer events
    iface = new Interface();

    //petSheet.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root root = iface.createRoot(new AbsoluteLayout(), petSheet);

    root.setSize(width(), 354); // this includes the secondary buttons
    //    root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
    layer.addAt(root.layer, 0, 442);

    final Group buttons = new Group(new AbsoluteLayout()).addStyles(
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
              assets().getImage("pet/main-buttons/071_lazer.png"), // licor
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
      ToggleButton but = new ToggleButton (Icons.image(img_butt_solto.get(0)));
      buttons.add(AbsoluteLayout.at(but, topleft[b][0], topleft[b][1], 120, 120));

      // add button b's secondary buttons TODO: use animated sheets for them
      
      sbuttons.add(new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.solid(0x55FFFFFF))));

      for (int s = 0; s < img_butt_secondary.get(b).size(); ++s) {
        Button sbut = new Button(Icons.image(img_butt_secondary.get(b).get(s)));
        sbuttons.get(b).add(AbsoluteLayout.at(sbut, 
          topleft_secondary[s][0], topleft_secondary[s][1], 120, 120));

        if (b == 6 // diversao
        &&  s == 0) // licor
          sbut.clicked().connect(new UnitSlot() {
            public void onEmit() {
              alcool_ = alcool_max_; // TODO modificar de acordo com folha
            }
          });

      }

      but.selected().map(new Function <Boolean,Icon>() {
        public Icon apply (Boolean selected) {
               if (selected) {
                  return Icons.image(img_butt_apertado.get(b_final));
               } else {
                  return Icons.image(img_butt_solto.get(b_final));
               }
      }
      }).connectNotify(but.icon.slot());

      // all secondary buttons are added; toggle visibility only
      root.add(AbsoluteLayout.at(sbuttons.get(b_final), 0, 0, width(), 120));
      sbuttons.get(b_final).setVisible(false);
    }

    Selector sel = new Selector(buttons, null);
    root.add(AbsoluteLayout.at(buttons, 0, 118, width(), 236));

    // TODO: improve this part with a button-> index map so we don't go through
    // all butts
    sel.selected.connect(new Slot<Element<?>>() {
      @Override public void onEmit (Element<?> event) {
        if (event == null) {
          for (Group sb : sbuttons) {
            sb.setVisible(false);
          }
        } else {
          for (int i=0; i < num_main_butts; ++i) {
            if (buttons.childAt(i) == (ToggleButton) event && 
                sbuttons.get(i).childCount() != 0) {
              sbuttons.get(i).setVisible(true);
            } else {
              sbuttons.get(i).setVisible(false);
            }
          }
        }
      }
    });


    /* Exemplo p/ sinais e eventos
     *
    but0.clicked().connect(new UnitSlot() {
        @Override
        public void onEmit() {
          System.out.println("but0 clicked!");
          but0.icon.update(but0press);
        }
    });
    */
  }


  //--------------------------------------------------------------------------------
  @Override
  public void init() {
    System.out.println("passivo is " + alcool_passivo_beats_);
    System.out.println("coelho seg " + beats_coelhosegundo);

    // create a group layer to hold everything
    layer = graphics().createGroupLayer();
    graphics().rootLayer().add(layer);
    petSheet = PetStyles.newSheet();
    

    // ------------------------------------------------------------------
    make_statusbar();
    make_background();
    make_buttons();
    // ------------------------------------------------------------------

    // sprites
    pingo = new Pingo(layer, width() / 2, height() / 2);
 
  }


  //--------------------------------------------------------------------------------
  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the sprites group layer automatically
    // so no need to do anything here!

    if (iface != null) {
      iface.paint(_clock);
    }
    if (statbar_iface != null) {
      statbar_iface.paint(_clock);
    }
  }

  //--------------------------------------------------------------------------------
  @Override
  public void update(int delta) {
    _clock.update(delta);
    if (pingo != null)
      pingo.update(delta);
    else if (pingovomitando != null)
      pingovomitando.update(delta);
    else if (pingobebado != null)
      pingobebado.update(delta);
    else if (pingocoma != null)
      pingocoma.update(delta);

    if(beat / beats_coelhodia >= 30) {
      if (pingomorto == null) {
          // pingo morre
          // beat = beat; // pass
          //pingos.del(pingo);
        pingomorto = new PingoMorto(layer, width() / 2, height() / 2);
        pingo.detatch(layer);
        pingo = null;
      } else {
        pingomorto.update(delta);
      }
    } else {
      // update properties
      if (alcool_ == 10) {
        if (pingocoma == null) {
          pingocoma = new PingoComa(layer, width() / 2, height() / 2);
          if (pingo != null) {
            pingo.detatch(layer);
            pingo = null;
          }
        }
      } else  {
        if (pingocoma != null) {
          pingocoma.detatch(layer);
          pingocoma = null;
        }

        if (alcool_ >= 7) {
          if (pingovomitando == null) {
            if (pingo != null) {
              pingo.detatch(layer);
              pingo = null;
            }
            pingovomitando = new PingoVomitando(layer, width() / 2, height() / 2);
          }
        } else {
          if (pingovomitando != null) {
            pingovomitando.detatch(layer);
            pingovomitando = null;
          }
        
          if (alcool_ >= 4) {
            if (pingobebado == null) {
              if (pingo != null) {
                pingo.detatch(layer);
                pingo = null;
              }
              pingobebado  = new PingoBebado(layer, width() / 2, height() / 2);
            }
          } else {
            if (pingobebado != null) {
              pingobebado.detatch(layer);
              pingobebado = null;
              pingo = new Pingo(layer, width() / 2, height() / 2);
            }
          }
        }
      }

      // update clock and passives
      beat = beat + 1;

      if ((beat % alcool_passivo_beats_) == 0)
        if (alcool_ > alcool_min_)
          alcool_ += alcool_passivo_;

      Label l = (Label) main_stat_.childAt(1);
      l.text.update(idade_coelhodias_str());
    }

    if (iface != null) {
      iface.update(delta);
    }
    if (statbar_iface != null) {
      statbar_iface.update(delta);
    }



//Pingo piscando
    if(pingo != null){
	System.out.println("Pingo Normal");
	pingo.detatch(layer);
	pingo = null;
	pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
	System.out.println("Pingo Piscando");

    }


//Pingo piscando
r = _rando.getInRange(1,4);//de 1 a 3 
/*
if(pingo!=null && beat/ ((int) Math.max(beats_coelhosegundo*60.*60.,1)) %r==0){
  System.out.println(r +" horas");
  pingo.detatch(layer);
  pingo = new Pingo(layer, width() / 2, height() / 2);;
}	
*/

    //System.out.println(_rando.getInRange(1,10));
 
 /*VIsualizando atributo no terminal*/
 //System.out.println("Fome: " + fome_);
 //System.out.println("Social: " + social_);
 //System.out.println("Humor: " + humor_);
 //System.out.println("Saude: " + saude_);
 //System.out.println("Alcool: " + alcool_); 


  }//fim do update

}
