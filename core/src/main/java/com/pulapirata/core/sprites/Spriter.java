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
