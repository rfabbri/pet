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
//
// Triple Play - utilities for use in PlayN-based games
// Copyright (c) 2011-2012, Three Rings Design, Inc. - All rights reserved.
// http://github.com/threerings/tripleplay/blob/master/LICENSE

package com.pulapirata.core;

import tripleplay.ui.*;

/**
 * Pet style sheet, from adapted from tripleplay.ui.SimpleStyles
 */
public class PetStyles
{
    /**
     * Creates and returns a simple default stylesheet.
     */
    public static Stylesheet newSheet () {
        return newSheetBuilder().create();
    }

    /**
     * Creates and returns a stylesheet builder configured with Pet default styles. The
     * caller can augment the sheet with additional styles and call {@code create}.
     */
    public static Stylesheet.Builder newSheetBuilder () {
        return Stylesheet.builder().
            add(Button.class,
                Style.BACKGROUND.is(Background.blank())).
            add(Button.class, Style.Mode.SELECTED,
                Style.BACKGROUND.is(Background.blank()));
    }
}
