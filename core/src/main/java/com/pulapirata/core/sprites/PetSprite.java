package com.pulapirata.core.sprites;

import static com.pulapirata.core.PetAttributes.VisibleCondition;
import static com.pulapirata.core.PetAttributes.VisibleCondition.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 */
public class PetSprite {
    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private HashMap<VisibleCondition, SpriteBase> animMap;
    private SpriteBase sprite;   // the current sprite animation
    private int spriteIndex = 0;


    /** pointer to attributes (mainly to get visible condition) */
    PetAttributes attribs;

    /**
     * Sets animation based on pet's current visible condition
     */
    public void set(attribs.VisibleCondition s) {
        // switch currentAnim to next anim
        spriteIndex = 0;
        sprite = animMap.get(s);
    }

    public void update(int delta) {
        if (hasLoaded) {
            spriteIndex = (spriteIndex + 1) % sprite.numSprites();
            sprite.setSprite(spriteIndex);
            // sprite.layer().setRotation(angle);
            if (spriteIndex == sprite.numSprites() - 1) {
                traversed = true;
            }
        }
    }

    public void detatch(GroupLayer petLayer) {
        petLayer.remove(sprite.layer());
    }

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    public float boundingRadius() {
        return 1.0 + Math.sqrt(sprite.width()*sprite.width() + sprite.height().sprite.height());
    }
}
