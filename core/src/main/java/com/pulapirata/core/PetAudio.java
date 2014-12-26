package com.pulapirata.core;
import tripleplay.sound.SoundBoard;
import tripleplay.sound.Clip;
import tripleplay.sound.Loop;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Singleton class to globally manage the game's audio
 */
class PetAudio {
    public static SoundBoard musicSoundBoard = new SoundBoard();
    public static SoundBoard fxSoundBoard = new SoundBoard();
    public static Clip burp = musicSoundBoard.getClip("pet/audio/fx/arroto_01");
    public static Clip ding = musicSoundBoard.getClip("pet/audio/fx/ding");
    public static Loop mainMusic = musicSoundBoard.getLoop("pet/audio/fx/ding");

    /**
     * Prohibit instantiation
     */
    private PetAudio() {
    }

    public static void init() {
        mainMusic.play();
    }
}
