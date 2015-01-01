/**
 * Pet - a comic pet simulator game
 * Copyright (C) 2013-2015 Ricardo Fabbri and Edson "Presto" Correa
 *
 * This program is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version. A different license may be requested
 * to the copyright holders.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>
 *
 * NOTE: this file may contain code derived from the PlayN, Tripleplay, and
 * React libraries, all (C) The PlayN Authors or Three Rings Design, Inc.  The
 * original PlayN is released under the Apache, Version 2.0 License, and
 * TriplePlay and React have their licenses listed in COPYING-OOO. PlayN and
 * Three Rings are NOT responsible for the changes found herein, but are
 * thankfully acknowledged.
 */
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
import playn.core.gl.GLContext;
import playn.core.Key;
import playn.core.Keyboard;

import react.Function;
import react.Functions;
import react.UnitSlot;
import react.Slot;
import react.Signal;

import tripleplay.util.Randoms;
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
import static tripleplay.ui.layout.TableLayout.COL;
import tripleplay.sound.SoundBoard;
import tripleplay.sound.Clip;
import tripleplay.sound.Loop;

import com.pulapirata.core.DevGame;
import com.pulapirata.core.DevClock;
import com.pulapirata.core.Aviso;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.Triggers.TriggerType;
import static com.pulapirata.core.Triggers.TriggerType.*;
import com.pulapirata.core.Trigger;
import com.pulapirata.core.PetWorld;
import com.pulapirata.core.Messages;
import com.pulapirata.core.PetAudio;
import com.pulapirata.core.utils.MessageLoader;

import static com.pulapirata.core.utils.Puts.*;

public class Pet extends DevGame {
    /*===============================================================================*/
    /* Data                                                                          */
    /*===============================================================================*/

    /*-------------------------------------------------------------------------------*/
    /** Status info shown on top */

    protected  static final String STAT_ALERT_1 =
        "Pingo recebeu convite para ir a um aniversario de um colega na escola.";
    protected  static final String STAT_FILLER_1 = "Idade: %02d:%02d\n Vida: %d/%d\n";
    protected  static final String STAT_FILLER_2 = "\nNutricao: %d/%d\n Grana: %d/%d";
    public Messages messages_;   // filled up by MessagesLoader

    /*-------------------------------------------------------------------------------*/
    /** Game Dimensions */

    public int width()  { return 480; }
    public int height() { return 800; }

    // origin measured from topleft corner
    public static int STAT_HEIGHT = 120;
    public static int WORLD_ORIGIN_Y = STAT_HEIGHT;
    public static int BUTTON_ORIGIN_Y = 438; // this includes the secondary buttons
    public static int MAIN_BUTTON_ORIGIN_Y = BUTTON_ORIGIN_Y+120;
    public static int WORLD_HEIGHT = MAIN_BUTTON_ORIGIN_Y-WORLD_ORIGIN_Y;


    /*-------------------------------------------------------------------------------*/

    public PetAttributes a() { return world_.mainPet(); }   // shortcut
    public PetWorld w() { return world_; }   // shortcut

    public enum UIDepth {
        Z_WORLD(100), Z_BG(20), Z_BUTTONS(26), Z_SBUTTONS(120), Z_STATBAR(22);
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
                  world_.idadeCoelhoHoras(), world_.idadeCoelhoMinutos(),
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
    private boolean printIniDbg_ = true;
    public int numGlucose_ = 1;
    public int dayGlucose_= 0;

    /*-------------------------------------------------------------------------------*/
    /** Misc variables */

    protected DevClock clock_ = new DevClock(UPDATE_RATE);
    public  final   Signal<Key> keyDown_ = Signal.create();

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

        keyDown_.connect(new Slot<Key>() {
            @Override public void onEmit (Key key) {
                pprint("[Petkey] keydown: " + key);
                double speedup;
                switch (key) {
                  case LEFT_BRACKET:
                    speedup = (double)UPDATE_RATE/((double)updateRate()*2);
                    if (speedup > 1  && speedup < 1.5)
                        setUpdateRate(UPDATE_RATE);
                    else
                        setUpdateRate(updateRate() * 2);
                    speedup = (double)UPDATE_RATE/((double)updateRate());
                    pprint("[Petkey] update_rate = " + updateRate() + "\t\tspeed ("
                            + speedup + "x)");
                    break;
                  case RIGHT_BRACKET:
                    speedup = (double)UPDATE_RATE/((double)updateRate()/2);
                    if (updateRate() >= 4)
                        if (speedup > 0.8 && speedup < 1) // clamp
                            setUpdateRate(UPDATE_RATE);
                        else
                            setUpdateRate(updateRate() / 2);
                    else
                        pprint("[Petkey] max game speed reached - 1 update per 2 milisecond");
                    speedup = (double)UPDATE_RATE/((double)updateRate());
                    pprint("[Petkey] update_rate = " + updateRate() + "\t\tspeed ("
                            + (double)UPDATE_RATE/(double)updateRate() + "x)");
                    break;
                  default: break;
                }
            }
        });

        PlayN.keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown (Keyboard.Event event) {
                keyDown_.emit(event.key());
                if (world_ != null && world_.worldLoaded())
                    world_.keyDown_.emit(event.key());
            }
            @Override public void onKeyUp (Keyboard.Event event) {
                if (world_ != null && world_.worldLoaded())
                    world_.keyUp_.emit(event.key());
            }
        });
    }

    public void setUpdateRate(int updateRate) {
        clock_.setUpdateRate(updateRate);
        super.setUpdateRate(updateRate);
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
        final ArrayList<Group> sbuttons_ = new ArrayList<Group>(0);

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
            Root sbroot = iface_.createRoot(new AbsoluteLayout(), petSheet_);

            broot.setSize(width(), 240);
                    // root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
            broot.layer.setDepth(UIDepth.Z_BUTTONS.getZ());
            layer_.addAt(broot.layer, 0, MAIN_BUTTON_ORIGIN_Y); // position of buttons

            sbroot.setSize(width(), 120);
                    // root.addStyles(Style.BACKGROUND.is(Background.solid(0xFF99CCFF)));
            sbroot.layer.setDepth(UIDepth.Z_SBUTTONS.getZ());
            layer_.addAt(sbroot.layer, 0, BUTTON_ORIGIN_Y); // position of buttons

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
                        assets().getImage("pet/main-buttons/01_comer_principal.png"),
                        assets().getImage("pet/main-buttons/02_brincar_principal.png"),
                        assets().getImage("pet/main-buttons/03_socializar_principal.png"),
                        assets().getImage("pet/main-buttons/04_higienizar_principal.png"),
                        assets().getImage("pet/main-buttons/05_desestressar_principal.png"),
                        assets().getImage("pet/main-buttons/06_sair_principal.png"),
                        assets().getImage("pet/main-buttons/07_cuidar_principal.png"),
                        assets().getImage("pet/main-buttons/08_disciplinar_principal.png")
//                    assets().getImage("pet/main-buttons/01_comida_principal.png"),
//                    assets().getImage("pet/main-buttons/02_diversao_principal.png"),
//                    assets().getImage("pet/main-buttons/03_social_principal.png"),
//                    assets().getImage("pet/main-buttons/04_higiene_principal.png"),
//                    assets().getImage("pet/main-buttons/05_obrigacoes_principal.png"),
//                    assets().getImage("pet/main-buttons/06_saude_principal.png"),
//                    assets().getImage("pet/main-buttons/07_lazer_principal.png"),
//                    assets().getImage("pet/main-buttons/08_disciplina_principal.png")
                    ));

            final ArrayList<Image> imgButtApertado =
                new ArrayList<Image> (Arrays.asList(
                        assets().getImage("pet/main-buttons/01_comer_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/02_brincar_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/03_socializar_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/04_higienizar_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/05_desestressar_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/06_sair_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/07_cuidar_principal_apertado.png"),
                        assets().getImage("pet/main-buttons/08_disciplinar_principal_apertado.png")
//                    assets().getImage("pet/main-buttons/01_comida_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/02_diversao_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/03_social_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/04_higiene_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/05_obrigacoes_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/06_saude_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/07_lazer_principal_apertado.png"),
//                    assets().getImage("pet/main-buttons/08_disciplina_principal_apertado.png")
                    ));

            ArrayList< ArrayList<Image> > s_imgButtSecondary = new ArrayList< ArrayList<Image> > (0);

            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/011_leite.png"),
                    assets().getImage("pet/main-buttons/012_sopa_de_bacon.png"),
                    assets().getImage("pet/main-buttons/013_sopa_de_cenoura.png"),
                    assets().getImage("pet/main-buttons/014_pizza.png"),
                    assets().getImage("pet/main-buttons/015_salada_com_legumes.png"),
                    assets().getImage("pet/main-buttons/016_chocolate.png"),
                    assets().getImage("pet/main-buttons/017_agua.png"),
                    assets().getImage("pet/main-buttons/018_cafe.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/021_bola.png"),
                    assets().getImage("pet/main-buttons/022_tv.png"),
                    assets().getImage("pet/main-buttons/023_livro.png"),
                    assets().getImage("pet/main-buttons/024_anime.png"),
                    assets().getImage("pet/main-buttons/025_quadrinhos.png"),
                    assets().getImage("pet/main-buttons/026_video_game.png"),
                    assets().getImage("pet/main-buttons/027_cineprive.png"),
                    assets().getImage("pet/main-buttons/028_livro2.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/031_ligar_amigo.png"),
                    assets().getImage("pet/main-buttons/032_convidar_colega.png"),
                    assets().getImage("pet/main-buttons/033_rpg.png"),
                    assets().getImage("pet/main-buttons/034_redesocial.png"),
                    assets().getImage("pet/main-buttons/035_fazer_festa.png"),
                    assets().getImage("pet/main-buttons/036_booty_call.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/041_escovar_os_dentes.png"),
                    assets().getImage("pet/main-buttons/042_tomar_banho.png"),
                    assets().getImage("pet/main-buttons/043_varrer.png"),
                    assets().getImage("pet/main-buttons/044_passar_perfume.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/051_melzinho_na_chupeta.png"),
                    assets().getImage("pet/main-buttons/052_bombom_de_licor.png"),
                    assets().getImage("pet/main-buttons/052_cerveja.png"),
                    assets().getImage("pet/main-buttons/053_suco_de_macaco.png"),
                    assets().getImage("pet/main-buttons/055_cigarro.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/061_para_escola.png"),
                    assets().getImage("pet/main-buttons/062_para_o_trabalho.png"),
                    assets().getImage("pet/main-buttons/063_para_o_parque.png"),
                    assets().getImage("pet/main-buttons/064_para_o_bar.png"),
                    assets().getImage("pet/main-buttons/065_para_a_igreja.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/071_curativo.png"),
                    assets().getImage("pet/main-buttons/072_remedio.png"),
                    assets().getImage("pet/main-buttons/073_glicose.png"),
                    assets().getImage("pet/main-buttons/074_vacina.png"),
                    assets().getImage("pet/main-buttons/075_viagra.png")
                    )));
            s_imgButtSecondary.add(
                new ArrayList<Image> (Arrays.asList(
                    assets().getImage("pet/main-buttons/081_fazer_tarefa.png"),
                    assets().getImage("pet/main-buttons/082_com_sermao.png"),
                    assets().getImage("pet/main-buttons/083_com_castigo.png"),
                    assets().getImage("pet/main-buttons/084_com_chinelada.png"),
                    assets().getImage("pet/main-buttons/085_com_chicotada.png")
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
                {0,   0},
                {120, 0},
                {240, 0},
                {360, 0},
                {480, 0},
                {600, 0},
                {720, 0},
                {840, 0}
            };

            /*-------------------------------------------------------------------------------*/

            numMainButts_ = imgButtSolto.size();

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
                sbuttons_.add(new Group(new AbsoluteLayout()).addStyles(
                  Style.BACKGROUND.is(Background.solid(0x55FFFFFF))));

                int numSecondaryButtons = imgButtSecondary.get(b).size();
                secondaryButtons_.add(new ArrayList<Button>(numSecondaryButtons));

                totalNumButts_ += numSecondaryButtons;

                for (int s = 0; s < numSecondaryButtons; ++s) {
                    Image buttImage = imgButtSecondary.get(b).get(s);
                    Button sbut = new Button(Icons.image(buttImage));
                    secondaryButtons_.get(b).add(sbut);
                    sbuttons_.get(b).add(AbsoluteLayout.at(sbut,
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
                    sbroot.add(AbsoluteLayout.at(sbuttons_.get(bFinal), 0, 0, width(), 120));
                    sbuttons_.get(bFinal).setVisible(false);

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
                broot.add(AbsoluteLayout.at(buttons, 0, 0, width(), 240));

                // TODO: improve this part with a button -> index map so we don't go through
                // all butts
                sel.selected.connect(new Slot<Element<?>>() {
                    @Override public void onEmit (Element<?> event) {
                        if (event == null) {
                            for (Group sb : sbuttons_)
                                sb.setVisible(false);
                        } else {
                            for (int i=0; i < numMainButts_; ++i) {
                                if (buttons.childAt(i) == (ToggleButton) event &&
                                        sbuttons_.get(i).childCount() != 0) {
                                    sbuttons_.get(i).setVisible(true);
                                } else {
                                    sbuttons_.get(i).setVisible(false);
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
            /** Triggers for each secondary button */
            final TriggerType[][] trigg =
                // tabela de triggers seguindo ordem exata dos botoes (ordem dos
                // arquivos de imagens)
                new TriggerType[][] {
                    {LEITE, SOPA_DE_BACON, SOPA_DE_CENOURA, PIZZA, SALADA_COM_LEGUMES, CHOCOLATE, AGUA, NOT_IMPLEMENTED},
                    {BOLA, TV, LIVRO, ANIME, QUADRINHOS, VIDEOGAME, CINE_PRIVE, NOT_IMPLEMENTED},
                    {LIGAR_PARA_AMIGO, CONVIDAR_COLEGA, JOGAR_RPG, REDE_SOCIAL, FAZER_FESTA, BOOTY_CALL},
                    {ESCOVAR_DENTES, TOMAR_BANHO, VARRER, PASSAR_PERFUME},
                    {PINGUINHA_NA_CHUPETA, BOMBOM_DE_LICOR, CERVEJA, SUCO_DE_MACACO, CIGARRO},
                    {SAIR_PARA_ESCOLA, NOT_IMPLEMENTED, SAIR_PARA_PARQUE, NOT_IMPLEMENTED, SAIR_PARA_IGREJA},
                    {CURATIVO, REMEDIO, DAR_GLICOSE, NOT_IMPLEMENTED, NOT_IMPLEMENTED /*VIAGRA*/},
                    {ESTUDAR, GRITAR, CASTIGAR, CHINELADA, CHICOTEAR}
                };

            assert numMainButts_ != 0 : "tried to wire world before constructing buttons.";
            for (int b = 0; b < numMainButts_; ++b) {
                for (int s = 0; s < secondaryButtons_.get(b).size(); ++s) {
                    if (trigg[b][s] == NOT_IMPLEMENTED)
                        continue;
                    final TriggerType t = trigg[b][s];
                    final int but = b;
                    secondaryButtons_.get(b).get(s).clicked().connect(new UnitSlot() {
                        public void onEmit() {
                            //a().triggers(SOPA_DE_CENOURA).fire(a).fireIfAllowed(a, CRIANCA);   // TODO remover argumento redundante ou criar overload
                            sbuttons_.get(but).setVisible(false);

                            // Fazendo a verificação de está de noite ou de dia.
                            if (world_.worldLoaded() && world_.hourOfDay() > 8 ) { // use asset manager
                                if (t == DAR_GLICOSE && numGlucose_ != 0) {
                                    numGlucose_--;
                                    dayGlucose_ = w().idadeCoelhoDias();
                                    world_.triggers().get(t).fireIfAllowed(a());
                                } else
                                    world_.triggers().get(t).fireIfAllowed(a());
                            } else {
                                pprint("[button] buttons are blocked at night.");
                            }
                        }
                    });

//                    if (b == 6 /* diversao */ && s == 0/* licor */)
                        // hooks up the liquor button to setting alcool to max.  button
                        // press event is filtered to emit alcool().max() to the
                        // alcool() attribute.
//                        secondaryButtons_.get(b).get(s).clicked().map(
//                            Functions.constant( a().alcool().max()) ).connect(a().alcool().slot());
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
      assert graphics().ctx() != null : "currently we only support platforms with OpenGL";

      graphics().ctx().setTextureFilter(GLContext.Filter.NEAREST, GLContext.Filter.NEAREST);

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
      world_ = new PetWorld(worldLayer_, width(), WORLD_HEIGHT, this);
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
      PetAudio.init();
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

                // restore doses of glucose daily
                if (dayGlucose_ != w().idadeCoelhoDias()) {
                    numGlucose_++;
                    dayGlucose_ = w().idadeCoelhoDias();
                }
            }

            if (messages_ != null) {
                if (!messages_.isLabelSet() && world_.attributesLoaded()) {
                    messages_.setLabel((Label) rightStatbarGroup_.childAt(1));
                    messages_.init(a());
                }
                if (world_.beat_ % 20 == 0)
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

        PetAudio.update(delta);
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
