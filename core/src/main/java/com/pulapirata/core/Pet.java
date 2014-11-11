package com.pulapirata.core;

import java.util.ArrayList;
import java.util.LinkedList;
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
import playn.core.util.Callback;

import react.Function;
import react.Functions;
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

import com.pulapirata.core.Aviso;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.Triggers.TriggerType;
import com.pulapirata.core.Trigger;
import com.pulapirata.core.PetWorld;
import com.pulapirata.core.Messages;
import com.pulapirata.core.utils.MessageLoader;

import static com.pulapirata.core.utils.Puts.*;

public class Pet extends Game.Default {
    /*===============================================================================*/
    /* Data                                                                          */
    /*===============================================================================*/

    /*-------------------------------------------------------------------------------*/
    /** Status info shown on top */

    protected  static final String STAT_ALERT_1 =
        "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
    protected  static final String STAT_FILLER_1 = "Idade: %d%s\n Vida: %d/%d\n";
    protected  static final String STAT_FILLER_2 = "\nNutricao: %d/%d\n Grana: %d/%d";
    public Messages messages_;   // filled up by MessagesLoader

    /*-------------------------------------------------------------------------------*/
    /** Game Dimensions */

    public int width()  { return 480; }
    public int height() { return 800; }

    // origin measured from topleft corner
    public static int STAT_HEIGHT = 120;
    public static int WORLD_ORIGIN_Y = STAT_HEIGHT;
    public static int BUTTON_ORIGIN_Y = 442; // this includes the secondary buttons
    public static int MAIN_BUTTON_ORIGIN_Y = BUTTON_ORIGIN_Y+120;
    public static int WORLD_HEIGHT = MAIN_BUTTON_ORIGIN_Y-WORLD_ORIGIN_Y;


    /*-------------------------------------------------------------------------------*/

    public PetAttributes a() { return world_.mainPet(); }   // shortcut

    public enum UIDepth {
        Z_WORLD(100), Z_BG(20), Z_BUTTONS(26), Z_STATBAR(22);
        private final int z;

        UIDepth (int z) {
            this.z = z;
        }

        public int getZ() {
            return z;
        }
    }

    /*-------------------------------------------------------------------------------*/
    /** Time data */

    public static final int UPDATE_RATE = 100; // ms    // was: 100.

    public String idadeCoelhoDiasStr1() {
        if (world_ == null || !world_.worldLoaded())
            return "initializing (.)(.) initializing...";

        if (world_.idadeCoelhoDias() == 0)
            return String.format(
                  STAT_FILLER_1,
                  world_.idadeCoelhoHoras(), "h",
                  a().vida().val(),
                  a().vida().max());
        else
            return String.format(STAT_FILLER_1,
                  world_.idadeCoelhoDias(), " dias",
                  a().vida().val(),
                  a().vida().max());
    }

    public String idadeCoelhoDiasStr2() {
        if (world_ == null || !world_.worldLoaded())
            return "initializing (|) initializing";

        return String.format(STAT_FILLER_2,
                a().nutricao().val(), a().nutricao().max(),
                a().dinheiro().val(), a().dinheiro().max());
    }

    /*-------------------------------------------------------------------------------*/
    /** Pet attributes and info */

    private boolean bgLoaded_ = false;
    private boolean printIniDbg_ = false;

    /*-------------------------------------------------------------------------------*/
    /** Misc variables */

    protected final Clock.Source clock_ = new Clock.Source(UPDATE_RATE);

    /*-------------------------------------------------------------------------------*/
    /** Layers, groups and associated resources */

    private ImageLayer bgLayer_ = null;
    private Image bgImageDay_, bgImageNight_;
    private GroupLayer layer_;
    protected Group mainStat_;
    private Group rightStatbarGroup_;
    private TableLayout rightPartLayout_;
    private Interface iface_, statbarIface_;
    private Image exclamacao_;
    private Stylesheet petSheet_;
    ButtonManager bm_ = new ButtonManager();

    protected PetWorld world_;

    //--------------------------------------------------------------------------------
    /** Constructor */
    public Pet() {
        super(UPDATE_RATE);
    }

    //--------------------------------------------------------------------------------
    /** Constructs the top status bar */
    private void makeStatusbar() {
      // create and add the status title layer using drawings for faster loading
      CanvasImage bgtile = graphics().createImage(480, 119);
      bgtile.canvas().setFillColor(0xFFFFFFFF);
      bgtile.canvas().fillRect(0, 0, 480, 119);
      bgtile.canvas().setFillColor(0xFF333366);
      bgtile.canvas().fillRect(4, 4, 472, 112);

      // Font font = graphics().createFont("earthboundzero", Font.Style.PLAIN, 18);
      ImageLayer statlayer = graphics().createImageLayer(bgtile);
      //
      //  statlayer.setWidth(graphics().width());
      // FIXME: problem with graphics.width not being set correctly in html;
      // it always seems to give 640
      //
      statlayer.setHeight(STAT_HEIGHT);   // altura do retangulo de informacoes
      statlayer.setDepth(UIDepth.Z_STATBAR.getZ());
      layer_.add(statlayer);

      // ------ The text in the status bar as a tripleplay nested layout interface

      // TODO: e o tal do gaps?
      final int mae = 20; // mae == margin on the sides of exlamation
      final int mte = 18; // mae == margin on top of exlamation

      final int mainStatWidth = 200;

      TableLayout statbarLayout = new TableLayout(
          COL.minWidth(mainStatWidth).alignLeft().fixed(),
          COL.minWidth(30).stretch()).gaps(mae, mae).alignTop();

      // AxisLayout statbarLayout = new AxisLayout.horizontal().add(
      //   COL.minWidth(250).alignLeft().fixed(),
      //   COL.minWidth(30).stretch()).gaps(mae, mae).alignTop();

      // the left status plus is the left column
      // the (!) icon plust the right text is the right column

      rightPartLayout_ = new TableLayout(COL.fixed().minWidth(30),
              COL.alignLeft()).gaps(mae, mae).alignTop();

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
            new Button(Icons.image(exclamacao_)),
            // TODO in future this button will actually be an animation sprite
            new Label("Hello, world!").addStyles(Styles.make(
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

      statbarRoot.setSize(width(), STAT_HEIGHT);  // this includes the secondary buttons
      statbarRoot.layer.setDepth(UIDepth.Z_STATBAR.getZ());

      layer_.addAt(statbarRoot.layer, 0, 0);
      statbarRoot.add(AbsoluteLayout.at(statbar, mae, mte, width()-mae, STAT_HEIGHT-mte));
    }

    //--------------------------------------------------------------------------------
    /**
     * Request background images to be loaded
     */
    private void startBackgroundInit() {
        bgImageDay_ = assets().getImage("pet/images/cenario_quarto.png");
        bgImageNight_ = assets().getImage("pet/images/cenario_quarto_noite.png");
        bgImageDay_.addCallback(new Callback<Image>() {
                @Override
                public void onSuccess(Image resource) {
                    installBackgroundInit();
                }
                @Override
                public void onFailure(Throwable err) {
                    error(err);
                }
        });
    }
    /**
     * Install initially loaded background image
     */
    private void installBackgroundInit() {
        bgLayer_ = graphics().createImageLayer(bgImageDay_);
        bgLayer_.setDepth(UIDepth.Z_BG.getZ());
        layer_.addAt(bgLayer_, 0, STAT_HEIGHT);  // quarto do pingo
        bgLoaded_ = true;
    }

    /*-------------------------------------------------------------------------------*/
    /**
     * Classe responsavel por criar os botoes
     */
    private class ButtonManager {
        private boolean isWired_ = false;

        public boolean isWired() { return isWired_; }

        protected int numMainButts_ = 0;
        protected ArrayList< ArrayList<Button> > secondaryButtons_ = null;

        // Used for asset loading. We could also use an asset watcher as in
        // PeaLoader.java
        protected int numLoadedButts_ = 0;
        protected int totalNumButts_ = -1;

        public boolean hasLoaded()  { return numLoadedButts_ == totalNumButts_;}

        public void makeButtons() {
            // create our UI manager and configure it to process pointer events
            iface_ = new Interface();

            // petSheet_.builder().add(Button.class, Style.BACKGROUND.is(Background.blank()));
            Root broot = iface_.createRoot(new AbsoluteLayout(), petSheet_);

            broot.setSize(width(), 354);
                    // root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
            broot.layer.setDepth(UIDepth.Z_BUTTONS.getZ());
            layer_.addAt(broot.layer, 0, BUTTON_ORIGIN_Y); // position of buttons

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
                {0, 0},
                {120, 0},
                {240, 0},
                {360, 0},
                {0, 120},
                {120, 120},
                {240, 120},
                {360, 120},
            };

            final int[][] topleftSecondary = new int [][] {
                {0, 0},
                {120, 0},
                {240, 0},
                {360, 0},
            };
            /*-------------------------------------------------------------------------------*/

            numMainButts_ = imgButtSolto.size();
            final ArrayList<Group> sbuttons = new ArrayList<Group>(0);

            secondaryButtons_ = new ArrayList< ArrayList<Button> >(numMainButts_);

            totalNumButts_ = numMainButts_;


            for (int b = 0; b < numMainButts_; ++b) {
                final int bFinal = b;
                ToggleButton but = new ToggleButton (Icons.image(imgButtSolto.get(b)));
                buttons.add(AbsoluteLayout.at(but, topleft[b][0], topleft[b][1], 120, 120));
                // callbacks for loading the images
                imgButtSolto.get(b).addCallback(new Callback<Image> () {
                        @Override
                        public void onSuccess(Image resource) {
                            numLoadedButts_++;
                        }
                        @Override
                        public void onFailure(Throwable err) {
                            error(err);
                        }
                });

                // add button b's secondary buttons
                sbuttons.add(new Group(new AbsoluteLayout()).addStyles(
                  Style.BACKGROUND.is(Background.solid(0x55FFFFFF))));

                int numSecondaryButtons = imgButtSecondary.get(b).size();
                secondaryButtons_.add(new ArrayList<Button>(numSecondaryButtons));

                totalNumButts_ += numSecondaryButtons;

                for (int s = 0; s < numSecondaryButtons; ++s) {
                    Image buttImage = imgButtSecondary.get(b).get(s);
                    Button sbut = new Button(Icons.image(buttImage));
                    secondaryButtons_.get(b).add(sbut);
                    sbuttons.get(b).add(AbsoluteLayout.at(sbut,
                      topleftSecondary[s][0], topleftSecondary[s][1], 120, 120));

                    but.selected().map(new Function <Boolean, Icon>() {
                        public Icon apply (Boolean selected) {
                            if (selected)
                                return Icons.image(imgButtApertado.get(bFinal));
                            else
                                return Icons.image(imgButtSolto.get(bFinal));
                        }
                    }).connectNotify(but.icon.slot());
                    // all secondary buttons are added; toggle visibility only
                    broot.add(AbsoluteLayout.at(sbuttons.get(bFinal), 0, 0, width(), 120));
                    sbuttons.get(bFinal).setVisible(false);

                    // callbacks for loading the images
                    buttImage.addCallback(new Callback<Image> () {
                            @Override
                            public void onSuccess(Image resource) {
                                numLoadedButts_++;
                            }
                            @Override
                            public void onFailure(Throwable err) {
                                error(err);
                            }
                    });
                }

                Selector sel = new Selector(buttons, null);
                broot.add(AbsoluteLayout.at(buttons, 0, 118, width(), 236));

                // TODO: improve this part with a button -> index map so we don't go through
                // all butts
                sel.selected.connect(new Slot<Element<?>>() {
                    @Override public void onEmit (Element<?> event) {
                        if (event == null) {
                            for (Group sb : sbuttons)
                                sb.setVisible(false);
                        } else {
                            for (int i=0; i < numMainButts_; ++i) {
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
            }
        }

        /**
         * Connects button events to the internal world.
         * This is usually called after world assets are loaded.
         */
        public void wireButtonsToWorld() {
            assert numMainButts_ != 0 : "tried to wire world before constructing buttons.";
            for (int b = 0; b < numMainButts_; ++b) {
                for (int s = 0; s < secondaryButtons_.get(b).size(); ++s) {
                    if (b == 0 /* comida */ && s == 0 /* sopa cenoura */)
                        secondaryButtons_.get(b).get(s).clicked().connect(new UnitSlot() {
                            public void onEmit() {
                                //a().triggers(SOPA_DE_CENOURA).fire(a).fireIfAllowed(a, CRIANCA);   // TODO remover argumento redundante ou criar overload
                                if (world_.worldLoaded()) { // use asset manager
                                    world_.triggers().get(TriggerType.SOPA_DE_CENOURA).fire(a());
                                }
                            }
                        });
                    if (b == 5 && s == 0)
                        secondaryButtons_.get(b).get(s).clicked().connect(new UnitSlot() {
                            public void onEmit() {
                                a().alcool().sub(1);
                            }
                        });

                    if (b == 6 /* diversao */ && s == 0/* licor */)
                        // hooks up the liquor button to setting alcool to max.  button
                        // press event is filtered to emit alcool().max() to the
                        // alcool() attribute.
                        secondaryButtons_.get(b).get(s).clicked().map(
                            Functions.constant( a().alcool().max()) ).connect(a().alcool().slot());
                }
            }

            isWired_ = true;
        }
    } // !ButtonManager


    @Override
    public void init() {
      //      try {     // (rfabbri): leaving this try-catch code as an example
      //        assert 1 == 0 : "Asserts are on +_+_+_+_+_+_+___+_+__";
      //      } catch (AssertionError e) {
      //        System.out.println("thread aborts.");
      //        System.exit(0);//logging or any action
      //      }
      //      if (0 == 0) {
      //        throw new AssertionError("testing assert err");
      //      } else {
      //      System.out.println("thread runs.");
      //      System.exit(1);
      //      }

      // create a group layer_ to hold everything
      layer_ = graphics().createGroupLayer();
      graphics().rootLayer().add(layer_);
      petSheet_ = PetStyles.newSheet();

      makeStatusbar();
      startBackgroundInit();
      GroupLayer worldLayer_ = graphics().createGroupLayer();
      worldLayer_.setDepth(UIDepth.Z_WORLD.getZ());
      // worldLayer_.setOrigin(0, WORLD_ORIGIN_Y);     // center of screen
      layer_.addAt(worldLayer_, 0, WORLD_ORIGIN_Y);
      world_ = new PetWorld(worldLayer_, width(), WORLD_HEIGHT);
      bm_.makeButtons();
      /** Load messages */
      MessageLoader.CreateMessages(Messages.JSON_PATH,
          new Callback<Messages>() {
              @Override
              public void onSuccess(Messages resource) {
                  messages_ = resource;
              }

              @Override
              public void onFailure(Throwable err) {
                  PlayN.log().error("Error loading messages : " + err.getMessage());
              }
          });
    }

    //--------------------------------------------------------------------------------
    @Override
    public void paint(float alpha) {
        // layers automatically paint themselves (and their children). The rootlayer
        // will paint itself, the background, and the sprites group layer_ automatically
        // so no need to do anything here!

        if (world_ != null)
            world_.paint(clock_);

        if (iface_ != null && bgLoaded_ && bm_.hasLoaded())
            iface_.paint(clock_);

        if (statbarIface_ != null)
            statbarIface_.paint(clock_);
    }

    //--------------------------------------------------------------------------------
    @Override
    public void update(int delta) {
        clock_.update(delta);

        if (world_ != null) {
            if (world_.worldLoaded()) {
                if (!bm_.isWired())
                    bm_.wireButtonsToWorld();

                if (printIniDbg_) {
                    a().print();
                    printIniDbg_ = false;
                }

                world_.update(delta);
            }

            if (messages_ != null) {
                if (!messages_.isLabelSet() && world_.attributesLoaded()) {
                    messages_.setLabel((Label) rightStatbarGroup_.childAt(1));
                    messages_.init(a());
                }
                if (world_.beat_ % world_.beatsCoelhoHora_ == 0)
                    messages_.update();
            }
        }


        Label l = (Label) mainStat_.childAt(1);
        l.text.update(idadeCoelhoDiasStr1());
        l =  (Label) mainStat_.childAt(2);
        l.text.update(idadeCoelhoDiasStr2());

        if (iface_ != null && bgLoaded_ && bm_.hasLoaded())
            iface_.update(delta);

        if (statbarIface_ != null)
            statbarIface_.update(delta);
    }

    /**
     * Should be called if an error occurs when loading the sprite image or data. Will handle calling
     * the {@link Callback} of the {@link Sprite}.
     */
    void error(Throwable err) {
        // don't let the error fall on deaf ears
        log().error("Error loading assets", err);
    }
}
