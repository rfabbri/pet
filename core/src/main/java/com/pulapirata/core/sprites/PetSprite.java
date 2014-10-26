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
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                    "pingo_bebe_pulando.png",
                    "pingo_bebe_vomitando.png"
                    // XXX
                    ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                    "pingo_bebe_pulando.json",
                    "pingo_bebe_vomitando.json"
                    // XXX
                    ));

    private final ArrayList<VisibleCondition> vc =
        new ArrayList<String>(Arrays.asList(
                    PULANDO,
                    VOMITANDO
                    // XXX
                    ));

    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private HashMap<VisibleCondition, Sprite> animMap;
    private Sprite sprite;   // the current sprite animation
    private int spriteIndex = 0;
    private int numLoaded = 0; // set to num of animations when resources have loaded and we can update
    private boolean traversed = false;

    /** pointer to attributes (mainly to get visible condition) */
    PetAttributes attribs;

    public PetSprite(final Grouplayer petLayer, float x, float y) {
        for (int i = 0; i < jsons.size(); i++) {
            sprite = SpriteLoader.getSprite(prefix + images.get(i), prefix + jsons.get(i))
            animMap.put(vc.get(i), sprite);

            // Add a callback for when the image loads.
            // This is necessary because we can't use the width/height (to center the
            // image) until after the image has been loaded
            sprite.addCallback(new Callback<Sprite>() {
                @Override
                public void onSuccess(Sprite sprite) {
                    sprite.setSprite(spriteIndex);
                    sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                    sprite.layer().setTranslation(x, y);
                    petLayer.add(sprite.layer());
                    numLoaded = has_loaded + 1;
                }

                @Override
                public void onFailure(Throwable err) {
                    log().error("Error loading image!", err);
                }
            });
        }
    }

    /**
     * Sets animation based on pet's current visible condition
     */
    public void set(attribs.VisibleCondition s) {
        // switch currentAnim to next anim
        spriteIndex = 0;
        sprite = animMap.get(s);
    }

    public boolean hasLoaded() {
        return numLoaded == jsons.size();
    }

    public void update(int delta) {
        if (hasLoaded()) {
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
    public boolean getTraversed(){
       return traversed;
    }
}
