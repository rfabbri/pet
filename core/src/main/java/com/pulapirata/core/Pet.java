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
  // obs: global class member variables are always appended with a trailing underscore
  /*
    Informacoes que aparecem no topo.
  */
  protected  static final String STAT_ALERT_1 = "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
  protected  static final String STAT_FILLER_1 = "Idade: %d%s\n Sede: %d/%d\n";
  protected  static final String STAT_FILLER_2 = "\nNutricao: %d/%d\n Alcool: %d/%d";

  /*-------------------------------------------------------------------------------*/

  /*
    Informacoes referentes as instancias criadas.
  */
  private ImageLayer bgLayer_ = null; 
  private Image bgImageDay_, bgImageNight_;
  private GroupLayer layer_;
  private Sound somArroto_ = assets().getSound("pet/sprites/arroto_01");
  private Sound somSoluco_ = assets().getSound("pet/sprites/soluco_01");
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
  //protected PetJson petJson;
  //private Sprite sprite;
  /*-------------------------------------------------------------------------------*/

  protected Group mainStat_;
  private Group rightStatbarGroup_;
  private Image exclamacao_;
  private TableLayout rightPartLayout_;

  // FIXME graphics.width() is weird in html, not respecting #playn-root
  // properties.
  /*
    Posicao do pingo na tela.
  */ 
  public int width() { return 480; }
  public int height() { return 800; }
  /*-------------------------------------------------------------------------------*/

  public static final int UPDATE_RATE = 100; // ms 
  protected final Clock.Source clock_ = new Clock.Source(UPDATE_RATE);
  
  private int beat_ = 0; // number of updates
   // the following is not static so that we can dynamically speedup the game if desired
  private int beatsCoelhoDia_ = 600; // beats por 1 coelho dia.
  private double beatsCoelhoSegundo_ = (double)beatsCoelhoDia_/(24.*60.*60.); 
  public int idade_coelhohoras() { return (int)((float)beat_ / ((float)beatsCoelhoDia_/24f)); }
  public int idade_coelhodias() { return beat_ / beatsCoelhoDia_; }

  /*
    Informacoes referente aos atributos do pingo.
  */
  private Interface iface_, statbarIface_;
  private int sede_ = 5;
  private int sedePassivo_ = 1;
  private int sedePassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60*60,1);
  private int sedeMax_ = 10;
  private int sedeMin_ = 0;
  //PetJson.parseJson("pet/jsons/atributos.json","fome_");


  //private int nutricao_ = PetJson.readJson("pet/jsons/atributos.json","fome").fome();
  private int nutricao_ = 50;
  private int nutricaoPassivo_ = -5;
  private int nutricaoPassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60./4.,1);
  private int nutricaoMax_ = 120;
  private int nutricaoMin_ = -20;

  private int alcool_ = 3;
  private int alcoolPassivo_ = -1;
  private int alcoolPassivoBeats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.,1);
  private int alcoolMax_ = 10;
  private int alcoolMin_ = 0;
  /*-------------------------------------------------------------------------------*/
  private boolean dormir_ = false;
  private Stylesheet petSheet_;

  /*
    Funcao para setar as informacoes no topo corretamente.
  */
  public String idade_coelhodias_str1() { 
    if (idade_coelhodias() == 0)
      return String.format(STAT_FILLER_1, idade_coelhohoras(), "h", sede_, sedeMax_);    
    else
      return String.format(STAT_FILLER_1, idade_coelhodias(), " dias", sede_, sedeMax_);    
  }
  public String idade_coelhodias_str2() { 
      return String.format(STAT_FILLER_2, nutricao_ , nutricaoMax_, alcool_, alcoolMax_);
  }
  private Aviso aviso_atual = new Aviso("Bem vindo ao jogo Pet");
  private Aviso nutricao_aviso = new Aviso();
  private Aviso humor_aviso = new Aviso();
  private Aviso social_aviso = new Aviso();
  private Aviso higiene_aviso = new Aviso();
  private Aviso estudo_aviso = new Aviso();
  private Aviso saude_aviso = new Aviso();
  private Aviso disciplina_aviso = new Aviso();
  private Aviso alcool_aviso = new Aviso();
  private List<Aviso> avisos = new ArrayList<Aviso>();//List que conterá os avisos
  //private String aviso_status_bar="ola"; 
  private ListIterator<Aviso> elementos;

  private int humor_ = 30;
  private int humor_passivo_ = -5;
  private int humor_passivo_beats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60./3.,1); //20 min
  private int humor_max_ = 120;
  private int humor_min_ = -20;

  private int social_ = 30;
  private int social_passivo_ = -5;
  private int social_passivo_beats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.*2./3.,1); //40min
  private int social_max_ = 120;
  private int social_min_ = -20;

  private int higiene_ = 30;
  private int higiene_passivo_ = -5;
  private int higiene_passivo_beats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60./2.,1); //30 min
  private int higiene_max_ = 120;
  private int higiene_min_ = -20;

  private int estudo_ = 0;
  private int estudo_passivo_ = -1;
  //private int estudo_passivo_beats_ = ;//? por dia a partir da matricula (colocar um valor inicial depois da matricula mudar)
  //(int) Math.max(beatsCoelhoSegundo_*60.*60.*24.,1); //dia
  private int estudo_max_ = 10;
  private int estudo_min_ = -5;

  private int saude_ = 5;
  private int saude_passivo_ = -1;
  private int saude_passivo_beats_ = (int) Math.max(beatsCoelhoSegundo_*60.*60.*24.,1);//? por idade (em dias?)
  private int saude_max_ = 10;
  private int saude_min_ = -5;

  private int disciplina_ = 0;
  private int disciplina_max_ = 10;
  private int disciplina_min_ = -5;

  private final Randoms _rando = Randoms.with(new Random());//Para gerar numeros aleatorios
  private int r;//excluir depois

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
    TableLayout statbar_layout = new TableLayout(
      COL.minWidth(mainStatWidth).alignLeft().fixed(), 
      COL.minWidth(30).stretch()).gaps(mae,mae).alignTop();

//    AxisLayout statbar_layout = new AxisLayout.horizontal().add(
//      COL.minWidth(250).alignLeft().fixed(), 
//      COL.minWidth(30).stretch()).gaps(mae,mae).alignTop();
    // the left status plus is the left column
    // the (!) icon plust the right text is the right column

    rightPartLayout_ = new TableLayout(COL.fixed().minWidth(30), COL.alignLeft()).gaps(mae,mae).alignTop();

    exclamacao_ = assets().getImage("pet/images/exclamacao.png");


    // Cria um grupo para os caras da esquerda
    // Basicamente 2 labels: nome grandao e indicadores em fonte menor

    String age1 = idade_coelhodias_str1(); 
    String age2 = idade_coelhodias_str2();
 
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
          new Label(aviso_atual.getAviso()).addStyles(Styles.make(
              Style.COLOR.is(0xFFFFFFFF),
              Style.TEXT_WRAP.is(true),
              Style.HALIGN.left,
              Style.FONT.is(PlayN.graphics().createFont("EarthboundZero", Font.Style.PLAIN, 16))
              ))
          );

    Group statbar = new Group (statbar_layout).add (
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
  private void make_background_init() {
    bgImageDay_ = assets().getImage("pet/images/cenario_quarto.png");
    bgImageNight_ = assets().getImage("pet/images/cenario_quarto_noite.png");
    bgLayer_ = graphics().createImageLayer(bgImageDay_);
    layer_.addAt(bgLayer_, 0, 120); //janela do quarto do pingo
  }

  private void set_background_day() {
    bgLayer_.setImage(bgImageDay_);
  }

  private void set_background_night() {
    bgLayer_.setImage(bgImageNight_);
  }


  //--------------------------------------------------------------------------------
  /*
    Funcao responsavel por criar os butoes, estes sao colocados em um ArraList
  */
  private void make_buttons() {
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

    final int[][] topleft_secondary = new int [][] {
      {0,0},
        {120,0},
        {240,0},
        {360,0},
    };
    /*-------------------------------------------------------------------------------*/

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
        /*
          Acesso todos os butoes primarios (b) e secundarios (s) de forma a criar
          cada evento. Feito isso, apos instancia uma classe/sprite referente a
          aquela acao.  A funcao "detatch" eh utilziada para remover o sprite de
          tela, e em seguida seta-se a classe pingo para null. Provavelmente tera
          de mudar, pois eh necessario que tudo seja em funcao de atributos.
        */
        if(b == 0 && s == 0) sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento comer sopa de cenoura, cria um novo pingoComendoSopaBacon_
            pingoComendoSopaCenoura_ = new PingoComendoSopaCenoura(layer_, width()/2, height()/2);
            if (pingo_ != null) {
              pingo_.detatch(layer_);
              pingo_ = null;
            } else if (pingoTriste_ != null) {
              pingoTriste_.detatch(layer_);
              pingoTriste_ = null;
            } else if (pingoChorando_ != null) {
              pingoChorando_.detatch(layer_);
              pingoChorando_ = null;
            }            
           //fome_ = fomeMax_;
          }
        });


        if(b == 0 && s == 1) sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento comer sopa de bacon, cria um novo pingoComendoSopaBacon_
            pingoComendoSopaBacon_ = new PingoComendoSopaBacon(layer_, width()/2, height()/2);
            if (pingo_ != null) {
              pingo_.detatch(layer_);
              pingo_ = null;
            }
            //fome_ = fomeMax_;
          }
        });


        if(b == 0 && s == 2)sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento beber agua, cria um novo pingoBebendoAgua_
            pingoBebendoAgua_ = new PingoBebendoAgua(layer_, width()/2, height()/2);
            if (pingo_ != null) {
              pingo_.detatch(layer_);
              pingo_ = null;
            }
            //sede_ = sedeMax_;
          }
        });

        if(b == 0 && s == 3)sbut.clicked().connect(new UnitSlot(){
          public void onEmit(){//Atravez do evento beber leite, cria um novo pingoBebendoLeite_
            pingoBebendoLeite_ = new PingoBebendoLeite(layer_, width()/2, height()/2);
            if (pingo_ != null) {
              pingo_.detatch(layer_);
              pingo_ = null;
            }
            //fome_ = fomeMax_;
          }
        });

        if (b == 6 /* diversao */ && s == 0 /* licor */) 
          sbut.clicked().connect(new UnitSlot() {
            public void onEmit() {	
              alcool_ = alcoolMax_; // TODO modificar de acordo com folha
            }
          });
        }
      /*-------------------------------------------------------------------------------*/

      but.selected().map(new Function <Boolean,Icon>() {
        public Icon apply (Boolean selected) {
          if (selected)
             return Icons.image(img_butt_apertado.get(b_final));
          else
             return Icons.image(img_butt_solto.get(b_final));
        }
      }).connectNotify(but.icon.slot());
      // all secondary buttons are added; toggle visibility only
      root_.add(AbsoluteLayout.at(sbuttons.get(b_final), 0, 0, width(), 120));
      sbuttons.get(b_final).setVisible(false);
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
  /*-------------------------------------------------------------------------------*/

  @Override
  public void init() {
    System.out.println("passivo is " + alcoolPassivoBeats_);
    System.out.println("coelho seg " + beatsCoelhoSegundo_);

    // create a group layer_ to hold everything
    layer_ = graphics().createGroupLayer();
    graphics().rootLayer().add(layer_);
    petSheet_ = PetStyles.newSheet();

    // ------------------------------------------------------------------
    make_statusbar();
    make_background_init();
    make_buttons();
    // ------------------------------------------------------------------

    /*
      Removi a criacao inicial do pingo_, ja que agora isto depende da hora.
    */
    // sprites
    pingo_ = new Pingo(layer_, width() / 2, height() / 2);
//pingoChorando_  = new PingoChorando(layer_, width() / 2, height() / 2);


    // pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
    //Adicionando avisos na lista
    avisos.add(nutricao_aviso);
    avisos.add(humor_aviso);
    avisos.add(social_aviso);
    avisos.add(higiene_aviso);
    avisos.add(estudo_aviso);
    avisos.add(saude_aviso);
    avisos.add(disciplina_aviso);
    avisos.add(alcool_aviso);
    //Iniciando o ListIterator e o Aviso atual 
    elementos = avisos.listIterator(); 
    aviso_atual = elementos.next();
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
    else if(pingoChorando_ != null)
      pingoChorando_.update(delta);
    else if(pingoTriste_ != null)
      pingoTriste_.update(delta);


    /*
      Eh realizada a verificacao de todos os atributos, e tomando acoes de acordo com cada funcionalidade
    */
    if (idade_coelhodias() >= 6) {
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
      if(nutricao_ <= nutricaoMin_ && pingoComendoSopaCenoura_ != null && pingoChorando_ == null){
        nutricao_ = nutricaoMin_;
        pingoChorando_ = new PingoChorando(layer_, width() / 2, height() / 2);
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
/*	else if(pingoDormindo_!=null){
	  pingoDormindo_.detatch(layer_);
	  pingoDormindo_ = null;
	}*/
      }
      if(nutricao_ <= nutricaoMin_ && pingoComendoSopaBacon_ != null && pingo_ == null){
        nutricao_ = nutricaoMin_;
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

      if(nutricao_ <= nutricaoMin_ && pingoBebendoLeite_ != null && pingo_ == null){
        nutricao_ = nutricaoMin_;
        pingo_ = new Pingo(layer_, width() / 2, height() / 2);
        pingoBebendoLeite_.detatch(layer_);
        pingoBebendoLeite_ = null;
        somArroto_.play(); 
      }
      // FIXME estamos confundindo tempo de vida com hora de relogio.
      // Pet esta nascendo `a meia noite sempre - 0 horas
      int horaDoDia = idade_coelhohoras()-idade_coelhodias()*24;
      if (dormir_ == false && (horaDoDia >= 22 || horaDoDia <= 8)
          && idade_coelhodias() >= 1 && pingoPiscando_==null &&
          (pingo_!=null || pingoComendoSopaCenoura_ !=null
          || pingoComendoSopaBacon_ != null || pingoComendoSopaCenoura_ != null
          || pingoBebendoAgua_ != null || pingoBebendoLeite_ != null
          || pingoComa_ != null || pingoVomitando_ != null
          || pingoBebado_ != null || pingoChorando_ != null || pingoTriste_ != null)) {
        dormir_ = true;
        set_background_night();
	      System.out.println("Instanciando pingo dormindo");
      	System.out.println("Horas: " + horaDoDia);
        pingoDormindo_ = new PingoDormindo(layer_, width()/2, height()/2);
	      System.out.println("Pronto");
        if (pingo_ != null) {
          pingo_.detatch(layer_);
          pingo_ = null;
        } else if(pingoPiscando_ != null) {
          pingoPiscando_.detatch(layer_);
          pingoPiscando_ = null;
        } else if(pingoComendoSopaCenoura_ != null){
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
        } else if(pingoChorando_ != null){
          pingoChorando_.detatch(layer_);
          pingoChorando_ = null;
        } else if(pingoTriste_ != null){
          pingoTriste_.detatch(layer_);
          pingoTriste_ = null;
        }
      }
      else if (horaDoDia < 22 && horaDoDia > 8) {
        if (pingoDormindo_ != null && pingo_ == null && pingoPiscando_ == null) {
          dormir_ = false;
          set_background_day();
          pingo_ = new Pingo(layer_, width()/2, height()/2);
          pingoDormindo_.detatch(layer_);
          pingoDormindo_ = null;
          System.out.println("Horario de ficar acordado: " + horaDoDia);
        }
      }

      if (alcool_ == 10) {
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

        if (alcool_ >= 7) {
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
//ERRO COM PINGO CHORANDO
   if(pingoChorando_ == null && nutricao_ <= 20){
      pingoChorando_  = new PingoChorando(layer_, width() / 2, height() / 2);
      if(pingo_ != null){
        pingo_.detatch(layer_);
        pingo_ = null;
      } else if(pingoTriste_ != null){
        pingoTriste_.detatch(layer_);
        pingoTriste_ = null;
        }
    } else if(pingo_!= null && nutricao_ > 20 && nutricao_ <= 40){
      pingoTriste_  = new PingoTriste(layer_, width() / 2, height() / 2);     
      pingo_.detatch(layer_);
      pingo_ = null;
    }

    if(pingo_ == null && (pingoTriste_ != null || pingoChorando_ != null) && nutricao_ > 40 && nutricao_ <= 80){
      pingo_ = new Pingo(layer_, width() / 2, height() / 2);
       if(pingoTriste_ !=null){
         pingoTriste_.detatch(layer_);
         pingoTriste_ = null;
       }else if(pingoChorando_ !=null){
         pingoChorando_.detatch(layer_);
         pingoChorando_ = null;
       } 
    }
	
    // update clock and passives
    beat_++;

    Label l = (Label) mainStat_.childAt(1);
    l.text.update(idade_coelhodias_str1());
    l =  (Label) mainStat_.childAt(2); 
    l.text.update(idade_coelhodias_str2());

    if (iface_ != null)
      iface_.update(delta);

    if (statbarIface_ != null)
      statbarIface_.update(delta);

    passivoAtributos();
    verifica_avisos();
     
    if (beat_ % alcoolPassivoBeats_ ==0){ // a cada hora 
      muda_aviso();
    }
    

    if(beat_ % (2*alcoolPassivoBeats_) ==0){
      piscar();
    }
    //System.out.println(_rando.getInRange(1,10));
  } //fim do update

  public void passivoAtributos() {
    //inicio dos passivos dos atributos
    if ((beat_ % alcoolPassivoBeats_) == 0) {
      if (alcool_ > alcoolMin_) {
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
    } /* Se for o pingo_ normal, a nutricao_ diminui.  */    
    if (pingo_ != null || pingoTriste_ != null || pingoChorando_ != null) {
      if ((beat_ % nutricaoPassivoBeats_) == 0)
        if (nutricao_ >= nutricaoMin_ && nutricao_ < nutricaoMax_)
          nutricao_ += nutricaoPassivo_;
    } /* Não achei necessidade (ainda) de criar um código apenas para pingo_
         comendo cenoura, já que possuem mesmas funcionalidades.  */
    else if (pingoComendoSopaBacon_ != null || pingoComendoSopaCenoura_ != null) {
      // se for o pingo_ comendo sopa de bacon, a fome_ dele deve diminuir
      if ((beat_ % nutricaoPassivoBeats_) == 0)
        if (nutricao_ <= nutricaoMax_ && nutricao_ > nutricaoMin_) {
          nutricao_ -= nutricaoPassivo_;
        }
    } else if(pingoBebendoLeite_ != null) {
     //Se for o pingo_ bebendo leite, a fome_ dele deve diminuir
      if ((beat_ % nutricaoPassivoBeats_) == 0)
        if (nutricao_ <= nutricaoMax_ && nutricao_ > nutricaoMin_) {
          nutricao_ -= nutricaoPassivo_; 
        }
    }
    /*
       if ((beat % fome_passivo_beats_) == 0)
       if (fome_ > fome_min_)
       fome_ += fome_passivo_;

       if ((beat % humor_passivo_beats_) == 0)
       if (humor_ > humor_min_)
       humor_ += humor_passivo_;

       if ((beat % social_passivo_beats_) == 0)
       if (social_ > social_min_)
       social_ += social_passivo_;

       if ((beat % higiene_passivo_beats_) == 0)
       if (higiene_ > higiene_min_)
       higiene_ += higiene_passivo_;
    /*Após a matricula
    if ((beat % estudo_passivo_beats_) == 0)
    if (estudo_ > estudo_min_)
    estudo_ += estudo_passivo_;


    if ((beat % saude_passivo_beats_) == 0)
    if (saude_ > saude_min_)
    saude_ += saude_passivo_;*/
    //fim dos passivos atributos
  }


  public void muda_aviso() {
    System.out.println("muda_aviso (inicio)");
    imprime(avisos);
    Aviso aux;
    if(elementos.hasNext()){
	    aux = elementos.next();
    }else{
	    elementos = avisos.listIterator();
	    aux = elementos.next();
    }
    while(aux.getAviso().isEmpty() && aux!=aviso_atual){
	    if(!elementos.hasNext()){
	      elementos = avisos.listIterator(); 
	    }
	      aux = elementos.next();
    }
    if(aux.getAviso().isEmpty()){
	    aviso_atual = new Aviso("Sem Avisos");
	    //aviso_status_bar = "Sem avisos"; 
    } else if(!aux.getAviso().isEmpty()){
	      aviso_atual = aux;
	      //aviso_status_bar = aux.getAviso();
    }
    atualiza_aviso();
    aviso_atual = aux;
    imprime(avisos);
    System.out.println("muda_aviso (final)");
  } 

  public void atualiza_aviso() {
    //    int n = statbar_iface.roots().iterator().next().childAt(0);
    //    System.out.println("childcount" + n + "\n");
    //    grp.childCount();
    /*
      statbar_iface.roots().iterator().next().childAt(0).childAt(1).childAt(1) = 
          new Label(aviso_atual.getAviso()).addStyles(Styles.make(
                Style.COLOR.is(0xFFFFFFFF),
                Style.TEXT_WRAP.is(true),
                Style.HALIGN.left
                ));
    */

    Label l = (Label) rightStatbarGroup_.childAt(1);
    l.text.update(aviso_atual.getAviso());
  }

  public void remove_aviso(Aviso aviso) {
    System.out.println("remove_aviso (inicio)");
    imprime(avisos);
    if(aviso_atual==aviso){
      //mudar o aviso que aparece na tela
      //elementos.remove();
      aviso.remove();//string = ""
      muda_aviso();
    }else{
      aviso.remove();//Campo String = ""
    }
    imprime(avisos);
    System.out.println("remove_aviso (final)");
  }
 
  void verifica_avisos() {
    if(nutricao_ <= 0){
      if(!nutricao_aviso.getAviso().equals("Pingo esta ficando fraco!")){
        nutricao_aviso.setAviso("Pingo esta ficando fraco!");
        if(aviso_atual==nutricao_aviso)
            atualiza_aviso();
        } 
    //chorando
    } else if(nutricao_ <= 20){
      if(!nutricao_aviso.getAviso().equals("Pingo esta com muita fome!")){
        nutricao_aviso.setAviso("Pingo esta com muita fome!");
        if(aviso_atual==nutricao_aviso)
            atualiza_aviso();
        } 
    //chorando
    } else if(nutricao_ <= 40){
     if (!nutricao_aviso.getAviso().equals(""))	
    	  remove_aviso(nutricao_aviso);
       //triste
    } else if(nutricao_ <= 60){
      if (!nutricao_aviso.getAviso().equals(""))	
    	  remove_aviso(nutricao_aviso);   
    } else if(nutricao_ <= 80){
      if(!nutricao_aviso.getAviso().equals("Pingo esta cheio")){
        nutricao_aviso.setAviso("Pingo esta cheio");
        if(aviso_atual==nutricao_aviso)
            atualiza_aviso();
      }
    //normal
    }else if(nutricao_ > 80){
      if(!nutricao_aviso.getAviso().equals("Pingo comeu demais e esta passando mal")){
        nutricao_aviso.setAviso("Pingo comeu demais e esta passando mal");
        if(aviso_atual==nutricao_aviso)
            atualiza_aviso();
    //normal+vomitando
        }
    }
/*
    //Humor 
    if(humor_ <= 0){
    humor_aviso.setAviso("Pingo está mal-humorado!");
    //na tabela está "MAU HUMORADO"
    //bravo
    } else if(humor_ <= 20){
    humor_aviso.setAviso("Pingo quer brincar!");
    //triste
    } else if(humor_ <= 40){
    humor_aviso.remove();
    //normal
    }else if(humor_ <= 60){
    humor_aviso.remove();
    //normal
    }else if(humor_ <= 80){
    humor_aviso.remove();
    //normal
    } else if(humor_ <= 100){
    humor_aviso.setAviso("Pingo está muito contente");
    }
    if(!humor_aviso.isEmpty()  && avisos.contains(humor_aviso)){
    avisos.remove(humor_aviso);
    } else if(!humor_aviso.isEmpty() && !avisos.contains(humor_aviso)){
    avisos.add(humor_aviso);
    }

    //Social
    if(social_ <= 0){
    social_aviso.setAviso("Pingo está sentindo falta de compainha");
    //chorando
    } else if(social_ <= 20){
    social_aviso.setAviso("Pingo gostaria de uma amizade");
    //triste
    } else if(social_ <= 40){
    social_aviso.remove();
    //normal
    } else if(social_ <= 60){
    social_aviso.remove();
    //normal
    }else if(social_ <= 80){
    social_aviso.remove();
    //normal
    } else if(social_ <= 100){
    social_aviso.setAviso("Pingo está muito contente");
    //normal
  }
  if(social_aviso.isEmpty()  && avisos.contains(social_aviso)){
    remove_aviso(social_aviso);
  } else if(!social_aviso.isEmpty() && !avisos.contains(social_aviso)){
    avisos.add(social_aviso);
  }
  //higiene
  if(higiene_ <= 0){
    higiene_aviso.setAviso("Pingo está imundo e pode ficar doente");
  } else if(higiene_ <= 20){
    higiene_aviso.setAviso("Pingo está imundo e pode ficar doente");//outra ação associada
  } else {
    higiene_aviso.remove();
  }
  if(higiene_aviso.isEmpty() && avisos.contains(higiene_aviso)){
    remove_aviso(higiene_aviso);
  } else if(!higiene_aviso.isEmpty()  && !avisos.contains(higiene_aviso)){
    avisos.add(higiene_aviso);
  }
  //Estudos
  if(estudo_==-5){
    estudo_aviso.setAviso("Pingo foi expulso da escola e não pode mais estudar");
  }else if(estudo_<=0){
    estudo_aviso.setAviso("Pingo reprovou de ano. Ele pode tentar se matricular somente 1 vez mais");
  }else if(estudo_<=3){
    estudo_aviso.setAviso("Pingo está de recuperação. Ele precisa estudar para a prova de amanhã");
  }else if(estudo_<=6){
    estudo_aviso.remove();
  }else if(estudo_<=9){
    estudo_aviso.setAviso("Pingo é um dos melhores alunos da classe");
  }else if(estudo_<=10){
    estudo_aviso.setAviso("Pingo é o melhor aluno da escola");
  }
  if(estudo_aviso.isEmpty() && avisos.contains(estudo_aviso)){
    remove_aviso(estudo_aviso);
  } else if(!estudo_aviso.isEmpty()  && !avisos.contains(estudo_aviso)){
    avisos.add(estudo_aviso);
  } 


  //Saude
  if(saude_==-5){
    saude_aviso.setAviso("Pingo não recebeu cuidados médicos à tempo e faleceu");
  }else if(saude_<=0){
    saude_aviso.setAviso("Pingo está muito doente para receber qualquer atividade");
  }else if(saude_<=3){
    saude_aviso.setAviso("Pingo está doente!");
  }else if(saude_<=6){
    saude_aviso.remove();
  }else if(saude_<=9){
    saude_aviso.remove();
  }else if(saude_<=10){
    saude_aviso.setAviso("Pingo está com a saúde perfeita");
  }
  if(saude_aviso.isEmpty() && avisos.contains(saude_aviso)){
    remove_aviso(saude_aviso);
  } else if(!estudo_aviso.isEmpty() && !avisos.contains(saude_aviso)){
    avisos.add(saude_aviso);
  } 


  //Disciplina
  if(disciplina_==-5){
    disciplina_aviso.setAviso("Pingo é preso praticando vandalismo");
    //pingo sai 1 dia do cenário; 
  }else if(disciplina_<=0){
    disciplina_aviso.remove();
    //pingo não executa nenhuma atividade de estudo ou higiene
  }else if(disciplina_<=3){
    disciplina_aviso.remove();	
    //50% de chance de não executar atividade de estudo ou higiene
  }else if(disciplina_<=6){
    disciplina_aviso.remove();
  }else if(disciplina_<=9){
    disciplina_aviso.remove();	
    //pingo solicita permissão para estudar(caso tenha estudo 3 ou menos)
    //ou limpar o quarto caso tenha mais de 6 cocôs pelo quarto
    if(estudo_<=3){
      //solicita estudar
    }
  }else if(disciplina_<=10){
    disciplina_aviso.remove();
    // pingo automaticamente estuda caso tenha estudo 3 ou menos
    //ou limpa o quarto caso tenha mais de 6 cocôs pelo quarto.
    if(estudo_<=3){
      // "estudar();"
    }
  }
  if(disciplina_aviso.isEmpty() && avisos.contains(disciplina_aviso)){
    remove_aviso(disciplina_aviso);
  } else if(!disciplina_aviso.isEmpty()  && !avisos.contains(disciplina_aviso)){
    avisos.add(disciplina_aviso);
  } 

  */
    //Alcool
    if (alcool_<=0){
      if (!alcool_aviso.getAviso().equals(""))
     	 remove_aviso(alcool_aviso);
      //normal
    } else if (alcool_ <= 3) {
       if (!alcool_aviso.getAviso().equals(""))	
    	  remove_aviso(alcool_aviso);
      //normal
    } else if(alcool_<=6) {
        if (!alcool_aviso.getAviso().equals("Pingo esta bebado")) {
          alcool_aviso.setAviso("Pingo esta bebado");
          if(aviso_atual==alcool_aviso)
            atualiza_aviso();
        }
        //bebado
      } else if(alcool_<=9) {
        if(!alcool_aviso.getAviso().equals("Pingo esta muito bebado para executar certas atividades")){     
         alcool_aviso.setAviso("Pingo esta muito bebado para executar certas atividades");
          if(aviso_atual==alcool_aviso)
         atualiza_aviso();
        }
        //bebado + vomitando
      } else if(alcool_<=10) {
        if(!alcool_aviso.getAviso().equals("Pingo entrou em coma alcoólico")) {
          alcool_aviso.setAviso("Pingo entrou em coma alcoólico");
          if(aviso_atual==alcool_aviso)
          atualiza_aviso();
        }
        //em coma
      }
  }


  void piscar() {
    //Pingo piscando
       if(pingoPiscando_ != null && pingoDormindo_ == null){
         System.out.println("Pingo Piscando");
         pingoPiscando_.detatch(layer_);
         pingoPiscando_ = null;
         pingo_ = new Pingo(layer_, width() / 2, height() / 2);
         System.out.println("Pingo Normal");
       } else if(pingo_ != null && pingoDormindo_==null){
         System.out.println("Pingo Normal");
         pingo_.detatch(layer_);
         pingo_ = null;
         pingoPiscando_ = new PingoPiscando(layer_, width() / 2, height() / 2);
         System.out.println("Pingo Piscando");
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
    r = _rando.getInRange(1,11);//de 1 a 10 
    if(pingo!=null && pingo.getTraversed() && beat/((int) Math.max(beatsCoelhoSegundo_*60.*60.*2.,1))%r==0){
    //System.out.println(r +" horas");
    pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
    pingo.detatch(layer);
    pingo = null;
    }else if(pingopiscando!=null && pingopiscando.getTraversed()){
    //System.out.println(r +" horas");
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
      System.out.println("Lista de Avisos: ");
      ListIterator<Aviso> i = l.listIterator();
      Aviso a;
      //a= i.next();
      while(i.hasNext()) {
         a = i.next();
         System.out.print("   " + a.getAviso()); 
         //a=i.next();  
      }
     System.out.println(" ");
  } 

}//fim da Classe Pet

/*
   remove_aviso (cara)
   {
// compara com os ponteiros importantes tipo aviso_status_bar se  o cara esta sendo apontado por algum
// em caso afirmativo, seta o ponteiro em questao para o proximo elemento valido
// 	remove o cara da lista
// em caso negativo,
//    simplesmente remove da lista
}*/
