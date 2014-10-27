package com.pulapirata.core.sprites;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import react.Slot;
import playn.core.GroupLayer;
import playn.core.ImageLayer;
import playn.core.PlayN;
import playn.core.util.Callback;
import static playn.core.PlayN.log;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.VisibleCondition;
import static com.pulapirata.core.PetAttributes.VisibleCondition.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 */
public class PetSpriter extends Spriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_bebado.png",
                "pingo_bebe_chorando.png",
                "pingo_bebe_coma.png",
                "pingo_bebe_comendo.png",
                "pingo_bebe_dormindo.png",
                "pingo_bebe_febre.png",
                "pingo_bebe_morto.png",
                "pingo_bebe_piscando.png",
                "pingo_bebe_pulando.png",
                "pingo_bebe_triste.png",
                "pingo_bebe_vomitando.png"
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_bebado.json",
                "pingo_bebe_chorando.json",
                "pingo_bebe_coma.json",
                "pingo_bebe_comendo.json",
                "pingo_bebe_dormindo.json",
                "pingo_bebe_febre.json",
                "pingo_bebe_morto.json",
                "pingo_bebe_piscando.json",
                "pingo_bebe_pulando.json",
                "pingo_bebe_triste.json",
                "pingo_bebe_vomitando.json"
        ));

    private final ArrayList<VisibleCondition> vc =
        new ArrayList<VisibleCondition>(Arrays.asList(
                BEBADO,
                CHORANDO,
                COMA,
                COMENDO,
                DORMINDO,
                FEBRE,
                MORTO,
                NORMAL,
                PULANDO,
                TRISTE,
                VOMITANDO
        ));

    // Atualmente nao tem sprite antigo para estes:
    //        IRRITADO,
    //        BRAVO,
    //        DOENTE,
    //        MACHUCADO,
    //        MUITO_MACHUCADO,
    //        COMA_ALCOOLICO,
    //        COM_MOSQUITO,
    //        COM_STINKY_MOSQUITO,
    //        UNDETERMINED
    // Novo nao tem:
    //        MACHUCADO,
    //        MUITO_MACHUCADO,
    //        NORMAL_COM_VOMITO,
    //        BEBADO_VOMITANDO,


    // all member animations(sprites) should have same atlas as source,
    // as built in PetSpriteLoader.java, and also the same layer
    private HashMap<VisibleCondition, Sprite> animMap_ = new HashMap<VisibleCondition, Sprite> ();
    private Sprite currentSprite_;   // the current sprite animation
    private int spriteIndex_ = 0;
    private int numLoaded_ = 0; // set to num of animations when resources have loaded and we can update
    private boolean traversed_ = false;
    protected GroupLayer.Clipped petLayer_;

    public PetSpriter(final GroupLayer stageLayer, final float x, final float y) {
        stageLayer.add(petLayer_);
        for (int i = 0; i < jsons.size(); i++) {
            Sprite s = SpriteLoader.getSprite(prefix + images.get(i), prefix + jsons.get(i));
            //System.out.println("sprite true? : " + sprite == null + "i : " + i + vc.size());
            animMap_.put(vc.get(i), s);

            // Add a callback for when the image loads.
            // This is necessary because we can't use the width/height (to center the
            // image) until after the image has been loaded
            s.addCallback(new Callback<Sprite>() {
                @Override
                public void onSuccess(Sprite sprite) {
                    sprite.setSprite(spriteIndex_);
                    sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
                    sprite.layer().setTranslation(x, y);
                    petLayer_.add(sprite.layer());
                    if (sprite == animMap_.get(NORMAL)) {
                        petLayer_ = PlayN.graphics().createGroupLayer(sprite.width(), sprite.height());
                        set(NORMAL);
                    } else {
                        sprite.layer().setVisible(false);
                    }
                    numLoaded_++;
                }

                @Override
                public void onFailure(Throwable err) {
                    log().error("Error loading image!", err);
                }
            });
        }

        // Error check of internal structures - ifndef NDEBUG
        int n = VisibleCondition.values().length;
        boolean[] hasState = new boolean[n];
        for (int i = 0; i < vc.size(); ++i) {
            hasState[vc.get(i).ordinal()] = true;
        }
        for (int i = 0; i < n; ++i) {
            if (!hasState[i]) {
                System.out.println("Warning: sprite file not specified for state " + VisibleCondition.values()[i]);
                System.out.println("         make sure this is rendered some other way");
            }
        }
    }

    /**
     * Sets animation based on pet's current visible condition
     */
    public void set(VisibleCondition s) {
        // switch currentAnim to next anim
        spriteIndex_ = 0;
        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);
        currentSprite_ = animMap_.get(s);
        if (currentSprite_ == null) {
            System.out.println("Warning: no direct anim for requested visibleCondition " + s);
            // Handle a different way of animating this visible
            // condition (composite anims or synthetic or flump)
            //
            // setup quick and dirty handlers for now
            switch (s) {
                // reroute to some other available anim,
                // or just print
                case UNDETERMINED:
                    System.out.println("Error:  " + s + " visible condition shouldn't occur!");
                    set(MORTO);  // ideally have a ? sprite for UNDETERMINED, but better hide this from users.
                    break;
                default:
            }
        }

        petLayer_.setSize(currentSprite_.width(), currentSprite_.height());
        currentSprite_.layer().setVisible(true);
    }

    public void set(int i) {
        set(VisibleCondition.values()[i]);
    }

    @Override
    public boolean hasLoaded() {
        return numLoaded_ == jsons.size();
    }

    public void update(int delta) {
        if (hasLoaded()) {
            spriteIndex_ = (spriteIndex_ + 1) % currentSprite_.numSprites();
            currentSprite_.setSprite(spriteIndex_);
            // currentSprite_.layer().setRotation(angle);
            if (spriteIndex_ == currentSprite_.numSprites() - 1) {
                traversed_ = true;
            }
        }
    }

    /**
     * The radius of the bounding sphere to the present sprite frame
     */
    public float boundingRadius() {
        return 1.0f + (float) Math.sqrt(
                currentSprite_.width()*currentSprite_.width() +
                currentSprite_.height()*currentSprite_.height());
    }
    public boolean getTraversed_(){
       return traversed_;
    }

    /**
     * Returns a slot which can be used to wire the current sprite animation to
     * the emissions of a {@link Signal} or another value.
     */
    public Slot<Integer> slot () {
        return new Slot<Integer> () {
            @Override public void onEmit (Integer value) {
                set(value);
            }
        };
    }

    /**
     * Return the current animation sprite {@link ImageLayer}.
     */
    @Override
    public GroupLayer.Clipped layer() {
        return petLayer_;
    }

//    /**
//     * Return this PetSpriter's grouplayer which contains all sprites.
//     * At any one point, the current sprite's layer is visible, the others are
//     * invisible.
//     *
//     * TODO: make a sizable group layer of imagelayers.
//     */
//    public GroupLayer groupLayer() {
//        return petLayer_;
//    }
}
