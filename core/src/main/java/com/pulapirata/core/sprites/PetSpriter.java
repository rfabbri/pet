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
package com.pulapirata.core.sprites;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.EnumMap;
import react.Slot;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;
import static playn.core.PlayN.log;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.VisibleCondition;
import static com.pulapirata.core.PetAttributes.VisibleCondition.*;
import com.pulapirata.core.PetAttributes.AgeStage;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 */
public class PetSpriter extends CompositeSpriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";



    private final ArrayList<String> imagesBebe =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Bebe/pingo_bebe_bebado_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_chorando_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_coma_alcoolico_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_comendo_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_dormindo_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_doente_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_morto_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_piscando_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_pulando_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_triste_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_vomitando_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_varrendo_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_bravo_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_irritado_v2.png",
                "pet/sprites/Pingo/Bebe/pingo_bebe_ressaca_v2.png",
                "pet/sprites/overlays/questionmark.png"
                // TODO: melhorar animacao "NORMAL" compondo respiro+ piscada aleatoria pingo_bebe_respirando_v2.png
        ));

    private final ArrayList<String> jsonsBebe =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Bebe/pingo_bebe_bebado_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_chorando_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_coma_alcoolico_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_comendo_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_dormindo_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_doente_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_morto_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_piscando_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_pulando_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_triste_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_vomitando_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_varrendo_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_bravo_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_irritado_v2.json",
                "pet/sprites/Pingo/Bebe/pingo_bebe_ressaca_v2.json",
                "pet/sprites/overlays/questionmark.json"
        ));

    private final ArrayList<VisibleCondition> vcBebe =
        new ArrayList<VisibleCondition>(Arrays.asList(
                BEBADO,
                CHORANDO,
                COMA_ALCOOLICO,
                COMENDO,
                DORMINDO,
                DOENTE,
                MORTO,
                NORMAL,
                PULANDO,
                TRISTE,
                VOMITANDO,
                VARRENDO,
                BRAVO,
                IRRITADO,
                RESSACA,
                UNDETERMINED
        ));
    // Novo nao tem:
    //        MACHUCADO,
    //        MUITO_MACHUCADO,
    //        NORMAL_COM_VOMITO,
    //        BEBADO_VOMITANDO,

    private final ArrayList<String> imagesCrianca =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Crianca/pingo_crianca_bebado_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_bravo_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_chorando_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_coma_alcoolico_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_comendo_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_doente_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_dormindo_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_irritado_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_morto_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_piscando_v2.png",
                "pet/sprites/overlays/questionmark.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_respirando_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_ressaca_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_triste_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_varrendo_v2.png",
                "pet/sprites/Pingo/Crianca/pingo_crianca_vomitando_v2.png",
                "pet/sprites/overlays/questionmark.png"
        ));

    private final ArrayList<String> jsonsCrianca =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Crianca/pingo_crianca_bebado_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_bravo_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_chorando_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_coma_alcoolico_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_comendo_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_doente_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_dormindo_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_irritado_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_morto_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_piscando_v2.json",
                "pet/sprites/overlays/questionmark.json",
                //"pet/sprites/Pingo/Crianca/pingo_crianca_pulando_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_respirando_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_ressaca_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_triste_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_varrendo_v2.json",
                "pet/sprites/Pingo/Crianca/pingo_crianca_vomitando_v2.json",
                "pet/sprites/overlays/questionmark.json"
        ));

    private final ArrayList<VisibleCondition> vcCrianca =
        new ArrayList<VisibleCondition>(Arrays.asList(
                BEBADO,
                BRAVO,
                CHORANDO,
                COMA_ALCOOLICO,
                COMENDO,
                DOENTE,
                DORMINDO,
                IRRITADO,
                MORTO,
                NORMAL,
                PULANDO,
                RESPIRANDO,
                RESSACA,
                TRISTE,
                VARRENDO,
                VOMITANDO,
                UNDETERMINED
        ));

    private final ArrayList<String> imagesAdolescente =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_bebado.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_bravo.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_caminhando.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_chorando.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_comaalcoolico.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_comendo.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_doente.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_dormindo.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_irritado.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_machucado.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_morto.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_piscando.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_respirando.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_ressaca.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_triste.png",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_vomitando.png",
                "pet/sprites/overlays/questionmark.png"
        ));

    private final ArrayList<String> jsonsAdolescente =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_bebado.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_bravo.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_caminhando.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_chorando.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_comaalcoolico.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_comendo.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_doente.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_dormindo.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_irritado.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_machucado.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_morto.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_piscando.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_respirando.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_ressaca.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_triste.json",
                "pet/sprites/Pingo/Adolescente/pingo_adolescente_vomitando.json",
                "pet/sprites/overlays/questionmark.json"
        ));

    private final ArrayList<VisibleCondition> vcAdolescente =
        new ArrayList<VisibleCondition>(Arrays.asList(
                BEBADO,
                BRAVO,
                PULANDO,
                CHORANDO,
                COMA_ALCOOLICO,
                COMENDO,
                DOENTE,
                DORMINDO,
                IRRITADO,
                MACHUCADO,
                MORTO,
                NORMAL,
                RESPIRANDO,
                RESSACA,
                TRISTE,
                VOMITANDO,
                UNDETERMINED
        ));


    private final ArrayList<String> imagesMeia_Idade =
        new ArrayList<String>(Arrays.asList(
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_chorando.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_vomitando.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_coma_alcoolico.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_comendo.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_machucado.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_morto.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_piscando.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_respirando.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_varrendo.png",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_vomitando.png",
                "pet/sprites/overlays/questionmark.png"
        ));

    private final ArrayList<String> jsonsMeia_Idade =
        new ArrayList<String>(Arrays.asList(
                // "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_andando.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_chorando.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_vomitando.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_coma_alcoolico.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_comendo.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_machucado.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_morto.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_piscando.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_respirando.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_varrendo.json",
                "pet/sprites/Pingo/Meia_Idade/pingo_meia_idade_vomitando.json",
                "pet/sprites/overlays/questionmark.json"
        ));

    private final ArrayList<VisibleCondition> vcMeia_Idade =
        new ArrayList<VisibleCondition>(Arrays.asList(
                CHORANDO,
                BEBADO,
                COMA_ALCOOLICO,
                COMENDO,
                MACHUCADO,
                MORTO,
                NORMAL,
                RESPIRANDO,
                VARRENDO,
                VOMITANDO,
                UNDETERMINED
        ));


    private final ArrayList< ArrayList<String> > images =
        new ArrayList< ArrayList<String> >(Arrays.asList(
                imagesBebe,
                imagesCrianca,
                imagesAdolescente,
                imagesAdolescente,  // XXX no adult, just use adolescent for now
                imagesMeia_Idade,
                imagesMeia_Idade,
                imagesMeia_Idade
        ));

    private final ArrayList< ArrayList<String> > jsons =
        new ArrayList< ArrayList<String> >(Arrays.asList(
                jsonsBebe,
                jsonsCrianca,
                jsonsAdolescente,
                jsonsAdolescente,
                jsonsMeia_Idade,
                jsonsMeia_Idade,
                jsonsMeia_Idade
        ));

    private final ArrayList< ArrayList<VisibleCondition > > vc =
        new ArrayList< ArrayList<VisibleCondition> >(Arrays.asList(
                vcBebe,
                vcCrianca,
                vcAdolescente,
                vcAdolescente,
                vcMeia_Idade,
                vcMeia_Idade,
                vcMeia_Idade
        ));

    AgeStage age_ = AgeStage.BEBE;

    int totalNumSprites_ = -1;

    public int totalNumSprites() {
        if (totalNumSprites_ != -1)
            return totalNumSprites_;
        int t = 0;
        for (int a = 0; a < jsons.size(); ++a)
            t += jsons.get(a).size();
        return (totalNumSprites_ = t);
    }

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == totalNumSprites();
    }

    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer


    // animMap_.get(age_).get(NORMAL)
    private EnumMap<AgeStage, EnumMap<VisibleCondition, Sprite> > animMap_ =
        new EnumMap<AgeStage, EnumMap<VisibleCondition, Sprite> > (AgeStage.class);
    private VisibleCondition currentVisibleCondition_;   // the current enum of the sprite animation

    public PetSpriter() {
        assert images.size() == jsons.size() && jsons.size() == vc.size();

        for (int a = 0; a < jsons.size(); a++) { // for each age stage
            EnumMap<VisibleCondition, Sprite> animMapForAge = new EnumMap<VisibleCondition, Sprite> (VisibleCondition.class);
            animMap_.put(AgeStage.values()[a], animMapForAge);
            for (int i = 0; i < jsons.get(a).size(); i++) {
                String spriteFnames = images.get(a).get(i);
                String jsonFnames   = jsons.get(a).get(i);
                printd("[petspriter] Loading sprite file: " + spriteFnames + jsonFnames);
                Sprite s = SpriteLoader.getSprite(spriteFnames, jsonFnames);
                //System.out.println("sprite true? : " + sprite == null + "i : " + i + vc.size());
                animMap_.get(AgeStage.values()[a]).put(vc.get(a).get(i), s);

                // Add a callback for when the image loads.
                // This is necessary because we can't use the width/height (to center the
                // image) until after the image has been loaded
                s.addCallback(new Callback<Sprite>() {
                    @Override
                    public void onSuccess(Sprite sprite) {
                        sprite.setSprite(0);
                        sprite.layer().setOrigin(0, 0);
                        sprite.layer().setTranslation(0, 0);
    //                    sprite.layer().setScale(4);
                        if (sprite == animMap_.get(age_).get(NORMAL))   // start with normal by default.
                            set(NORMAL);
                        else
                            sprite.layer().setVisible(false);
                        dprint("[petspriter] added, visible: " + sprite.layer().visible() + " full layer: " + animLayer_.visible());
                        animLayer_.add(sprite.layer());
                        numLoaded_++;
                    }

                    @Override
                    public void onFailure(Throwable err) {
                        log().error("Error loading image!", err);
                    }
                });
            }

            // Error check of internal structures - ifndef NDEBUG
            int n = VisibleCondition.values().length;
            boolean[] hasState = new boolean[n];
            for (int i = 0; i < vc.get(a).size(); ++i) {
                hasState[vc.get(a).get(i).ordinal()] = true;
            }
            for (int i = 0; i < n; ++i) {
                if (!hasState[i]) {
                    dprint("Warning: sprite file not specified for state " + VisibleCondition.values()[i] + " age " + age_);
                    dprint("         make sure this is rendered some other way");
                }
            }
        }
    }

    /**
     * Sets animation based on pet's current visible condition
     */
    public void set(VisibleCondition s) {
        Sprite newSprite = animMap_.get(age_).get(s);
        if (newSprite == null) {
            pprint("[petspriter.set] Warning: no direct anim for requested visibleCondition " + s + " age " + age_);
            // Handle a different way of animating this visible
            // condition (composite anims or synthetic or flump)
            //
            // setup quick and dirty handlers for now
            switch (s) {
                // reroute to some other available anim,
                // or just print
                case UNDETERMINED:
                    dprint("[petspriter.set] Error:  " + s + " visible condition shouldn't occur!");
                    break;
                // case COM_MOSQUITO:
                //  case COM_STINKY_MOSQUITO:
                //      System.out.println("[petspriter.set] mosquitim.. ");
                //      if (currentSprite_ == null)
                //          set(NORMAL);  // falback to normal or else keep sprite that was there before.
                //                        // the visible appearance will still be
                //                        // COM_MOSQUITOS, but we make it just look
                //                        // NORMAL for now
                //      return;
                case BRAVO:     // TODO: automatic fallback from an array ? this is just temporary anyway.
                case IRRITADO:
                    System.out.println("[petspriter.set] Warning:  using fallback anim TRISTE for: " + s + ".");
                    set(TRISTE);
                    return;
                case BEBADO_VOMITANDO:
                    System.out.println("[petspriter.set] Warning:  using fallback anim VOMITANDO for: " + s + ".");
                    set(VOMITANDO);
                    return;
                case COMA:
                    System.out.println("[petspriter.set] Warning:  using fallback anim COMA_ALCOOLICO for: " + s + ".");
                    set(COMA_ALCOOLICO);
                    return;
                case NORMAL_COM_VOMITO:
                    System.out.println("[petspriter.set] Warning: anim not available for now. using fallback anim VOMITANDO for " + s + ".");
                    set(VOMITANDO);
                    return;
                case NORMAL:
                    System.out.println("[petspriter.set] Warning: not even normal anim available! probably testing.. using fallback anim UNDETERMINED for " + s + ".");
                    set(UNDETERMINED);
                    return;
                case PULANDO:
                    System.out.println("[petspriter.set] Warning: using fallback anim UNDETERMINED for " + s + ".");
                    set(UNDETERMINED);
                    return;
                default:
                    System.out.println("[petspriter.set] Warning: using fallback anim UNDETERMINED for " + s + ".");
                    // dprinte("[petspriter.set] Error:  no fallback anim for: " + s + ".");
                    set(UNDETERMINED);
                    return;
            }
        }
        dprint("[petspriter.set] requested " + s + " visible condition");

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        currentVisibleCondition_ = s;
        setCurrentSprite(newSprite, 2f);
//         currentSprite_.layer().setVisible(false); // XXX
    }

    @Override
    public void set(int i) {
        set(VisibleCondition.values()[i]);
    }

    public void setClass(int i) {
        age_ = AgeStage.values()[i];
        set(currentVisibleCondition_);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (hasLoaded())
            dprint("[petSpriter] currentVisibleCondition: " + currentVisibleCondition_);
    }

    /**
     * Returns a slot which can be used to wire the class (eg, AgeStage) of the
     * current sprite animation to the emissions of a {@link Signal} or another value.
     * There is an animation set in a composite spriter. The PetSpriter has a
     * class which changes the animation set. This can be ageState, or style, etc.
     */
    public Slot<Integer> classSlot() {
        return new Slot<Integer>() {
            @Override public void onEmit (Integer value) {
                setClass(value);
            }
        };
    }

//    /**
//     * Return this PetSpriter's grouplayer which contains all sprites.
//     * At any one point, the current sprite's layer is visible, the others are
//     * invisible.
//     *
//     * TODO: make a sizable group layer of imagelayers.
//     */
//    public GroupLayer groupLayer() {
//        return animLayer_;
//    }
}
