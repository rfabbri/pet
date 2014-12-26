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

import react.Slot;
import playn.core.PlayN;
import playn.core.GroupLayer;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A base class for classes managing a composite sprite.
 * It consists of a series of indexed sprite animations (many flipbook anim sequences
 * indexing same atlas)
 */
public abstract class CompositeSpriter extends Spriter {

    protected Sprite currentSprite_;   // the current sprite animation
    protected int spriteIndex_ = 0;
    protected int numLoaded_ = 0; // set to num of animations when resources have loaded and we can update
    protected boolean traversed_ = false;
    protected GroupLayer.Clipped animLayer_ = PlayN.graphics().createGroupLayer(0, 0);

    @Override
    public void update(int delta) {
        if (hasLoaded()) {
            dprint( "[compositeSpriter] loaded & being updated.");
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

    /**
     * Return the current animation sprite {@link ImageLayer}.
     */
    @Override
    public GroupLayer.Clipped layer() {
        return animLayer_;
    }

    public abstract void set(int i);

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

    private boolean traversed(){
       return traversed_;
    }

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    @Override
    public float boundingRadius() {
        return (float) Math.sqrt(
                animLayer_.width()*animLayer_.width() +
                animLayer_.height()*animLayer_.height())/2.0f;
    }

    /**
     * TODO perhaps scale should not be here.
     */
    protected void setCurrentSprite(Sprite newSprite, float scale) {
        traversed_ = false;
        // switch currentAnim to next anim
        spriteIndex_ = 0;
        currentSprite_ = newSprite;
        animLayer_.setSize(currentSprite_.maxWidth(), currentSprite_.maxHeight()); // where to clip the animations in this composite spritey
        animLayer_.setScale(scale); // change the scale of the sprite for testing
        animLayer_.setOrigin(animLayer_.width() / 2f, animLayer_.height() / 2f);
        currentSprite_.layer().setVisible(true);
    }
}
