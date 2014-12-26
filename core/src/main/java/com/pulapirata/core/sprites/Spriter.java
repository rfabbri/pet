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

import playn.core.GroupLayer;
import playn.core.ImageLayer;

/**
 * A base class for classes managing a sprite. Supports a flipbook anim sequence
 * or series of sprite animations (many flipbook anim sequences indexing same
 * atlas)
 */
public abstract class Spriter {

    public abstract void update(int delta);

    /**
     * True if all assets (jsons, images/sheets) have been loaded
     */
    public abstract boolean hasLoaded();

    /**
     * Detatches from the layer. Hides the sprite from the layer.
     */
    public void detatch(GroupLayer layer) {
        layer.remove(layer());
    }

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    public abstract float boundingRadius();

    /**
     * Return the sprite {@link ImageLayer}.
     */
    public abstract GroupLayer.Clipped layer();
}
