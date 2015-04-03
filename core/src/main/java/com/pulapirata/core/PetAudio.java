/**
 * Pet - a comic pet simulator game
 * Copyright (C) 2013-2015 Ricardo Fabbri and Edson "Presto" Correa
 *
 * This program is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version. A different license may be requested
 * to the copyright holders.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>
 *
 * NOTE: this file may contain code derived from the PlayN, Tripleplay, and
 * React libraries, all (C) The PlayN Authors or Three Rings Design, Inc.  The
 * original PlayN is released under the Apache, Version 2.0 License, and
 * TriplePlay and React have their licenses listed in COPYING-OOO. PlayN and
 * Three Rings are NOT responsible for the changes found herein, but are
 * thankfully acknowledged.
 */
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
    public static Loop varrendo = fxSoundBoard.getLoop("pet/audio/fx/varrendopet"); //sweep?
    public static Clip pulo = fxSoundBoard.getClip("pet/audio/fx/pulopet");
    public static Clip botao = fxSoundBoard.getClip("pet/audio/fx/botao1pet");
    public static Loop coma = fxSoundBoard.getLoop("pet/audio/fx/comapet");
    public static Clip poo = fxSoundBoard.getClip("pet/audio/fx/pumpet");
    public static Clip slipper = fxSoundBoard.getClip("pet/audio/fx/chineladapet");
    public static Loop cellPhone = fxSoundBoard.getLoop("pet/audio/fx/celularpet");
    public static Loop eating = fxSoundBoard.getLoop("pet/audio/fx/comendopet");
    public static Loop TV = fxSoundBoard.getLoop("pet/audio/fx/tvpet");
    public static Loop mainMusic = musicSoundBoard.getLoop("pet/audio/music/POL-two-fat-gangsters-short");
//    public static Loop mainMusic = musicSoundBoard.getLoop("pet/audio/music/POL-snowy-hill-short");

    /**
     * Prohibit instantiation
     */
    private PetAudio() {
    }

    public static void init() {
        mainMusic.play();
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
                        try {
                            java.lang.Thread.sleep(500);                 //1000 milliseconds is one second.
                        } catch(java.lang.InterruptedException ex) {
                            java.lang.Thread.currentThread().interrupt();
                        }
                        hickup.play();
                        break;
                    case COMA_ALCOOLICO:
                        coma.play();
                        break;
                    default:
                        pprint("[audio] no audio in alcool state " + s);
                        break;
                }
            }
        };
    }
}
