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
import react.Value;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A Class to store messages to be displayed in the game.
 * It is a reactive string that can wire itself to certain events of the game,
 * such as changing PetAttributeState
 */
public class Message {
    /** reactive string that can send/receive signals */
    public Value<String> text_ = Value.create(null);
    private int priority_;

    public Message() {}
    /** Creates a message with the supplied text */
    public Message(String m) { text_.update(m); }
    public String get() { return text_.get(); }
    public void setMessage(String m) { text_.update(m); }
    public boolean isEmpty() { return text_.get().equals(""); }
    public int priority() { return priority_; }
    public void setPriority(int p) { priority_ = p; }
    public void remove() { setMessage(""); }
    @Override public String toString() { return get(); }
}
