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
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.Pet;
import com.pulapirata.core.PetWorld;
import com.pulapirata.core.PetAudio;
import static com.pulapirata.core.utils.Puts.*;
/**
 * Manages actions.
 * - runs for a specified duration
 * - actually contains action logic
 *   TODO: this will actually be ActionManager....
 */
class Action {
    private ActionState action_;
    public ActionState get() { return action_; }
    private PetAttributes pa_;
    public PetAttributes petAttributes() { return pa_; }

    private int duration_ = 5;   // default duration in coelhoSegundos
    public int duration() { return duration_; }
    public void setDuration(int d) { duration_ = d; }
    private double remaining_ = duration_;

    public void update(int delta) {
        pprint(String.format("[action] tick... time remaining %.2g", + remaining_));
        dprint("[action] delta, beatscoelhoseg " + delta + " " +
                PetWorld.beatsCoelhoSegundo_);
        remaining_ -= 1./(double)PetWorld.beatsCoelhoSegundo_;
        if (remaining_ <= 0) {
            remaining_ = 0;
            pa_.sAction().updateState(ActionState.DEFAULT);
            switch (action_) {
                case COMENDO:
                    PetAudio.burp.play();
                    break;
                default:
                    break;
            }
        }
    }

    public Action(ActionState action, PetAttributes pa) {
        action_ = action;
        pa_ = pa;
    }

    public boolean start() {
        pprint("[action] started " + action_ + " for duration " + duration_ + "s");
        remaining_ = duration_;
        pa_.sAction().updateState(action_);
//        try {
//            java.lang.Thread.sleep(1000);                 //1000 milliseconds is one second.
//        } catch(java.lang.InterruptedException ex) {
//            java.lang.Thread.currentThread().interrupt();
//        }
//        pa_.sAction().updateState(ActionState.DEFAULT);
        return true; // success
    }

    public void start(int duration) {
        setDuration(duration);
        start();
    }

    boolean wasInterrupted() { return false; /* TODO interruption not implemented for now */}

    boolean finished() { return remaining_ == 0; }
}
