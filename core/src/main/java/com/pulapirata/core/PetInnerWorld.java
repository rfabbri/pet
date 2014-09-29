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
    /** Misc variables */

    modeMap id_;   // id_[ALCOOL] -> mode_[id_[ALCOOL]]: id do BEBADO (bitfield)

    /*-------------------------------------------------------------------------------*/
    /** Types of entities */
    public static final int ALCOOL    = (1 << 0);
    public static final int NUTRITION = (1 << 1);
    public static final int HUMOR     = (1 << 2);
    public static final int THIRST    = (1 << 3);
    public static final int SOCIAL    = (1 << 4);
    public static final int HIGIENE   = (1 << 5);
    public static final int ACTION    = (1 << 6);

    /*-------------------------------------------------------------------------------*/
    /** Modes, Actions, Qualitatives */

    public enum QualitativeAttributeMode {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO, // alcool
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE // humor
    }


    /*-------------------------------------------------------------------------------*/
    /** Components.
     * Components are bags of types, positions, and other properties shared among
     * dynamic state/psycho entities inside the Pet
     */

    // The type of each component: alcool, nutrition, ..., action
    public final Component.IMask type_       = new Component.IMask(this);

    public final PetAttributeComponent att_  = new Component.IScalar(this);
    public final Component.IMask qmode_      = new Component.IMask(this);


    /*-------------------------------------------------------------------------------*/
    /** Misc methods */

    /** Returns the state of inner atribute
     */
    QualitativeAttributeMode mode(int e_type) {
        return qmode_.get(id_(e_type));
    }

    public PetInnerWorld () {
    }
}
