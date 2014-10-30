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
//        pprint(s);
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
