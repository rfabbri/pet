package com.pulapirata.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.*;
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
import playn.core.Sound;
import playn.core.Json;

import com.pulapirata.core.sprites.Pingo;
import com.pulapirata.core.sprites.PingoMorto;
import com.pulapirata.core.sprites.PingoVomitando;
import com.pulapirata.core.sprites.PingoBebado;
import com.pulapirata.core.sprites.PingoComa;
import com.pulapirata.core.sprites.PingoBebendoAgua;
import com.pulapirata.core.sprites.PingoBebendoLeite;
import com.pulapirata.core.sprites.PingoComendoSopaBacon;
import com.pulapirata.core.sprites.PingoComendoSopaCenoura;
import com.pulapirata.core.sprites.PingoPiscando;
import com.pulapirata.core.sprites.PingoDormindo;
import com.pulapirata.core.sprites.PingoChorando;
import com.pulapirata.core.sprites.PingoTriste;
import com.pulapirata.core.utils.PetJson;
import com.pulapirata.core.Aviso;

// TODO: we need a generic sprite class; or the layer could automatically update
// them

import react.Function;
import react.UnitSlot;
import react.Slot;

import tripleplay.ui.Element;
import tripleplay.ui.Elements;
import tripleplay.ui.Selector;
import tripleplay.ui.Background;
import tripleplay.ui.Button;
import tripleplay.ui.Icon;
import tripleplay.ui.Icons;
import tripleplay.ui.ToggleButton;
import tripleplay.ui.Label;
import tripleplay.ui.Group;
import tripleplay.ui.SizableGroup;
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

  /*===============================================================================*/
  /* Data                                                                          */
  /*===============================================================================*/

  /*-------------------------------------------------------------------------------*/
  /* Game config data management */

  protected PetJson petJson_;

  /*-------------------------------------------------------------------------------*/
  /* Status info shown on top */

  protected  static final String STAT_ALERT_1 =
    "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
  protected  static final String STAT_FILLER_1 = "Idade: %d%s\n Sede: %d/%d\n";
  protected  static final String STAT_FILLER_2 = "\nFome: %d/%d\n Alcool: %d/%d";

  /*-------------------------------------------------------------------------------*/
  /* Sounds */

  private Sound somArroto_ = assets().getSound("pet/sprites/arroto_01");
  private Sound somSoluco_ = assets().getSound("pet/sprites/soluco_01");

  /*-------------------------------------------------------------------------------*/

  // FIXME graphics.width() is weird in html, not respecting #playn-root
  // properties.
  public int width()  { return 480; }
  public int height() { return 800; }

  /*-------------------------------------------------------------------------------*/
  /* Time data */

  public static final int UPDATE_RATE = 100; // ms
  private int beat_ = 0; // total number of updates so far
   // the following is not static so that we can dynamically speedup the game if desired
  private int beatsCoelhoDia_ = 600; // beats por 1 coelho dia.
  private double beatsCoelhoSegundo_ = (double)beatsCoelhoDia_/(24.*60.*60.);
  public int idadeCoelhoHoras() { return (int)((float)beat_ / ((float)beatsCoelhoDia_/24f)); }
  public int idadeCoelhoDias() { return beat_ / beatsCoelhoDia_; }

  /*-------------------------------------------------------------------------------*/
  /* Pet attributes & info */

  private int sede_ = 5;
  private int sedePassivo_ = 1;
  private int sedePassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60*60,1);
  private int sedeMax_ = 10;
  private int sedeMin_ = 0;

  private int fome_ = PetJson.readJson("pet/jsons/atributos.json","fome").fome();
  // private int fome_ = 20;
  private int fomePassivo_ = 10;
  private int fomePassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60*60,1);
  private int fomeMax_ = 120;
  private int fomeMin_ = -20;

  private int alcool_ = 3;
  private int alcoolPassivo_ = -1;
  private int alcoolPassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.,1);
  private int alcoolMax_ = 10;
  private int alcoolMin_ = 0;

  private int humor_ = 30;
  private int humorPassivo_ = -5;
  private int humorPassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60./3.,1); //20 min
  private int humorMax_ = 120;
  private int humorMin_ = -20;

  private int social_ = 30;
  private int socialPassivo_ = -5;
  private int socialPassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.*2./3.,1); //40min
  private int socialMax_ = 120;
  private int socialMin_ = -20;

  private int higiene_ = 30;
  private int higienePassivo_ = -5;
  private int higienePassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60./2.,1); //30 min
  private int higieneMax_ = 120;
  private int higieneMin_ = -20;

  private int estudo_ = 0;
  private int estudoPassivo_ = -1;
  // private int estudoPassivo_beats_ = ;
  //  ? por dia a partir da matricula (colocar um valor inicial depois da matricula mudar)
  // (int) Math.max(beatsCoelhoSegundo_*60.*60.*24.,1); //dia
  private int estudoMax_ = 10;
  private int estudoMin_ = -5;

  private int saude_ = 5;
  private int saudePassivo_ = -1;
  private int saudePassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.*24.,1);//? por idade (em dias?)
  private int saudeMax_ = 10;
  private int saudeMin_ = -5;

  private int disciplina_ = 0;
  private int disciplinaMax_ = 10;
  private int disciplinaMin_ = -5;

  /*-------------------------------------------------------------------------------*/

  private boolean dormir_ = false;
  private int diaProibidoBeber_ = 0;

  /*===============================================================================*/
  /* Code                                                                          */
  /*===============================================================================*/

  /*-------------------------------------------------------------------------------*/
  /* Misc variables */

  private final Randoms _rando = Randoms.with(new Random());
  private int r_; // excluir depois

  protected final Clock.Source clock_ = new Clock.Source(UPDATE_RATE);

  /*-------------------------------------------------------------------------------*/
  /* Layers, groups & associated resources */

  private ImageLayer bgLayer_ = null;
  private Image bgImageDay_, bgImageNight_;
  private GroupLayer layer_;
  protected Group mainStat_;
  private Group rightStatbarGroup_;
  private TableLayout rightPartLayout_;
  private Interface iface_, statbarIface_;
  private Image exclamacao_;
  private Stylesheet petSheet_;

  /*-------------------------------------------------------------------------------*/
  /* Pet Sprites */

  protected Pingo pingo_ = null;
  protected PingoMorto pingoMorto_ = null;
  protected PingoVomitando pingoVomitando_ = null;
  protected PingoBebado pingoBebado_ = null;
  protected PingoComa pingoComa_ = null;
  protected PingoBebendoAgua pingoBebendoAgua_ = null;
  protected PingoBebendoLeite pingoBebendoLeite_ = null;
  protected PingoComendoSopaBacon pingoComendoSopaBacon_ = null;
  protected PingoComendoSopaCenoura pingoComendoSopaCenoura_ = null;
  protected PingoPiscando pingoPiscando_ = null;
  protected PingoDormindo pingoDormindo_ = null;
  protected PingoChorando pingoChorando_ = null;
  protected PingoTriste pingoTriste_ = null;

  /*-------------------------------------------------------------------------------*/
  /* Alerts/Avisos */

  private Aviso avisoAtual = new Aviso("Bem vindo ao jogo Pet");
  private Aviso fomeAviso = new Aviso();
  private Aviso humorAviso = new Aviso();
  private Aviso socialAviso = new Aviso();
  private Aviso higieneAviso = new Aviso();
  private Aviso estudoAviso = new Aviso();
  private Aviso saudeAviso = new Aviso();
  private Aviso disciplinaAviso = new Aviso();
  private Aviso alcoolAviso = new Aviso();
  private List<Aviso> avisos = new ArrayList<Aviso>();//List que conterá os avisos
  //private String aviso_status_bar="ola";
  private ListIterator<Aviso> elementos;


  //--------------------------------------------------------------------------------
  /* funcoes para setar as informacoes no topo corretamente.  */

  public String idadeCoelhoDiasStr1() {
    if (idadeCoelhoDias() == 0)
      return String.format(STAT_FILLER_1, idadeCoelhoHoras(), "h", sede_, sedeMax_);
    else
      return String.format(STAT_FILLER_1, idadeCoelhoDias(), " dias", sede_, sedeMax_);
  }
  public String idadeCoelhoDiasStr2() {
      return String.format(STAT_FILLER_2, fome_ , fomeMax_, alcool_, alcoolMax_);
  }

  //--------------------------------------------------------------------------------
  public Pet() {
    super(UPDATE_RATE);
  }

  //--------------------------------------------------------------------------------
  private void makeStatusbar() {
    // create and add the status title layer using drawings for faster loading
    CanvasImage bgtile = graphics().createImage(480, 119);
    bgtile.canvas().setFillColor(0xFFFFFFFF);
    bgtile.canvas().fillRect(0, 0, 480, 119);
    bgtile.canvas().setFillColor(0xFF333366);
    bgtile.canvas().fillRect(4, 4, 472, 112);
    //Fonte
    //Font font = graphics().createFont("earthboundzero", Font.Style.PLAIN, 18);
    ImageLayer statlayer = graphics().createImageLayer(bgtile);
    //
    //  statlayer.setWidth(graphics().width());
    // FIXME: problem with graphics.width not being set correctly in html;
    // it always seems to give 640
    //
    statlayer.setHeight(120);//altura do retangulo de informacoes
    layer_.add(statlayer);

    // test: write something in white letters: Pet


    // ------ The text in the status bar as a tripleplay nested layout interface

    // TODO: e o tal do gaps?
    final int mae = 20; // mae == margin on the sides of exlamation
    final int mte = 18; // mae == margin on top of exlamation

    final int mainStatWidth = 200;

    // sm stands for statbar_margin
    TableLayout statbarLayout = new TableLayout(
      COL.minWidth(mainStatWidth).alignLeft().fixed(),
      COL.minWidth(30).stretch()).gaps(mae,mae).alignTop();

//    AxisLayout statbarLayout = new AxisLayout.horizontal().add(
//      COL.minWidth(250).alignLeft().fixed(),
//      COL.minWidth(30).stretch()).gaps(mae,mae).alignTop();
    // the left status plus is the left column
    // the (!) icon plust the right text is the right column

    rightPartLayout_ = new TableLayout(COL.fixed().minWidth(30), COL.alignLeft()).gaps(mae,mae).alignTop();

    exclamacao_ = assets().getImage("pet/images/exclamacao.png");


    // Cria um grupo para os caras da esquerda
    // Basicamente 2 labels: nome grandao e indicadores em fonte menor

    String age1 = idadeCoelhoDiasStr1();
    String age2 = idadeCoelhoDiasStr2();

    mainStat_ = new SizableGroup (AxisLayout.vertical(), mainStatWidth, 0).add (
        new Label("PINGO").addStyles(Styles.make(
            Style.COLOR.is(0xFFFFFFFF),
            Style.HALIGN.left,
            //Style.FONT.is(PlayN.graphics().createFont("Helvetica", Font.Style.PLAIN, 24))
            Style.FONT.is(PlayN.graphics().createFont("EarthboundZero", Font.Style.PLAIN, 31)),
            Style.AUTO_SHRINK.is(true)
        )),
        new Label(age1).addStyles(Styles.make(
            Style.COLOR.is(0xFFFFFFFF),
            Style.HALIGN.left,
            Style.FONT.is(PlayN.graphics().createFont("EarthboundZero", Font.Style.PLAIN, 16)),
            Style.AUTO_SHRINK.is(true)
        )),
        new Label(age2).addStyles(Styles.make(
                    Style.COLOR.is(0xFFFFFFFF),
                    Style.HALIGN.left,
                    Style.FONT.is(PlayN.graphics().createFont("EarthboundZero", Font.Style.PLAIN, 16)),
                    Style.AUTO_SHRINK.is(true)
                ))
        ).addStyles(Styles.make(Style.HALIGN.left));

    rightStatbarGroup_ = new Group(rightPartLayout_).add (
          new Button(Icons.image(exclamacao_)), // FIXME an icon goes here or else blank space w icon's size
          // TODO in future this button will actually be an animation sprite
          new Label(avisoAtual.getAviso()).addStyles(Styles.make(
              Style.COLOR.is(0xFFFFFFFF),
              Style.TEXT_WRAP.is(true),
              Style.HALIGN.left,
              Style.FONT.is(PlayN.graphics().createFont("EarthboundZero", Font.Style.PLAIN, 16))
              ))
          );

    Group statbar = new Group (statbarLayout).add (
        mainStat_,
        rightStatbarGroup_
        ).addStyles(Style.VALIGN.top);

    // create our UI manager and configure it to process pointer events
    statbarIface_ = new Interface();

    //petSheet_.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root statbarRoot = statbarIface_.createRoot(new AbsoluteLayout(), petSheet_);

    statbarRoot.setSize(width(), 120); // this includes the secondary buttons

    layer_.addAt(statbarRoot.layer, 0, 0);
    statbarRoot.add(AbsoluteLayout.at(statbar,mae,mte,width()-mae,120-mte));
  }

  //--------------------------------------------------------------------------------
  private void makeBackgroundInit() {
    bgImageDay_ = assets().getImage("pet/images/cenario_quarto.png");
    bgImageNight_ = assets().getImage("pet/images/cenario_quarto_noite.png");
    bgLayer_ = graphics().createImageLayer(bgImageDay_);
    layer_.addAt(bgLayer_, 0, 120); //janela do quarto do pingo
  }

  private void setBackgroundDay() {
    bgLayer_.setImage(bgImageDay_);
  }

  private void setBackgroundNight() {
    bgLayer_.setImage(bgImageNight_);
  }


  //--------------------------------------------------------------------------------
  /* Funcao responsavel por criar os botoes, estes sao colocados em um ArraList */

  private void makeButtons() {
    // create our UI manager and configure it to process pointer events
    iface_ = new Interface();

    //petSheet_.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root root_ = iface_.createRoot(new AbsoluteLayout(), petSheet_);

    root_.setSize(width(), 354); // this includes the secondary buttons
    //    root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
    layer_.addAt(root_.layer, 0, 442); //Position of buttons

    final Group buttons = new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.blank()));

    // TODO we could use TableLayout in the future but I dont trust it now;
    // I prefer pixel control for now.
    //
    //    Group iface_ = Group(new TableLayout(4).gaps(0, 0)).add(
    //      label("", Background.image(testBg)),
    //    );

    final ArrayList<Image> imgButtSolto =
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

    final ArrayList<Image> imgButtApertado =
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

    ArrayList< ArrayList<Image> > s_imgButtSecondary = new ArrayList< ArrayList<Image> > (0);

    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/011_comida.png"),
            assets().getImage("pet/main-buttons/012_comida.png"),
            assets().getImage("pet/main-buttons/013_comida.png"),
            assets().getImage("pet/main-buttons/014_comida.png")
            )));

    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/021_diversao.png"),
            assets().getImage("pet/main-buttons/022_diversao.png"),
            assets().getImage("pet/main-buttons/023_diversao.png"),
            assets().getImage("pet/main-buttons/024_diversao.png")
            )));

    s_imgButtSecondary.add(
        new ArrayList<Image> (0)
        );

    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/041_higiene.png"),
            assets().getImage("pet/main-buttons/042_higiene.png"),
            assets().getImage("pet/main-buttons/043_higiene.png"),
            assets().getImage("pet/main-buttons/044_higiene.png")
            )));
    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/051_obrigacoes.png"),
            assets().getImage("pet/main-buttons/052_obrigacoes.png")
            )));
    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/061_saude.png"),
            assets().getImage("pet/main-buttons/062_saude.png")
            )));
    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/071_lazer.png"), // licor
            assets().getImage("pet/main-buttons/072_lazer.png")
            )));
    s_imgButtSecondary.add(
        new ArrayList<Image> (Arrays.asList(
            assets().getImage("pet/main-buttons/081_disciplina.png"),
            assets().getImage("pet/main-buttons/082_disciplina.png"),
            assets().getImage("pet/main-buttons/083_disciplina.png"),
            assets().getImage("pet/main-buttons/084_disciplina.png")
            )));

    final ArrayList< ArrayList<Image> > imgButtSecondary = s_imgButtSecondary;

    /*
      Posicao de cada "butt"
    */
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

    final int[][] topleftSecondary = new int [][] {
      {0,0},
        {120,0},
        {240,0},
        {360,0},
    };
    /*-------------------------------------------------------------------------------*/

    final int numMainButts = imgButtSolto.size();
    final ArrayList<Group> sbuttons = new ArrayList<Group>(0);

    for (int b =0; b < numMainButts; ++b) {
      final int bFinal = b;
      ToggleButton but = new ToggleButton (Icons.image(imgButtSolto.get(0)));
      buttons.add(AbsoluteLayout.at(but, topleft[b][0], topleft[b][1], 120, 120));

      // add button b's secondary buttons TODO: use animated sheets for them
      sbuttons.add(new Group(new AbsoluteLayout()).addStyles(
        Style.BACKGROUND.is(Background.solid(0x55FFFFFF))));

      for (int s = 0; s < imgButtSecondary.get(b).size(); ++s) {
        Button sbut = new Button(Icons.image(imgButtSecondary.get(b).get(s)));
        sbuttons.get(b).add(AbsoluteLayout.at(sbut,
          topleftSecondary[s][0], topleftSecondary[s][1], 120, 120));
        /*
          Acesso todos os butoes primarios (b) e secundarios (s) de forma a criar
          cada evento. Feito isso, apos instancia uma classe/sprite referente a
          aquela acao.  A funcao "detatch" eh utilziada para remover o sprite de
          tela, e em seguida seta-se a classe pingo para null. Provavelmente tera
          de mudar, pois eh necessario que tudo seja em funcao de atributos.
        */

        if(b == 0 && s == 0) sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento comer sopa de cenoura, cria um novo pingoComendoSopaCenoura_
            if(dormir_== false){
              pingoComendoSopaCenoura_ = new PingoComendoSopaCenoura(layer_, width()/2, height()/2);
              if (pingo_ != null) {
                pingo_.detatch(layer_);
                pingo_ = null;
              }
              //fome_ = fomeMax_;
            }
          }
        });

        if(b == 0 && s == 1) sbut.clicked().connect(new UnitSlot(){
            public void onEmit(){//Atravez do evento comer sopa de bacon, cria um novo pingoComendoSopaBacon_
              if (dormir_== false) {
                pingoComendoSopaBacon_ = new PingoComendoSopaBacon(layer_, width()/2, height()/2);
                if (pingo_ != null) {
                  pingo_.detatch(layer_);
                  pingo_ = null;
                }
              }
            //fome_ = fomeMax_;
            }
        });

        if(b == 0 && s == 2)sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento beber agua, cria um novo pingoBebendoAgua_
            if(dormir_== false){
             pingoBebendoAgua_ = new PingoBebendoAgua(layer_, width()/2, height()/2);
              if (pingo_ != null) {
                pingo_.detatch(layer_);
                pingo_ = null;
              }
            }
            //sede_ = sedeMax_;
          }
        });

        if(b == 0 && s == 3)sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento beber leite, cria um novo pingoBebendoLeite_
            if(dormir_== false){
              pingoBebendoLeite_ = new PingoBebendoLeite(layer_, width()/2, height()/2);
              if (pingo_ != null) {
                pingo_.detatch(layer_);
                pingo_ = null;
              }
            }
            //fome_ = fomeMax_;
          }
        });

        if(b == 5 && s == 0)sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Quando o pingo estiver em coma, deve-se dar a injecao a ele
            if(alcool_== alcoolMax_){
              alcool_ = alcool_ - 1;
              pingoVomitando_ = new PingoVomitando(layer_, width() / 2, height() / 2);
              if (pingoComa_ != null) {
                pingoComa_.detatch(layer_);
                pingoComa_ = null;
              }
            }
            //fome_ = fomeMax_;
          }
        });

        if (b == 6 /* diversao */ && s == 0/* licor */)sbut.clicked().connect(new UnitSlot() {
            public void onEmit() {
              if(dormir_== false && diaProibidoBeber_ != idadeCoelhoDias()){
                alcool_ = alcoolMax_; // TODO modificar de acordo com folha
              }
            }
          });
        }
      /*-------------------------------------------------------------------------------*/

      but.selected().map(new Function <Boolean,Icon>() {
        public Icon apply (Boolean selected) {
          if (selected)
             return Icons.image(imgButtApertado.get(bFinal));
          else
             return Icons.image(imgButtSolto.get(bFinal));
        }
      }).connectNotify(but.icon.slot());
      // all secondary buttons are added; toggle visibility only
      root_.add(AbsoluteLayout.at(sbuttons.get(bFinal), 0, 0, width(), 120));
      sbuttons.get(bFinal).setVisible(false);
    }

    Selector sel = new Selector(buttons, null);
    root_.add(AbsoluteLayout.at(buttons, 0, 118, width(), 236));

    // TODO: improve this part with a button-> index map so we don't go through
    // all butts
    sel.selected.connect(new Slot<Element<?>>() {
      @Override public void onEmit (Element<?> event) {
        if (event == null) {
          for (Group sb : sbuttons)
            sb.setVisible(false);
        } else {
          for (int i=0; i < numMainButts; ++i) {
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
  /*-------------------------------------------------------------------------------*/

  @Override
  public void init() {
    System.out.println("passivo is " + alcoolPassivoBeats_);
    System.out.println("coelho seg " + beatsCoelhoSegundo_);

    // create a group layer_ to hold everything
    layer_ = graphics().createGroupLayer();
    graphics().rootLayer().add(layer_);
    petSheet_ = PetStyles.newSheet();

    makeStatusbar();
    makeBackgroundInit();
    makeButtons();

    // sprites
    pingo_ = new Pingo(layer_, width() / 2, height() / 2);

    //Adicionando avisos na lista
    avisos.add(fomeAviso);
    avisos.add(humorAviso);
    avisos.add(socialAviso);
    avisos.add(higieneAviso);
    avisos.add(estudoAviso);
    avisos.add(saudeAviso);
    avisos.add(disciplinaAviso);
    avisos.add(alcoolAviso);
    //Iniciando o ListIterator e o Aviso atual
    elementos = avisos.listIterator();
    avisoAtual = elementos.next();
  }


  //--------------------------------------------------------------------------------
  @Override
  public void paint(float alpha) {
    // layers automatically paint themselves (and their children). The rootlayer
    // will paint itself, the background, and the sprites group layer_ automatically
    // so no need to do anything here!

    if (iface_ != null)
      iface_.paint(clock_);

    if (statbarIface_ != null)
      statbarIface_.paint(clock_);
  }

  //--------------------------------------------------------------------------------
  @Override
  public void update(int delta) {
    /*
      Aqui que sao realizadas as atualizacoes dos sprites, sem isto o sprite ficaria estatico
    */
    clock_.update(delta);
    if (pingo_ != null)
      pingo_.update(delta);
    else if (pingoVomitando_ != null)
      pingoVomitando_.update(delta);
    else if (pingoBebado_ != null)
      pingoBebado_.update(delta);
    else if (pingoComa_ != null)
      pingoComa_.update(delta);
    else if(pingoComendoSopaCenoura_ != null)
      pingoComendoSopaCenoura_.update(delta);
    else if(pingoComendoSopaBacon_ !=null)
      pingoComendoSopaBacon_.update(delta);
    else if (pingoBebendoAgua_ != null)
      pingoBebendoAgua_.update(delta);
    else if(pingoBebendoLeite_ != null)
      pingoBebendoLeite_.update(delta);
    else if(pingoDormindo_ != null)
      pingoDormindo_.update(delta);

    /*
      Eh realizada a verificacao de todos os atributos, e tomando acoes de acordo com cada funcionalidade
    */
    if (idadeCoelhoDias() >= 6) {
      if (pingoMorto_ == null) {
        // pingo morre
        pingoMorto_ = new PingoMorto(layer_, width() / 2, height() / 2);
        pingo_.detatch(layer_);
        pingo_ = null;
      } else
        pingoMorto_.update(delta);
    }
    else {
      // update properties
      if(fome_ <= fomeMin_ && pingoComendoSopaCenoura_ != null && pingo_ == null){
        fome_ = fomeMin_;
        pingo_ = new Pingo(layer_, width() / 2, height() / 2);
        pingoComendoSopaCenoura_.detatch(layer_);
        pingoComendoSopaCenoura_ = null;
        somArroto_.play();
      }
      else if (pingo_ != null && pingoDormindo_ != null) { // TENTAR RESOLVER AKI
        pingoComendoSopaCenoura_ = new PingoComendoSopaCenoura(layer_, width() / 2, height() / 2);
        if (pingo_ != null) {
          pingo_.detatch(layer_);
          pingo_ = null;
        }
/*        else if(pingoDormindo_!=null){
          pingoDormindo_.detatch(layer_);
          pingoDormindo_ = null;
        }*/
      }
      if(fome_ <= fomeMin_ && pingoComendoSopaBacon_ != null && pingo_ == null){
        fome_ = fomeMin_;
        pingo_ = new Pingo(layer_, width() / 2, height() / 2);
        pingoComendoSopaBacon_.detatch(layer_);
        pingoComendoSopaBacon_ = null;
        somArroto_.play();
      }

      //Quando a sede_ for 0, aqui é realizada a troca do layer_ dele bebendo agua para normal
      if(sede_ <= sedeMin_ && pingoBebendoAgua_ != null && pingo_ == null){
        sede_ = sedeMin_; // para caso na hora de decrementar, resultar em um valor negativo. Assim o fará ser 0
        pingo_ = new Pingo(layer_, width() / 2, height() / 2);
        pingoBebendoAgua_.detatch(layer_);
        pingoBebendoAgua_ = null;
        somArroto_.play();
      }

      if(fome_ <= fomeMin_ && pingoBebendoLeite_ != null && pingo_ == null){
        fome_ = fomeMin_;
        pingo_ = new Pingo(layer_, width() / 2, height() / 2);
        pingoBebendoLeite_.detatch(layer_);
        pingoBebendoLeite_ = null;
        somArroto_.play();
      }
      // FIXME estamos confundindo tempo de vida com hora de relogio.
      // Pet esta nascendo `a meia noite sempre - 0 horas
      int horaDoDia = idadeCoelhoHoras()-idadeCoelhoDias()*24;
      if (dormir_ == false && (horaDoDia >= 22 || horaDoDia <= 8)
          && idadeCoelhoDias() >= 1 && pingoPiscando_==null &&
          (pingo_!=null || pingoComendoSopaCenoura_ !=null
          || pingoComendoSopaBacon_ != null || pingoComendoSopaCenoura_ != null
          || pingoBebendoAgua_ != null || pingoBebendoLeite_ != null
          || pingoComa_ != null || pingoVomitando_ != null
          || pingoBebado_ != null)) {
        dormir_ = true;
        setBackgroundNight();

        pingoDormindo_ = new PingoDormindo(layer_, width()/2, height()/2);
        if (pingo_ != null) {
          pingo_.detatch(layer_);
          pingo_ = null;
        } else if(pingoPiscando_ != null) {
          pingoPiscando_.detatch(layer_);
          pingoPiscando_ = null;
        }        else if(pingoComendoSopaCenoura_ != null){
          pingoComendoSopaCenoura_.detatch(layer_);
          pingoComendoSopaCenoura_ = null;
        } else if(pingoComendoSopaBacon_ != null){
          pingoComendoSopaBacon_.detatch(layer_);
          pingoComendoSopaBacon_ = null;
        } else if(pingoBebendoAgua_ != null){
          pingoBebendoAgua_.detatch(layer_);
          pingoBebendoAgua_ = null;
        } else if(pingoBebendoLeite_ != null){
          pingoBebendoLeite_.detatch(layer_);
          pingoBebendoLeite_ = null;
        } else if(pingoComa_ != null){
          pingoComa_.detatch(layer_);
          pingoComa_ = null;
        } else if(pingoVomitando_ != null){
          pingoVomitando_.detatch(layer_);
          pingoVomitando_ = null;
        } else if(pingoBebado_ != null){
          pingoBebado_.detatch(layer_);
          pingoBebado_ = null;
        }
      }
      else if (horaDoDia < 22 && horaDoDia > 8) {
        if (pingoDormindo_ != null && pingo_ == null && pingoPiscando_ == null) {
          dormir_ = false;
          setBackgroundDay();
          pingo_ = new Pingo(layer_, width()/2, height()/2);
          pingoDormindo_.detatch(layer_);
          pingoDormindo_ = null;
        }
      }

      if (alcool_ == 10) {
        diaProibidoBeber_ = idadeCoelhoDias();
        if (pingoComa_ == null) {
            pingoComa_ = new PingoComa(layer_, width() / 2, height() / 2);
            somSoluco_.play();
          if (pingo_ != null) {
            pingo_.detatch(layer_);//remove the layer_
            pingo_ = null;
          }
        }
      } else {
        if (pingoComa_ != null) {
          pingoComa_.detatch(layer_);
          pingoComa_ = null;
        }

        if (alcool_ >= 7 && alcool_<=9) {
          if (pingoVomitando_ == null) {
            if (pingo_ != null) {
              pingo_.detatch(layer_);//remove the layer_
              pingo_ = null;
            }
            pingoVomitando_ = new PingoVomitando(layer_, width() / 2, height() / 2);
            somSoluco_.play();
          }
        } else {
          if (pingoVomitando_ != null) {
            pingoVomitando_.detatch(layer_);
            pingoVomitando_ = null;
          }

          if (alcool_ >= 4) {
            if (pingoBebado_ == null) {
              if (pingo_ != null) {
                pingo_.detatch(layer_);
                pingo_ = null;
              }
              pingoBebado_  = new PingoBebado(layer_, width() / 2, height() / 2);
                    somSoluco_.play();
            }
          } else {
            if (pingoBebado_ != null) {
              pingoBebado_.detatch(layer_);
              pingoBebado_ = null;
              pingo_ = new Pingo(layer_, width() / 2, height() / 2);
            }
            if(pingoPiscando_ != null)
              pingoPiscando_.update(delta);
          }
        }
      }
    } // end if

    // update clock and passives
    beat_++;

    Label l = (Label) mainStat_.childAt(1);
    l.text.update(idadeCoelhoDiasStr1());
    l =  (Label) mainStat_.childAt(2);
    l.text.update(idadeCoelhoDiasStr2());

    if (iface_ != null)
      iface_.update(delta);

    if (statbarIface_ != null)
      statbarIface_.update(delta);

    passivoAtributos();
    verificaAvisos();

    if (beat_ % alcoolPassivoBeats_ == 0) // a cada hora
      mudaAviso();

    if(beat_ % (2*alcoolPassivoBeats_) == 0)
      piscar();
    //System.out.println(_rando.getInRange(1,10));
  } // end update

  public void passivoAtributos() {
    //inicio dos passivos dos atributos
    if ((beat_ % alcoolPassivoBeats_) == 0) {
      if (alcool_ > alcoolMin_ && alcool_ < alcoolMax_) {
        alcool_ += alcoolPassivo_;
      }
    }
    /*
      Se for pingo_ normal, a sede_ aumenta.
    */
    if (pingo_ != null) {
      if ((beat_ % sedePassivoBeats_) == 0)
      if (sede_ >= sedeMin_ && sede_ < sedeMax_) {
        sede_ += sedePassivo_;
      }
    } /* Se for pingo_ bebendo agua, a sede_ deve diminuir.  */
    else if (pingoBebendoAgua_ != null) {
      if ((beat_ % sedePassivoBeats_) == 0)
        if (sede_ <= sedeMax_ && sede_ > sedeMin_) {
          sede_ -= sedePassivo_+1;
        }
    } /* Se for o pingo_ normal, a fome_ aumenta.  */
    if (pingo_ != null) {
      if ((beat_ % fomePassivoBeats_) == 0)
        if (fome_ >= fomeMin_ && fome_ < fomeMax_)
          fome_ += fomePassivo_;
    } /* Não achei necessidade (ainda) de criar um código apenas para pingo_
         comendo cenoura, já que possuem mesmas funcionalidades.  */
    else if (pingoComendoSopaBacon_ != null || pingoComendoSopaCenoura_ != null) {
      // se for o pingo_ comendo sopa de bacon, a fome_ dele deve diminuir
      if ((beat_ % fomePassivoBeats_) == 0)
        if (fome_ <= fomeMax_ && fome_ > fomeMin_) {
          fome_ -= fomePassivo_;
        }
    } else if(pingoBebendoLeite_ != null) {
     //Se for o pingo_ bebendo leite, a fome_ dele deve diminuir
      if ((beat_ % fomePassivoBeats_) == 0)
        if (fome_ <= fomeMax_ && fome_ > fomeMin_) {
          fome_ -= fomePassivo_;
        }
    }
    /*
       if ((beat % fome_passivo_beats_) == 0)
       if (fome_ > fome_min_)
       fome_ += fome_passivo_;

       if ((beat % humorPassivoBeats_) == 0)
       if (humor_ > humorMin_)
       humor_ += humorPassivo_;

       if ((beat % socialPassivoBeats_) == 0)
       if (social_ > socialMin_)
       social_ += socialPassivo_;

       if ((beat % higienePassivoBeats_) == 0)
       if (higiene_ > higieneMin_)
       higiene_ += higienePassivo_;
    /*Após a matricula
    if ((beat % estudoPassivo_beats_) == 0)
    if (estudo_ > estudoMin_)
    estudo_ += estudoPassivo_;


    if ((beat % saudePassivoBeats_) == 0)
    if (saude_ > saudeMin_)
    saude_ += saudePassivo_;*/
    //fim dos passivos atributos
  }


  public void mudaAviso() {
    imprime(avisos);
    Aviso aux;
    if(elementos.hasNext()){
      aux = elementos.next();
    } else {
      elementos = avisos.listIterator();
      aux = elementos.next();
    }
    while(aux.getAviso().isEmpty() && aux!=avisoAtual) {
      if(!elementos.hasNext()) {
        elementos = avisos.listIterator();
      }
      aux = elementos.next();
    }
    if(aux.getAviso().isEmpty()){
      avisoAtual = new Aviso("Sem Avisos");
      //aviso_status_bar = "Sem avisos";
    } else if(!aux.getAviso().isEmpty()) {
      avisoAtual = aux;
      //aviso_status_bar = aux.getAviso();
    }
    atualizaAviso();
    avisoAtual = aux;
    imprime(avisos);
  }

  public void atualizaAviso() {
    //    int n = statbar_iface.roots().iterator().next().childAt(0);
    //    System.out.println("childcount" + n + "\n");
    //    grp.childCount();
    /*
      statbar_iface.roots().iterator().next().childAt(0).childAt(1).childAt(1) =
          new Label(avisoAtual.getAviso()).addStyles(Styles.make(
                Style.COLOR.is(0xFFFFFFFF),
                Style.TEXT_WRAP.is(true),
                Style.HALIGN.left
                ));
    */

    Label l = (Label) rightStatbarGroup_.childAt(1);
    l.text.update(avisoAtual.getAviso());
  }

  public void removeAviso(Aviso aviso) {
    imprime(avisos);
    if(avisoAtual==aviso){
      //mudar o aviso que aparece na tela
      //elementos.remove();
      aviso.remove();//string = ""
      mudaAviso();
    }else{
      aviso.remove();//Campo String = ""
    }
    imprime(avisos);
  }

  void verificaAvisos() {
    if(fome_ >=80){
      if(!fomeAviso.getAviso().equals("Pingo esta ficando fraco!")){
        fomeAviso.setAviso("Pingo esta ficando fraco!");
        if(avisoAtual==fomeAviso)
            atualizaAviso();
        }
    //chorando
    } else if(fome_ >= 60){
      if(!fomeAviso.getAviso().equals("Pingo esta com muita fome!")){
        fomeAviso.setAviso("Pingo esta com muita fome!");
        if(avisoAtual==fomeAviso)
            atualizaAviso();
        }
    //chorando
    } else if(fome_ >= 40){
     if (!fomeAviso.getAviso().equals(""))
              removeAviso(fomeAviso);
       //triste
    } else if(fome_ >= 20){
      if (!fomeAviso.getAviso().equals(""))
              removeAviso(fomeAviso);
    } else if(fome_ >= 0){
      if(!fomeAviso.getAviso().equals("Pingo esta cheio")){
        fomeAviso.setAviso("Pingo esta cheio");
        if(avisoAtual==fomeAviso)
            atualizaAviso();
      }
    //normal
    }else if(fome_ >= -20){
      if(!fomeAviso.getAviso().equals("Pingo comeu demais e esta passando mal")){
        fomeAviso.setAviso("Pingo comeu demais e esta passando mal");
        if(avisoAtual==fomeAviso)
            atualizaAviso();
    //normal+vomitando
        }
    }
/*
    //Humor
    if(humor_ <= 0){
    humorAviso.setAviso("Pingo está mal-humorado!");
    //na tabela está "MAU HUMORADO"
    //bravo
    } else if(humor_ <= 20){
    humorAviso.setAviso("Pingo quer brincar!");
    //triste
    } else if(humor_ <= 40){
    humorAviso.remove();
    //normal
    }else if(humor_ <= 60){
    humorAviso.remove();
    //normal
    }else if(humor_ <= 80){
    humorAviso.remove();
    //normal
    } else if(humor_ <= 100){
    humorAviso.setAviso("Pingo está muito contente");
    }
    if(!humorAviso.isEmpty()  && avisos.contains(humorAviso)){
    avisos.remove(humorAviso);
    } else if(!humorAviso.isEmpty() && !avisos.contains(humorAviso)){
    avisos.add(humorAviso);
    }

    //Social
    if(social_ <= 0){
    socialAviso.setAviso("Pingo está sentindo falta de compainha");
    //chorando
    } else if(social_ <= 20){
    socialAviso.setAviso("Pingo gostaria de uma amizade");
    //triste
    } else if(social_ <= 40){
    socialAviso.remove();
    //normal
    } else if(social_ <= 60){
    socialAviso.remove();
    //normal
    }else if(social_ <= 80){
    socialAviso.remove();
    //normal
    } else if(social_ <= 100){
    socialAviso.setAviso("Pingo está muito contente");
    //normal
  }
  if(socialAviso.isEmpty()  && avisos.contains(socialAviso)){
    removeAviso(socialAviso);
  } else if(!socialAviso.isEmpty() && !avisos.contains(socialAviso)){
    avisos.add(socialAviso);
  }
  //higiene
  if(higiene_ <= 0){
    higieneAviso.setAviso("Pingo está imundo e pode ficar doente");
  } else if(higiene_ <= 20){
    higieneAviso.setAviso("Pingo está imundo e pode ficar doente");//outra ação associada
  } else {
    higieneAviso.remove();
  }
  if(higieneAviso.isEmpty() && avisos.contains(higieneAviso)){
    removeAviso(higieneAviso);
  } else if(!higieneAviso.isEmpty()  && !avisos.contains(higieneAviso)){
    avisos.add(higieneAviso);
  }
  //Estudos
  if(estudo_==-5){
    estudoAviso.setAviso("Pingo foi expulso da escola e não pode mais estudar");
  }else if(estudo_<=0){
    estudoAviso.setAviso("Pingo reprovou de ano. Ele pode tentar se matricular somente 1 vez mais");
  }else if(estudo_<=3){
    estudoAviso.setAviso("Pingo está de recuperação. Ele precisa estudar para a prova de amanhã");
  }else if(estudo_<=6){
    estudoAviso.remove();
  }else if(estudo_<=9){
    estudoAviso.setAviso("Pingo é um dos melhores alunos da classe");
  }else if(estudo_<=10){
    estudoAviso.setAviso("Pingo é o melhor aluno da escola");
  }
  if(estudoAviso.isEmpty() && avisos.contains(estudoAviso)){
    removeAviso(estudoAviso);
  } else if(!estudoAviso.isEmpty()  && !avisos.contains(estudoAviso)){
    avisos.add(estudoAviso);
  }


  //Saude
  if(saude_==-5){
    saudeAviso.setAviso("Pingo não recebeu cuidados médicos à tempo e faleceu");
  }else if(saude_<=0){
    saudeAviso.setAviso("Pingo está muito doente para receber qualquer atividade");
  }else if(saude_<=3){
    saudeAviso.setAviso("Pingo está doente!");
  }else if(saude_<=6){
    saudeAviso.remove();
  }else if(saude_<=9){
    saudeAviso.remove();
  }else if(saude_<=10){
    saudeAviso.setAviso("Pingo está com a saúde perfeita");
  }
  if(saudeAviso.isEmpty() && avisos.contains(saudeAviso)){
    removeAviso(saudeAviso);
  } else if(!estudoAviso.isEmpty() && !avisos.contains(saudeAviso)){
    avisos.add(saudeAviso);
  }


  //Disciplina
  if(disciplina_==-5){
    disciplinaAviso.setAviso("Pingo é preso praticando vandalismo");
    //pingo sai 1 dia do cenário;
  }else if(disciplina_<=0){
    disciplinaAviso.remove();
    //pingo não executa nenhuma atividade de estudo ou higiene
  }else if(disciplina_<=3){
    disciplinaAviso.remove();
    //50% de chance de não executar atividade de estudo ou higiene
  }else if(disciplina_<=6){
    disciplinaAviso.remove();
  }else if(disciplina_<=9){
    disciplinaAviso.remove();
    //pingo solicita permissão para estudar(caso tenha estudo 3 ou menos)
    //ou limpar o quarto caso tenha mais de 6 cocôs pelo quarto
    if(estudo_<=3){
      //solicita estudar
    }
  }else if(disciplina_<=10){
    disciplinaAviso.remove();
    // pingo automaticamente estuda caso tenha estudo 3 ou menos
    //ou limpa o quarto caso tenha mais de 6 cocôs pelo quarto.
    if(estudo_<=3){
      // "estudar();"
    }
  }
  if(disciplinaAviso.isEmpty() && avisos.contains(disciplinaAviso)){
    removeAviso(disciplinaAviso);
  } else if(!disciplinaAviso.isEmpty()  && !avisos.contains(disciplinaAviso)){
    avisos.add(disciplinaAviso);
  }

  */
    //Alcool
    if (alcool_<=0){
      if (!alcoolAviso.getAviso().equals(""))
              removeAviso(alcoolAviso);
      //normal
    } else if (alcool_ <= 3) {
       if (!alcoolAviso.getAviso().equals(""))
              removeAviso(alcoolAviso);
      //normal
    } else if(alcool_<=6) {
        if (!alcoolAviso.getAviso().equals("Pingo esta bebado")) {
          alcoolAviso.setAviso("Pingo esta bebado");
          if(avisoAtual==alcoolAviso)
            atualizaAviso();
        }
        //bebado
      } else if(alcool_<=9) {
        if(!alcoolAviso.getAviso().equals("Pingo esta muito bebado para executar certas atividades")){
         alcoolAviso.setAviso("Pingo esta muito bebado para executar certas atividades");
          if(avisoAtual==alcoolAviso)
         atualizaAviso();
        }
        //bebado + vomitando
      } else if(alcool_<=10) {
        if(!alcoolAviso.getAviso().equals("Pingo entrou em coma alcoólico")) {
          alcoolAviso.setAviso("Pingo entrou em coma alcoólico");
          if(avisoAtual==alcoolAviso)
          atualizaAviso();
        }
        //em coma
      }
  }


  void piscar() {
    //Pingo piscando
    if(pingoPiscando_ != null && pingoDormindo_ == null){
      pingoPiscando_.detatch(layer_);
      pingoPiscando_ = null;
      pingo_ = new Pingo(layer_, width() / 2, height() / 2);
    } else if(pingo_ != null && pingoDormindo_==null){
      pingo_.detatch(layer_);
      pingo_ = null;
      pingoPiscando_ = new PingoPiscando(layer_, width() / 2, height() / 2);
    }

    /*
       if(pingo != null && beat>6){
       System.out.println("Pingo Normal");
       pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
       pingo.detatch(layer);
       pingo = null;
    //pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
    System.out.println("Pingo Piscando");
    }
     */
    /*
    //Pingo piscando
    r_ = _rando.getInRange(1,11);//de 1 a 10
    if(pingo!=null && pingo.getTraversed() && beat/((int) Math.max(beatsCoelhoSegundo_*60.*60.*2.,1))%r_==0){
    //System.out.println(r_ +" horas");
    pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
    pingo.detatch(layer);
    pingo = null;
    }else if(pingopiscando!=null && pingopiscando.getTraversed()){
    //System.out.println(r_ +" horas");
    System.out.println(pingopiscando.getTraversed());
    pingo = new Pingo(layer, width() / 2, height() / 2);
    pingopiscando.detatch(layer);
    pingopiscando = null;
    }/*
    if(pingopiscando!=null){
    System.out.println("FALSE");
    if(pingopiscando.getTraversed()){
    System.out.println("TRUE");
    }
    }*/
  }

  public void imprime(List<Aviso> l) {
      ListIterator<Aviso> i = l.listIterator();
      Aviso a;
      //a= i.next();
      while(i.hasNext()) {
         a = i.next();
      }
  }
}//fim da Classe Pet

/*
   removeAviso (cara)
   {
// compara com os ponteiros importantes tipo aviso_status_bar se  o cara esta sendo apontado por algum
// em caso afirmativo, seta o ponteiro em questao para o proximo elemento valido
//         remove o cara da lista
// em caso negativo,
//    simplesmente remove da lista
}*/
