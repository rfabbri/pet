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
