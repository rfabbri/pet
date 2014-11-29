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
public class DroppingSpriter extends CompositeSpriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/dejetos/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                  "dejetos_coco.png",  // placeholder, for testing.
                  "dejetos_cocomole.png"   // placeholder, for testing.
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                  "dejetos_coco.json",  // placeholder, for testing.
                  "dejetos_cocomole.json"   // placeholder, for testing.
        ));

    private final ArrayList<TipoCoco> vc =
        new ArrayList<TipoCoco>(Arrays.asList(
                    NORMAL,
                    MOLE
        ));


    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private EnumMap<TipoCoco, Sprite> animMap_ = new EnumMap<TipoCoco, Sprite> (TipoCoco.class);
    private TipoCoco currentTipoCoco_;

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
                    if (sprite == animMap_.get(NORMAL))   // start with normal by default.
                        set(NORMAL);
                    else
                        sprite.layer().setVisible(false);
                    dprint("[droppingSpriter] added, visible: " +
                        sprite.layer().visible() + " full layer: " + animLayer_.visible());
                    animLayer_.add(sprite.layer());
                    numLoaded_++;
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

    void set(TipoCoco s) {
        Sprite newSprite = animMap_.get(s);


        if (newSprite == null) {
            pprint("[petspriter.set] Warning: no direct anim for requested visibleCondition " + s);
        }

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        traversed_ = false;
        // switch currentAnim to next anim
        spriteIndex_ = 0;

        currentSprite_ = newSprite;
        currentTipoCoco_ = s;
        animLayer_.setSize(currentSprite_.maxWidth(), currentSprite_.maxHeight()); // where to clip the animations in this composite spritey
        animLayer_.setScale(1.3f); // change the scale of the sprite for testing
        animLayer_.setOrigin(animLayer_.width() / 2f, animLayer_.height() / 2f);
        currentSprite_.layer().setVisible(true);
    }


    @Override
    public void set(int i) {
        set(TipoCoco.values()[i]);
    }

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == jsons.size();
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (hasLoaded())
            dprint("[droppingSpriter] currentTipoCoco_: " + currentTipoCoco_);
    }
}
