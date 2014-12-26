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
import com.pulapirata.core.PetAttributes.State;
import static com.pulapirata.core.PetAttributes.State.*;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 * TODO: make commong parts between this and PetSpriter into common base class
 * CompositeSpriter
 */
public class MosquitoSpriter extends CompositeSpriter {
    private final String prefix = "pet/sprites/Pingo/Bebe/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_sujo_v2.png",
                "pingo_bebe_muito_sujo_v2.png",
                "pingo_bebe_muito_sujo_v2.png"
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_sujo_v2.json",
                "pingo_bebe_muito_sujo_v2.json",
                "pingo_bebe_muito_sujo_v2.json"
        ));

    private final ArrayList<State> vc =
        new ArrayList<State>(Arrays.asList(
                SUJO,
                MUITO_SUJO,
                IMUNDO
        ));

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == jsons.size();
    }

    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private EnumMap<State, Sprite> animMap_ = new EnumMap<State, Sprite> (State.class);
    private State currentTipoMosquito_ = SUJO;

    /**
     * Copy constructor for sharing resources with a another preallocated
     * spriter.
     */
    public MosquitoSpriter(MosquitoSpriter another) {
//        this.animMap_ = another.animMap_;


//        for (int i = 0; i < another.animLayer_.size(); ++i)
//            this.animLayer_.add(another.animLayer_.get(i));

//        this.set(NORMAL);
        // maybe recreate animLayer?

        // THE RIGHT WAY
        // - for each sprite,
        // - create a new sprite using copy constructor to reuse
        // another.sprite's SpriteImage
        // - we'll have a way of doing this for global atlases as well.
    }

    public MosquitoSpriter() {
        for (int i = 0; i < jsons.size(); i++) {
            String spriteFnames = prefix + images.get(i);
            String jsonFnames   = prefix + jsons.get(i);
            printd("[mosquitoSpriter] Loading sprite file: " + spriteFnames + jsonFnames);
            Sprite s = SpriteLoader.getSprite(spriteFnames, jsonFnames);
            //System.out.println("sprite true? : " + sprite == null + "i : " + i + vc.size());
            animMap_.put(vc.get(i), s);

            // Add a callback for when the image loads.
            // This is necessary because we can't use the width/height (to center the
            // image) until after the image has been loaded
            s.addCallback(new Callback<Sprite>() {
                @Override
                public void onSuccess(Sprite sprite) {
                    sprite.setSprite(0);
                    sprite.layer().setOrigin(0, 0);
                    sprite.layer().setTranslation(0, 0);
                    sprite.layer().setVisible(false);
                    dprint("[MosquitoSpriter] added, visible: " +
                        sprite.layer().visible() + " full layer: " + animLayer_.visible());
                    animLayer_.add(sprite.layer());
                    numLoaded_++;
                    if (hasLoaded())
                        set(currentTipoMosquito_);
                }

                @Override
                public void onFailure(Throwable err) {
                    log().error("Error loading image!", err);
                }
            });
        }

        // Error check of internal structures - ifndef NDEBUG
        int n = State.values().length;
        boolean[] hasState = new boolean[n];
        for (int i = 0; i < vc.size(); ++i) {
            hasState[vc.get(i).ordinal()] = true;
        }
        for (int i = 0; i < n; ++i) {
            if (!hasState[i]) {
                dprint("Warning: sprite file not specified for state " + State.values()[i]);
                dprint("         make sure this is rendered some other way");
            }
        }
    }

    void set(State s) {
        dprint("[mosquito] Requested TipoMosquito " + s);

        if (!hasLoaded())
            return;

        Sprite newSprite = animMap_.get(s);

        if (newSprite == null) {
            dprint("[mosquitospriter.set] Requested mosquitos for state " + s);
            dprint("[mosquitospriter.set]  assuming without mosquito.");
            currentSprite_.layer().setVisible(false);
            return;
        }
        currentTipoMosquito_ = s;

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        super.setCurrentSprite(newSprite, 1.7f);
    }

    @Override
    public void set(int i) {
        set(State.values()[i]);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (hasLoaded()) {
            dprint("[mosquito] currentTipoMosquito_: " + currentTipoMosquito_);
        }
    }
}
