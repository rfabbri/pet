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
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.VisibleCondition;
import static com.pulapirata.core.PetAttributes.VisibleCondition.*;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class to manage sets of Pet sprite animations.
 * PetSprite always refers to the same global game atlas.
 * It is just like Sprite, but changes internal animation
 * based on the set state.
 */
public class PetSpriter extends CompositeSpriter {
//    public static String IMAGE = "pet/sprites/atlas.png";
//    public static String JSON = "pet/sprites/atlas.json";

    private final String prefix = "pet/sprites/Pingo/Bebe/";
    private final ArrayList<String> images =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_bebado_v2.png",
                "pingo_bebe_chorando_v2.png",
                "pingo_bebe_coma_alcoolico_v2.png",
                "pingo_bebe_comendo_v2.png",
                "pingo_bebe_dormindo_v2.png",
                "pingo_bebe_doente_v2.png",
                "pingo_bebe_morto_v2.png",
                "pingo_bebe_piscando_v2.png",
                "pingo_bebe_pulando_v2.png",
                "pingo_bebe_triste_v2.png",
                "pingo_bebe_vomitando_v2.png",
                "pingo_bebe_varrendo_v2.png"
        ));

    private final ArrayList<String> jsons =
        new ArrayList<String>(Arrays.asList(
                "pingo_bebe_bebado_v2.json",
                "pingo_bebe_chorando_v2.json",
                "pingo_bebe_coma_alcoolico_v2.json",
                "pingo_bebe_comendo_v2.json",
                "pingo_bebe_dormindo_v2.json",
                "pingo_bebe_doente_v2.json",
                "pingo_bebe_morto_v2.json",
                "pingo_bebe_piscando_v2.json",
                "pingo_bebe_pulando_v2.json",
                "pingo_bebe_triste_v2.json",
                "pingo_bebe_vomitando_v2.json",
                "pingo_bebe_varrendo_v2.json"
        ));

    private final ArrayList<VisibleCondition> vc =
        new ArrayList<VisibleCondition>(Arrays.asList(
                BEBADO,
                CHORANDO,
                COMA_ALCOOLICO,
                COMENDO,
                DORMINDO,
                DOENTE,
                MORTO,
                NORMAL,
                PULANDO,
                TRISTE,
                VOMITANDO,
                VARRENDO
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
    private EnumMap<VisibleCondition, Sprite> animMap_ = new EnumMap<VisibleCondition, Sprite> (VisibleCondition.class);
    private VisibleCondition currentVisibleCondition_;   // the current enum of the sprite animation

    public PetSpriter() {
        for (int i = 0; i < jsons.size(); i++) {
            String spriteFnames = prefix + images.get(i);
            String jsonFnames   = prefix + jsons.get(i);
            printd("[petspriter] Loading sprite file: " + spriteFnames + jsonFnames);
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
//                    sprite.layer().setScale(4);
                    if (sprite == animMap_.get(NORMAL))   // start with normal by default.
                        set(NORMAL);
                    else
                        sprite.layer().setVisible(false);
                    dprint("[petspriter] added, visible: " + sprite.layer().visible() + " full layer: " + animLayer_.visible());
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
        int n = VisibleCondition.values().length;
        boolean[] hasState = new boolean[n];
        for (int i = 0; i < vc.size(); ++i) {
            hasState[vc.get(i).ordinal()] = true;
        }
        for (int i = 0; i < n; ++i) {
            if (!hasState[i]) {
                dprint("Warning: sprite file not specified for state " + VisibleCondition.values()[i]);
                dprint("         make sure this is rendered some other way");
            }
        }
    }

    /**
     * Sets animation based on pet's current visible condition
     */
    public void set(VisibleCondition s) {
        Sprite newSprite = animMap_.get(s);
        if (newSprite == null) {
            pprint("[petspriter.set] Warning: no direct anim for requested visibleCondition " + s);
            // Handle a different way of animating this visible
            // condition (composite anims or synthetic or flump)
            //
            // setup quick and dirty handlers for now
            switch (s) {
                // reroute to some other available anim,
                // or just print
                case UNDETERMINED:
                    dprint("[petspriter.set] Error:  " + s + " visible condition shouldn't occur!");
                    set(MORTO);  // ideally have a ? sprite for UNDETERMINED, but better hide this from users.
                    return;
                case COM_MOSQUITO:
                case COM_STINKY_MOSQUITO:
                    System.out.println("[petspriter.set] mosquitim.. ");
                    if (currentSprite_ == null)
                        set(NORMAL);  // falback to normal or else keep sprite that was there before.
                                      // the visible appearance will still be
                                      // COM_MOSQUITOS, but we make it just look
                                      // NORMAL for now
                    return;
                case BRAVO:     // TODO: automatic fallback from an array ? this is just temporary anyway.
                case IRRITADO:
                    System.out.println("[petspriter.set] Warning:  using fallback anim TRISTE for: " + s + ".");
                    set(TRISTE);
                    return;
                case COMA:
                    System.out.println("[petspriter.set] Warning:  using fallback anim COMA_ALCOOLICO for: " + s + ".");
                    set(COMA_ALCOOLICO);
                    return;
                case NORMAL_COM_VOMITO:
                    System.out.println("[petspriter.set] Warning: anim not available for now. using fallback anim VOMITANDO for " + s + ".");
                    set(VOMITANDO);
                    return;
                default:
                    dprinte("[petspriter.set] Error:  no fallback anim for: " + s + ".");
                    break;
            }
        }

        if (currentSprite_ != null)  // only happens during construction / asset loadding
            currentSprite_.layer().setVisible(false);

        traversed_ = false;
        // switch currentAnim to next anim
        spriteIndex_ = 0;

        currentSprite_ = newSprite;
        currentVisibleCondition_ = s;
        animLayer_.setSize(currentSprite_.maxWidth(), currentSprite_.maxHeight()); // where to clip the animations in this composite spritey
        animLayer_.setScale(2f); // increase the scale of the sprite for testing
        animLayer_.setOrigin(animLayer_.width() / 2f, animLayer_.height() / 2f);
        currentSprite_.layer().setVisible(true);
    }

    @Override
    public void set(int i) {
        set(VisibleCondition.values()[i]);
    }

    @Override
    public void update(int delta) {
        super.update(delta);
        if (hasLoaded())
            dprint("[petSpriter] currentVisibleCondition: " + currentVisibleCondition_);
    }

//    /**
//     * Return this PetSpriter's grouplayer which contains all sprites.
//     * At any one point, the current sprite's layer is visible, the others are
//     * invisible.
//     *
//     * TODO: make a sizable group layer of imagelayers.
//     */
//    public GroupLayer groupLayer() {
//        return animLayer_;
//    }
}
