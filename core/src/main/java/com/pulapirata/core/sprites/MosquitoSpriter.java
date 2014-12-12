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
public class MosquitoSpriter extends CompositeSpriter {
    private final String prefix = "pet/sprites/Pingo/Bebe/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_sujo_v2.png",
                "pingo_bebe_muito_sujo_v2.png"
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_sujo_v2.json",
                "pingo_bebe_muito_sujo_v2.png"
        ));

    private final ArrayList<VisibleCondition> vc =
        new ArrayList<VisibleCondition>(Arrays.asList(
                COM_MOSQUITO,
                COM_STINKY_MOSQUITO
        ));

    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private EnumMap<VisibleCondition, Sprite> animMap_ = new EnumMap<VisibleCondition, Sprite> (VisibleCondition.class);
    private VisibleCondition currentTipoMosquito_ = COM_MOSQUITO;

    /**
     * Copy constructor for sharing resources with a another preallocated
     * spriter.
     */
    public MosquitoSpriter(MosquitoSpriter another) {
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

    public MosquitoSpriter() {
        for (int i = 0; i < jsons.size(); i++) {
            String spriteFnames = prefix + images.get(i);
            String jsonFnames   = prefix + jsons.get(i);
            printd("[mosquitoSpriter] Loading sprite file: " + spriteFnames + jsonFnames);
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
                    dprint("[MosquitoSpriter] added, visible: " +
                        sprite.layer().visible() + " full layer: " + animLayer_.visible());
                    animLayer_.add(sprite.layer());
                    numLoaded_++;
                    if (hasLoaded())
                        set(currentTipoMosquito_);
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

    void set(VisibleCondition s) {
        currentTipoMosquito_ = s;
        pprint("[mosquito] TipoMosquito " + s);

        if (!hasLoaded())
            return;

        Sprite newSprite = animMap_.get(s);

        if (newSprite == null) {
            pprint("[mosquitospriter.set] Warning: no direct anim for requested visibleCondition " + s);
            pprint("[mosquitospriter.set] Warning: assuming without mosquito.");
        }

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        super.setCurrentSprite(newSprite, 1.3f);
    }

    @Override
    public void set(int i) {
        assert i < images.size() : "[mosquito] wrong index\n";
        set(VisibleCondition.values()[i]);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (hasLoaded()) {
            dprint("[mosquito] currentTipoMosquito_: " + currentTipoMosquito_);
        }
    }
}
