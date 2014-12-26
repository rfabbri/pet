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
package com.pulapirata.core.utils;

/**
 * Pet UTilS. Generic stuff. We prefix with p.
 */
public class Puts {

    /**
     * Shortcut to print
     */
    public static void pprint(String s) {
        System.out.println(s);
    }

    /**
     * Shortcut to print and exit
     */
    public static void pprinte(String s) {
        pprint(s);
        System.exit(269);   // random
    }

    /**
     * Debug print. Can be disabled here.
     */
    public static void dprint(String s) {
        // pprint(s);
    }

    public static void printd(String s) {
        dprint(s);
    }

    /**
     * Debug print. Can be disabled here.
     */
    public static void dprinte(String s) {
        pprinte(s);
    }
}
