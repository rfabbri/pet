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
     * The radius of the bounding sphere to the present sprite frame
     */
    public abstract float boundingRadius();

}
