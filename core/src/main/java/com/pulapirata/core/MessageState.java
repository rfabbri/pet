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
import java.util.EnumMap;
import java.util.Map;
import react.Value;
import react.Functions;
import react.Function;
import com.pulapirata.core.PetAttributeState;
import com.pulapirata.core.PetAttributes.State;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A message that reacts to PetAttributestates.
 * The message strings reflect a specific PetAttributeState.
 */
class MessageState extends Message {
    PetAttributeState as_;
    /**
     * Construct with a text message for each state in {@link PetAttributeState} {@param as}.
     * There must be one state message string per state. When there is no
     * message for a given state, a null string must be provided.
     */
    MessageState(EnumMap<State, String> stateMessages, PetAttributeState as) {
        assert as.isInitialized();
        as.map( forMapInt(stateMessages, "[messageState] error: no message for state") ).connect(text_.slot());
        as.updateForce(as.get());
        as_ = as;
    }

    /**
     * Returns a function which performs a map lookup with a default value. The function created by
     * this method returns defaultValue for all inputs that do not belong to the map's key set.
     */
    public static Function<Integer, String> forMapInt (final EnumMap<State, String> map, final String defaultValue)
    {
        return new Function<Integer, String>() {
            public String apply (Integer id) {
                String value = map.get(State.values()[id]);
                //if (value == null) {
                    dprint("[messageState] id: " + id + " state: " + State.values()[id] + " value: " + value);
                //}

                return (value != null || map.containsKey(State.values()[id])) ? value : defaultValue;
            }
        };
    }
}
