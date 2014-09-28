package com.pulapirata.core;

import tripleplay.entity.Component;
import tripleplay.entity.Entity;
import tripleplay.entity.System;
import tripleplay.entity.World;

/**
 * An entity world inside Pet designed for keeping track of attributes and
 * interaction among multiple other objects. We call this the <em>intrinsic
 * world</em>. Pet's intrinsic properties are efficiently updated and watched by
 * several systems that implement game logic.
 */
class PetInnerWorld extends World {
    /*-------------------------------------------------------------------------------*/
    /** Types of entities */
    public static final int ALCOOL    = (1 << 0);
    public static final int NUTRITION = (1 << 1);
    public static final int HUMOR     = (1 << 2);
    public static final int THIRST    = (1 << 3);

    /*-------------------------------------------------------------------------------*/
    /** Components.
     * Components are bags of types, positions, and other properties shared among
     * dynamic state/psycho entities inside the Pet
     */
    public final Component.IMask type_ = new Component.IMask(this);
    public final PetAttributeComponent att_ = new Component.IScalar(this);
}
