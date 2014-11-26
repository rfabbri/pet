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
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 */
public class DroppingSpriter extends Spriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/Pingo/Bebe/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_dejeto.png",
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_dejeto.json",
        ));

    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private Sprite currentSprite_;   // the current sprite animation
    private int spriteIndex_ = 0;
    private int numLoaded_ = 0; // set to num of animations when resources have loaded and we can update
    private boolean traversed_ = false;
    protected GroupLayer.Clipped pooLayer_ = PlayN.graphics().createGroupLayer(0, 0);

    public DroppingSpriter() {
        for (int i = 0; i < jsons.size(); i++) {
            String spriteFnames = prefix + images.get(i);
            String jsonFnames   = prefix + jsons.get(i);
            printd("[droppingSpriter] Loading sprite file: " + spriteFnames + jsonFnames);
            Sprite s = SpriteLoader.getSprite(spriteFnames, jsonFnames);

            // Add a callback for when the image loads.
            // This is necessary because we can't use the width/height (to center the
            // image) until after the image has been loaded
            s.addCallback(new Callback<Sprite>() {
                @Override
                public void onSuccess(Sprite sprite) {
                    sprite.setSprite(0);
                    sprite.layer().setOrigin(0, 0);
                    sprite.layer().setTranslation(0, 0);
                    set();
                    pooLayer_.add(sprite.layer());

                    numLoaded_++;
                }

                @Override
                public void onFailure(Throwable err) {
                    log().error("Error loading image!", err);
                }
            });
        }
    }

    void set() {
        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        traversed_ = false;
        // switch currentAnim to next anim
        spriteIndex_ = 0;

        currentSprite_ = newSprite;
        petLayer_.setSize(currentSprite_.maxWidth(), currentSprite_.maxHeight()); // where to clip the animations in this composite spritey
        petLayer_.setScale(2f); // increase the scale of the sprite for testing
        petLayer_.setOrigin(petLayer_.width() / 2f, petLayer_.height() / 2f);
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

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == jsons.size();
    }

    public void update(int delta) {
        if (hasLoaded()) {
            dprint( "[droppingSpriter] loaded & being updated.");
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
                pooLayer_.width()*petLayer_.width() +
                pooLayer_.height()*petLayer_.height())/2.0f;
    }

    private boolean traversed(){
       return traversed_;
    }

    /**
     * Return the current animation sprite {@link ImageLayer}.
     */
    @Override
    public GroupLayer.Clipped layer() {
        return pooLayer_;
    }
}
