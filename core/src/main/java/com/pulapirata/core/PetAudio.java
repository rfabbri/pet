package com.pulapirata.core;
import react.Slot;
import tripleplay.sound.SoundBoard;
import tripleplay.sound.Clip;
import tripleplay.sound.Loop;
import com.pulapirata.core.PetAttributes.State;
import static com.pulapirata.core.PetAttributes.State.*;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Singleton class to globally manage the game's audio
 */
class PetAudio {
    public static SoundBoard musicSoundBoard = new SoundBoard();
    public static SoundBoard fxSoundBoard = new SoundBoard();
    public static Clip burp = fxSoundBoard.getClip("pet/audio/fx/arroto_01");
    public static Clip ding = fxSoundBoard.getClip("pet/audio/fx/ding");
    public static Clip hickup = fxSoundBoard.getClip("pet/audio/fx/soluco_01");
    public static Loop mainMusic = musicSoundBoard.getLoop("pet/audio/fx/ding");

    /**
     * Prohibit instantiation
     */
    private PetAudio() {
    }

    public static void init() {
        //mainMusic.play();
        hickup.preload();
        burp.preload();
        ding.preload();
    }

    public static void update (int delta) {
        //musicSoundBoard.update(delta);
        fxSoundBoard.update(delta);
    }

    /**
     * Returns a slot which can be used to wire the class (eg, State) of the
     * of an attribute in PetAttributes to a {@link Signal} or another value.
     */
    public static Slot<Integer> stateSlot() {
        return new Slot<Integer>() {
            @Override public void onEmit (Integer value) {
                pprint("[audio] tst");
                State s = State.values()[value];
                switch (s) {
                    case BEBADO:
                    case MUITO_BEBADO:
                        pprint("[audio] playing hickup");
                        hickup.play();
                        break;
                    default:
                        pprint("[audio] no audio in alcool state " + s);
                        break;
                }
            }
        };
    }
}
