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

import react.IntValue;
import react.Slot;


/**
 * An attribute class that just stores qualitiative/partition values.
 * The values are stored in a reactive int which is the ordinal value of an
 * enum.
 */
public class PetAttributeEnum<State extends Enum<State>> extends IntValue {
    /**
     * start with easily identifiable dummy default values
     */
    public PetAttributeEnum() {
        super(-696);
    }

    public State getState() {
        return s_;
    }

    /**
     * Convenience method
     */
    public State updateState(State s) {
        s_ = s;
        updateInt(s.ordinal());
        return s;
    }

    public void print() {
        System.out.println(java.util.Arrays.asList(this.s_.getClass().getEnumConstants()));
    }

    public State s_;
}
