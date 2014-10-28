package com.pulapirata.core.utils;

/**
 * Pet UTils. Generic stuff. We prefix with p.
 */
public class Puts {

    /**
     * Shortcut to print
     */

    public static void pprint(String s) {
        System.out.println(s);
    }

    public static void pprinte(String s) {
        pprint(s);
        System.exit(269);   // random
    }
}
