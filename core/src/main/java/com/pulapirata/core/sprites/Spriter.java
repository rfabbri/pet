package com.pulapirata.core.sprites;

/**
 * A base class for classes managing a sprite. Supports a flipbook anim sequence
 * or series of sprite animations (many flipbook anim sequences indexing same
 * atlas)
 */
public abstract class Spriter {

    public void update(int delta);

    /**
     * True if all assets (jsons, images/sheets) have been loaded
     */
    public boolean hasLoaded();

    /**
     * Detatches from the layer. Hides the sprite from the layer.
     */
    public void detatch(GroupLayer layer);

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    public float boundingRadius();
}
