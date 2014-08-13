package com.pulapirata.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
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
import com.pulapirata.core.utils.PetJson;

// TODO: we need a generic sprite class; or the layer could automatically update
// them

import react.Function;
import react.UnitSlot;
import react.Slot;

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

import static tripleplay.ui.layout.TableLayout.COL;

import playn.core.Json;

public class Pet extends Game.Default {
  /*
    Informacoes que aparecem no topo.
  */
  protected  static final String STAT_ALERT_1 = "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
  protected  static final String STAT_FILLER_1 = "Idade: %d %s\nSede: %d/%d\nFome: %d/%d\nAlcool: %d/%d";
  /*-------------------------------------------------------------------------------*/

  /*
    Informacoes referentes as instancias criadas.
  */
  private GroupLayer layer;
  private Json json;
  private Sound somArroto = assets().getSound("pet/sprites/arroto_01");
  private Sound somSoluco = assets().getSound("pet/sprites/soluco_01");
  protected Pingo pingo = null;
  protected PingoMorto pingomorto = null;
  protected PingoVomitando pingovomitando = null;
  protected PingoBebado pingobebado = null;
  protected PingoComa pingocoma = null;
  protected PingoBebendoAgua pingoBebendoAgua = null;
  protected PingoBebendoLeite pingoBebendoLeite = null;
  protected PingoComendoSopaBacon pingoComendoSopaBacon = null;
  protected PingoComendoSopaCenoura pingoComendoSopaCenoura = null;
  protected PingoPiscando pingoPiscando = null;
  protected PingoDormindo pingoDormindo = null;
  //protected PetJson petJson;
  //private Sprite sprite;
  /*-------------------------------------------------------------------------------*/

  protected Group main_stat_;
  // FIXME graphics.width() is weird in html, not respecting #playn-root
  // properties.
  /*
    Posicao do pingo na tela.
  */ 
  public int width() { return 480; }
  public int height() { return 800; }
  /*-------------------------------------------------------------------------------*/

  public static final int UPDATE_RATE = 100; // ms 
  protected final Clock.Source _clock = new Clock.Source(UPDATE_RATE);
  
  
  private int beat = 0; // number of updates
   // the following is not static so that we can dynamically speedup the game if desired
  private int beats_coelhodia = 600; // beats por 1 coelho dia.
  private double beats_coelhosegundo = (double)beats_coelhodia/(24.*60.*60.); 

  public int idade_coelhohoras() { return (int)((float)beat / ((float)beats_coelhodia/24f)); }
  public int idade_coelhodias() { return beat / beats_coelhodia; }

  /*
    Informacoes referente aos atributos do pingo.
  */
  private Interface iface, statbar_iface;
  private int sede = 5;
<<<<<<< HEAD
  private int sede_passivo = 1;
  private int sede_passivo_beats = (int) Math.max(beats_coelhosegundo*60*60,1);
  private int sede_max = 10;
  private int sede_min = 0;

  private int fome = 30;
  private int fome_passivo = 1;
  private int fome_passivo_beats = (int) Math.max(beats_coelhosegundo*60*60,1);
  private int fome_max = 120;
  private int fome_min = -20;

  private int alcool_ = 3;
  private int alcool_passivo_ = -1;
  private int alcool_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.,1);
  private int alcool_max_ = 10;
  private int alcool_min_ = 0;

=======
  private int sedePassivo = 1;
  private int sedePassivoBeats = (int) Math.max(beats_coelhosegundo*60*60,1);
  private int sedeMax = 10;
  private int sedeMin = 0;
  //PetJson.parseJson("pet/jsons/atributos.json","fome");
  private float fome = PetJson.parseJson("pet/jsons/atributos.json","fome");
  //private int fome = 20;
  private int fomePassivo = 10;
  private int fomePassivoBeats = (int) Math.max(beats_coelhosegundo*60*60,1);
  private int fomeMax = 120;
  private int fomeMin = -20;

  private int alcool = 3;
  private int alcoolPassivo = -1;
  private int alcoolPassivoBeats = (int) Math.max(beats_coelhosegundo*60.*60.,1);
  private int alcoolMax = 10;
  private int alcoolMin = 0;
>>>>>>> Commitando antes da formatação
  /*-------------------------------------------------------------------------------*/
  private boolean dormir = false;
  private Stylesheet petSheet;

  /*
    Funcao para setar as informacoes no topo corretamente.
  */
  public String idade_coelhodias_str() { 
    if (idade_coelhodias() == 0)
      return String.format(STAT_FILLER_1, idade_coelhohoras(), "h", sede, sedeMax, fome , fomeMax, alcool, alcoolMax); //informacoes exibidas 
    else
      return String.format(STAT_FILLER_1, idade_coelhodias(), " dias", sede, sedeMax, fome, fomeMax, alcool, alcoolMax); 
  }

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
    statlayer.setHeight(120);//altura do retangulo de informacoes
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
    layer.addAt(bgLayer, 0, 120);//janela do quarto do pingo
  }

  //--------------------------------------------------------------------------------
  /*
    Funcao responsavel por criar os butoes, estes sao colocados em um ArraList
  */
  private void make_buttons() {
    // create our UI manager and configure it to process pointer events
    iface = new Interface();

    //petSheet.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
    Root root = iface.createRoot(new AbsoluteLayout(), petSheet);

    root.setSize(width(), 354); // this includes the secondary buttons
    //    root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
    layer.addAt(root.layer, 0, 442);//Position of buttons

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
	  Acesso todos os butoes primarios (b) e secundarios (s) de forma a criar cada evento. Feito isso, apos instancia uma classe/sprite referente a aquela acao.
	  A funcao "detatch" eh utilziada para remover o sprite de tela, e em seguida seta-se a classe pingo para null. Provavelmente tera de mudar, pois eh necessario que tudo seja em funcao de atributos.
	*/
	if(b == 0 && s == 0)sbut.clicked().connect(new UnitSlot(){
	  public void onEmit(){//Atravez do evento comer sopa de cenoura, cria um novo pingoComendoSopaBacon
	    pingoComendoSopaCenoura = new PingoComendoSopaCenoura(layer, width()/2, height()/2);
	    if(pingo!=null){
	      pingo.detatch(layer);
	      pingo = null;
  	    }
	    //fome = fomeMax;
	  }
	});


	if(b == 0 && s == 1)sbut.clicked().connect(new UnitSlot(){
	  public void onEmit(){//Atravez do evento comer sopa de bacon, cria um novo pingoComendoSopaBacon
	    pingoComendoSopaBacon = new PingoComendoSopaBacon(layer, width()/2, height()/2);
	    if(pingo!=null){
	      pingo.detatch(layer);
	      pingo = null;
	    }
	    //fome = fomeMax;
	  }
	});


	if(b == 0 && s == 2)sbut.clicked().connect(new UnitSlot(){
	  public void onEmit(){//Atravez do evento beber agua, cria um novo pingoBebendoAgua
	    pingoBebendoAgua = new PingoBebendoAgua(layer, width()/2, height()/2);
	    if(pingo!=null){
	      pingo.detatch(layer);
	      pingo = null;
	    }
	    //sede = sedeMax;
	  }
	});

	if(b == 0 && s == 3)sbut.clicked().connect(new UnitSlot(){
	  public void onEmit(){//Atravez do evento beber leite, cria um novo pingoBebendoLeite
	    pingoBebendoLeite = new PingoBebendoLeite(layer, width()/2, height()/2);
	    if(pingo!=null){
	      pingo.detatch(layer);
		pingo = null;
	    }
	    //fome = fomeMax;
	  }
	});

        if (b == 6 // diversao
        &&  s == 0) // licor
          sbut.clicked().connect(new UnitSlot() {
            public void onEmit() {	
              alcool = alcoolMax; // TODO modificar de acordo com folha
	    }
          });
        /*-------------------------------------------------------------------------------*/
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
  /*-------------------------------------------------------------------------------*/

  @Override
  public void init() {
    System.out.println("passivo is " + alcoolPassivoBeats);
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

    /*
      Removi a criacao inicial do pingo, ja que agora isto depende da hora.
    */
    // sprites
    //pingo = new Pingo(layer, width() / 2, height() / 2);
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
    /*
      Aqui que sao realizadas as atualizacoes dos sprites, sem isto o sprite ficaria estatico
    */
    _clock.update(delta);
    if (pingo != null)
      pingo.update(delta);
    else if (pingovomitando != null)
      pingovomitando.update(delta);
    else if (pingobebado != null)
      pingobebado.update(delta);
    else if (pingocoma != null)
      pingocoma.update(delta);
    else if(pingoComendoSopaCenoura != null)
      pingoComendoSopaCenoura.update(delta);
    else if(pingoComendoSopaBacon !=null)
      pingoComendoSopaBacon.update(delta);
    else if (pingoBebendoAgua != null)
      pingoBebendoAgua.update(delta);
    else if(pingoBebendoLeite != null)
      pingoBebendoLeite.update(delta);
   else if(pingoDormindo != null)
      pingoDormindo.update(delta);

    /*
      Eh realizada a verificacao de todos os atributos, e tomando acoes de acordo com cada funcionalidade
    */
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
      if(fome <= fomeMin && pingoComendoSopaCenoura != null && pingo == null){
	fome = fomeMin;
	pingo = new Pingo(layer, width() / 2, height() / 2);
	pingoComendoSopaCenoura.detatch(layer);
	pingoComendoSopaCenoura = null;
	somArroto.play(); 
      }
      else if(pingo != null && pingoDormindo != null){ // TENTAR RESOLVER AKI
	pingoComendoSopaCenoura = new PingoComendoSopaCenoura(layer, width() / 2, height() / 2);
	pingo.detatch(layer);
	pingo = null;
      }
      if(fome <= fomeMin && pingoComendoSopaBacon != null && pingo == null){
	fome = fomeMin;
	pingo = new Pingo(layer, width() / 2, height() / 2);
	pingoComendoSopaBacon.detatch(layer);
	pingoComendoSopaBacon = null;
	somArroto.play(); 

      }

      if(sede <= sedeMin && pingoBebendoAgua != null && pingo == null){//Quando a sede for 0, aqui é realizada a troca do layer dele bebendo agua para normal
	sede = sedeMin; // para caso na hora de decrementar, resultar em um valor negativo. Assim o fará ser 0
	pingo = new Pingo(layer, width() / 2, height() / 2);
	pingoBebendoAgua.detatch(layer);
	pingoBebendoAgua = null;
	somArroto.play(); 
      }

      if(fome <= fomeMin && pingoBebendoLeite != null && pingo == null){
	fome = fomeMin;
	pingo = new Pingo(layer, width() / 2, height() / 2);
	pingoBebendoLeite.detatch(layer);
	pingoBebendoLeite = null;
	somArroto.play(); 
      }

      if(((idade_coelhohoras()-idade_coelhodias()*24) >= 22 || (idade_coelhohoras()-idade_coelhodias()*24) >= 0 && (idade_coelhohoras()-idade_coelhodias()*24) <= 8) && dormir == false){
	dormir = true;
	pingoDormindo = new PingoDormindo(layer, width()/2, height()/2);
	if(pingo!=null){
	  pingo.detatch(layer);
          pingo = null;
	}
      	System.out.println("Horas: "+(idade_coelhohoras()-idade_coelhodias()*24));
      }
      else if((idade_coelhohoras()-idade_coelhodias()*24) < 22 && (idade_coelhohoras()-idade_coelhodias()*24) > 8){
        if(pingoDormindo != null && pingo == null){
	  dormir = false;
	  pingo = new Pingo(layer, width()/2, height()/2);
	  pingoDormindo.detatch(layer);
	  pingoDormindo = null;
	  System.out.println("Horario de ficar acordado:"+(idade_coelhohoras()-idade_coelhodias()*24));

        }
      }

      if (alcool == 10) {
        if (pingocoma == null) {
          pingocoma = new PingoComa(layer, width() / 2, height() / 2);
	  somSoluco.play();
          if (pingo != null) {
            pingo.detatch(layer);//remove the layer
            pingo = null;
          }
        }
      } else  {
        if (pingocoma != null) {
          pingocoma.detatch(layer);
          pingocoma = null;
        }

        if (alcool >= 7) {
          if (pingovomitando == null) {
            if (pingo != null) {
              pingo.detatch(layer);//remove the layer
              pingo = null;
            }
            pingovomitando = new PingoVomitando(layer, width() / 2, height() / 2);
	    somSoluco.play();

          }
        } else {
          if (pingovomitando != null) {
            pingovomitando.detatch(layer);
            pingovomitando = null;
          }
        
          if (alcool >= 4) {
            if (pingobebado == null) {
              if (pingo != null) {
                pingo.detatch(layer);
                pingo = null;
              }
              pingobebado  = new PingoBebado(layer, width() / 2, height() / 2);
	      somSoluco.play();
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

      if ((beat % alcoolPassivoBeats) == 0)
       if (alcool > alcoolMin)
          alcool += alcoolPassivo;

/*
  Se for pingo normal, a sede aumenta.
*/
      if(pingo!=null){
	if ((beat % sedePassivoBeats) == 0)
	  if (sede >= sedeMin && sede < sedeMax)
	    sede += sedePassivo;
	}

/*
  Se for pingo bebendo agua, a sede deve diminuir.
*/      
      else if(pingoBebendoAgua != null){
	if ((beat % sedePassivoBeats) == 0)
	  if (sede <= sedeMax && sede > sedeMin)
	    sede -= sedePassivo+1;
      }
/*
  Se for o pingo normal, a fome aumenta.
*/    
      if(pingo!=null){
	if ((beat % fomePassivoBeats) == 0)
	  if (fome >= fomeMin && fome < fomeMax)
	    fome += fomePassivo;
      }

  /*
    Não achei necessidade (ainda) de criar um código apenas para pingo comendo cenoura, já que possuem mesmas funcionalidades.
  */
      else if(pingoComendoSopaBacon != null || pingoComendoSopaCenoura != null){//Se for o pingo comendo sopa de bacon, a fome dele deve diminuir
	if ((beat % fomePassivoBeats) == 0)
	  if (fome <= fomeMax && fome > fomeMin)
	    fome -= fomePassivo;
      }

      else if(pingoBebendoLeite != null){//Se for o pingo bebendo leite, a fome dele deve diminuir
	if ((beat % fomePassivoBeats) == 0)
	  if (fome <= fomeMax && fome > fomeMin)
	    fome -= fomePassivo;
      }
      Label l = (Label) main_stat_.childAt(1);
      l.text.update(idade_coelhodias_str());
    }

    if (iface != null) {
      iface.update(delta);
    }
    if (statbar_iface != null) {
      statbar_iface.update(delta);
    }
  }

}
