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
import com.pulapirata.core.PetAttributes.TipoCoco;
import static com.pulapirata.core.PetAttributes.TipoCoco.*;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 * TODO: make commong parts between this and PetSpriter into common base class
 * CompositeSpriter
 */
public class DroppingSpriter extends Spriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/dejetos/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                  "dejetos_coco.png",
                  "dejetos_cocomole.png"
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                  "dejetos_coco.json",
                  "dejetos_cocomole.json"
        ));

    private final ArrayList<TipoCoco> vc =
        new ArrayList<TipoCoco>(Arrays.asList(
                    NORMAL,
                    MOLE
        ));


    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private EnumMap<TipoCoco, Sprite> animMap_ = new EnumMap<TipoCoco, Sprite> (TipoCoco.class);
    private Sprite currentSprite_;   // the current sprite animation
    private TipoCoco currentTipoCoco_ = NORMAL;
    private int spriteIndex_ = 0;
    private int numLoaded_ = 0; // set to num of animations when resources have loaded and we can update
    private boolean traversed_ = false;
    protected GroupLayer.Clipped animLayer_ = PlayN.graphics().createGroupLayer(0, 0);

    /**
     * Copy constructor for sharing resources with a another preallocated
     * spriter.
     */
    public DroppingSpriter(DroppingSpriter another) {
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

    public DroppingSpriter() {
        for (int i = 0; i < jsons.size(); i++) {
            String spriteFnames = prefix + images.get(i);
            String jsonFnames   = prefix + jsons.get(i);
            printd("[droppingSpriter] Loading sprite file: " + spriteFnames + jsonFnames);
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
                    dprint("[droppingSpriter] added, visible: " +
                        sprite.layer().visible() + " full layer: " + animLayer_.visible());
                    animLayer_.add(sprite.layer());
                    numLoaded_++;
                    if (hasLoaded())
                        set(currentTipoCoco_);
                }

                @Override
                public void onFailure(Throwable err) {
                    log().error("Error loading image!", err);
                }
            });
        }

        // Error check of internal structures - ifndef NDEBUG
        int n = TipoCoco.values().length;
        boolean[] hasState = new boolean[n];
        for (int i = 0; i < vc.size(); ++i) {
            hasState[vc.get(i).ordinal()] = true;
        }
        for (int i = 0; i < n; ++i) {
            if (!hasState[i]) {
                dprint("Warning: sprite file not specified for state " + TipoCoco.values()[i]);
                dprint("         make sure this is rendered some other way");
            }
        }
    }

    public void set(TipoCoco s) {
        currentTipoCoco_ = s;
        dprint("[poo] TipoCoco " + s);

        if (!hasLoaded())
            return;

        Sprite newSprite = animMap_.get(s);

        if (newSprite == null) {
            pprint("[poo] Warning: no direct anim for requested TipoCoco " + s);
        }

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        traversed_ = false;
        // switch currentAnim to next anim
        spriteIndex_ = 0;

        currentSprite_ = newSprite;
        animLayer_.setSize(currentSprite_.maxWidth(), currentSprite_.maxHeight()); // where to clip the animations in this composite spritey
        animLayer_.setScale(1.3f); // change the scale of the sprite for testing
        animLayer_.setOrigin(animLayer_.width() / 2f, animLayer_.height() / 2f);
        currentSprite_.layer().setVisible(true);
    }

    /**
     * Flips horizontally
     */
    public void flipLeft() {
        currentSprite_.layer().setScaleX(-1);
        currentSprite_.layer().setTx(currentSprite_.width());
    }

    public void flipRight() {
        currentSprite_.layer().setScaleX(1);
        currentSprite_.layer().setTx(0);
    }

    public void set(int i) {
        set(TipoCoco.values()[i]);
    }

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == jsons.size();
    }

    public void update(int delta) {
        if (hasLoaded()) {
            dprint( "[droppingSpriter] loaded & being updated.");
            dprint(" currentTipoCoco_: " + currentTipoCoco_);
            dprint(" initial-spriteIndex_: " + spriteIndex_);
            dprint(" initial-currentSprite_.numSprites(): " + currentSprite_.numSprites());
            spriteIndex_ = (spriteIndex_ + 1) % currentSprite_.numSprites();
            currentSprite_.setSprite(spriteIndex_);
            // currentSprite_.layer().setRotation(angle);
            if (spriteIndex_ == currentSprite_.numSprites() - 1) {
                traversed_ = true;
            }
            dprint("spriteIndex_: " + spriteIndex_ +
                   " currentSprite_.numSprites(): " + currentSprite_.numSprites());
        }
    }

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    public float boundingRadius() {
        return (float) Math.sqrt(
                animLayer_.width()*animLayer_.width() +
                animLayer_.height()*animLayer_.height())/2.0f;
    }

    private boolean traversed(){
       return traversed_;
    }

    /**
     * Returns a slot which can be used to wire the current sprite animation to
     * the emissions of a {@link Signal} or another value.
     */
    public Slot<Integer> slot() {
        return new Slot<Integer>() {
            @Override public void onEmit (Integer value) {
                set(value);
            }
        };
    }

    /**
     * Return the current animation sprite {@link ImageLayer}.
     */
    @Override
    public GroupLayer.Clipped layer() {
        return animLayer_;
    }
}
