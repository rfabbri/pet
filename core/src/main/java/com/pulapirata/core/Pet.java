package com.pulapirata.core;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
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
import tripleplay.ui.Elements;
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

import com.pulapirata.core.Aviso;

public class Pet extends Game.Default {
  //------ Basic Game Properties
  private GroupLayer layer;
  private Group main_stat_; //< statusbar
  private Group right_statbar_group_;
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

  private  static final String STAT_ALERT_1 = "Pingo recebeu convite para ir a um aniversario de um colega na escola.";//Talvez seja excluido
  private  static final String STAT_FILLER_1 = "Idade: %d %s\nAlcool: %d/%d";
  private Aviso aviso_atual = new Aviso("Bem vindo ao jogo Pet");
  private Aviso fome_aviso = new Aviso();
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
  private int fome_passivo_ = -5;
  private int fome_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./4.,1); //15 min
  private int fome_max_ = 120;
  private int fome_min_ = -20;

  private int humor_ = 30;
  private int humor_passivo_ = -5;
  private int humor_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./3.,1); //20 min
  private int humor_max_ = 120;
  private int humor_min_ = -20;

  private int social_ = 30;
  private int social_passivo_ = -5;
  private int social_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.*2./3.,1); //40min
  private int social_max_ = 120;
  private int social_min_ = -20;

  private int higiene_ = 30;
  private int higiene_passivo_ = -5;
  private int higiene_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60./2.,1); //30 min
  private int higiene_max_ = 120;
  private int higiene_min_ = -20;

  private int estudo_ = 0;
  private int estudo_passivo_ = -1;
  //private int estudo_passivo_beats_ = ;//? por dia a partir da matricula (colocar um valor inicial depois da matricula mudar)
  //(int) Math.max(beats_coelhosegundo*60.*60.*24.,1); //dia
  private int estudo_max_ = 10;
  private int estudo_min_ = -5;

  private int saude_ = 5;
  private int saude_passivo_ = -1;
  private int saude_passivo_beats_ = (int) Math.max(beats_coelhosegundo*60.*60.*24.,1);//? por idade (em dias?)
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

    right_statbar_group_ = new Group(rightpart_layout).add (
          new Button(Icons.image(exclamacao)), // FIXME an icon goes here or else blank space w icon's size
          // TODO in future this button will actually be an animation sprite
          new Label(aviso_atual.getAviso()).addStyles(Styles.make(
              Style.COLOR.is(0xFFFFFFFF),
              Style.TEXT_WRAP.is(true),
              Style.HALIGN.left
              ))
          );

    final Group statbar = new Group (statbar_layout).add (
        main_stat_,
        right_statbar_group_
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
      // pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
      //Adicionando avisos na lista
      avisos.add(fome_aviso);
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
      else if(pingopiscando != null)
        pingopiscando.update(delta);

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
        Label l = (Label) main_stat_.childAt(1);
        l.text.update(idade_coelhodias_str());
      }

      if (iface != null) {
        iface.update(delta);
      }
      if (statbar_iface != null) {
        statbar_iface.update(delta);
      }

      passivoAtributos();

      verifica_avisos();

      
      if(beat%alcool_passivo_beats_==0){ //MUDAR DEPOIS: Aviso muda a cada hora
        muda_aviso();
      }
       
      //piscar();


      //System.out.println(_rando.getInRange(1,10));

      /*VIsualizando atributo no terminal*/
      //System.out.println("Fome: " + fome_);
      //System.out.println("Social: " + social_);
      //System.out.println("Humor: " + humor_);
      //System.out.println("Saude: " + saude_);
      //System.out.println("Alcool: " + alcool_); 

    } //fim do update

  public void passivoAtributos(){
    //inicio dos passivos dos atributos
    if ((beat % alcool_passivo_beats_) == 0)
      if (alcool_ > alcool_min_)
        alcool_ += alcool_passivo_;
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

  public void atualiza_aviso(){
	 //System.out.println("aviso_status_bar: " + aviso_status_bar);//Tirar depois, so para testes
//    make_statusbar();
    // Algo como main_stat_.Label.set(aviso)
    // statbar_iface.statbar[1].Label.set(aviso)
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
*//*
    right_statbar_group_.add(1,
        new Label(aviso_atual.getAviso()).addStyles(Styles.make(
              Style.COLOR.is(0xFFFFFFFF),
              Style.TEXT_WRAP.is(true),
              Style.HALIGN.left
              )));*/
	make_statusbar();

  }

  public void remove_aviso(Aviso aviso){
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
 


  void verifica_avisos(){
    /*if(fome_ <= 0){
      fome_aviso.setAviso("Pingo está ficando fraco!");
    //chorando
    } else if(fome_ <= 20){
    fome_aviso.setAviso("Pingo está com muita fome!");
    //chorando
    } else if(fome_ <= 40){
    fome_aviso.remove();
    //triste
    } else if(fome_ <= 60){
    fome_aviso.remove();
    //normal
    } else if(fome_ <= 80){
    fome_aviso.setAviso("Pingo está cheio");
    //normal
    }else if(fome_ <= 100){
    fome_aviso.setAviso("Pingo comeu demais e está passando mal");
    //normal+vomitando
    }

    if(!fome_aviso.isEmpty() && !avisos.contains(fome_aviso)){
    remove_aviso(fome_aviso);
    } else if(!fome_aviso.isEmpty() && !avisos.contains(fome_aviso)){
    avisos.add(fome_aviso);
    }
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
    if(alcool_<=0){
      if(!alcool_aviso.getAviso().equals(""))
     	 remove_aviso(alcool_aviso);
      //normal
    } else if(alcool_<=3){
       if(!alcool_aviso.getAviso().equals(""))	
    	  remove_aviso(alcool_aviso);
      //normal
    }else if(alcool_<=6){
	    if(!alcool_aviso.getAviso().equals("Pingo está bêbado")){      
	      alcool_aviso.setAviso("Pingo está bêbado");
		    if(aviso_atual==alcool_aviso)
			   atualiza_aviso();
    }
      //bebado
    }else if(alcool_<=9){
	    if(!alcool_aviso.getAviso().equals("Pingo está muito bêbado para executar certas atividades")){     
     	 alcool_aviso.setAviso("Pingo está muito bêbado para executar certas atividades");
		    if(aviso_atual==alcool_aviso)
			 atualiza_aviso();
	}
      //bebado + vomitando
    }else if(alcool_<=10){
     	if(!alcool_aviso.getAviso().equals("Pingo entrou em coma alcoólico")){      
		    alcool_aviso.setAviso("Pingo entrou em coma alcoólico");
		    if(aviso_atual==alcool_aviso)
			atualiza_aviso();
	    }
	      //em coma
    }

  }


  void piscar(){
    //Pingo piscando
    /*
       if(pingopiscando != null){
       System.out.println("Pingo Piscando");
       pingopiscando.detatch(layer);
       pingopiscando = null;
       pingo = new Pingo(layer, width() / 2, height() / 2);
       System.out.println("Pingo Normal");
       } else if(pingo != null){
       System.out.println("Pingo Normal");
       pingo.detatch(layer);
       pingo = null;
       pingopiscando = new PingoPiscando(layer, width() / 2, height() / 2);
       System.out.println("Pingo Piscando");
       }*/

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
    if(pingo!=null && pingo.getTraversed() && beat/((int) Math.max(beats_coelhosegundo*60.*60.*2.,1))%r==0){
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

  public void imprime(List<Aviso> l){
      System.out.println("Lista de Avisos: ");
      ListIterator<Aviso> i = l.listIterator();
      Aviso a;
      //a= i.next();
      while(i.hasNext()){
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



