package com.pulapirata.core;

import tripleplay.entity.Component;
import tripleplay.entity.Entity;
import tripleplay.entity.System;
import tripleplay.entity.World;


class PetWorld extends World {
    /*-------------------------------------------------------------------------------*/
    /** Misc variables */

    public final GroupLayer layer_;
    public final Signal<Key> keyReloadGameFile = Signal.create();

    /*-------------------------------------------------------------------------------*/
    /** Types of entities */
    public static final int PET      = (1 << 0);
    public static final int DROPPING = (1 << 1);
    public static final int VOMIT    = (1 << 2);
    public static final int DIARRHEA = (1 << 3);
    public static final int MOSQUITOS = (1 << 4);
    public static final int STINKY_MOSQUITOS = (1 << 5);

    /*-------------------------------------------------------------------------------*/
    /** Components.
     * Components are bags of types, positions, and other properties shared among
     * playable entities in Pet (like the bunny itself and its droppings)
     */
    public final Component.IMask type = new Component.IMask(this);
    public final Component.XY pos = new Component.XY(this);
    public final Component.IScalar expires = new Component.IScalar(this);
    private final Randoms _rando = Randoms.with(new Random());

    /*-------------------------------------------------------------------------------*/
    /** Time data */
    private int beat_ = 0; // total number of updates so far

    @Override public void update (int delta) {
        beat_ += delta;
        super.update(delta);
    }

    public PetWorld (GroupLayer stage) {
        this.stage = stage;

        keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown (Keyboard.Event event) {
                keyReloadGameFile.emit(event.key());
            }
        });
    }

    protected String typeName (int id) {
        switch (type.get(id)) {
        case PET: return "pet";
        case DROPPING: return "dropping";
        case VOMIT: return "vomit";
        case DIARRHEA: return "diarrhea";
        case MOSQUITOS: return "mosquitos";
        case STINKY_MOSQUITOS: return "stinky_mosquitos";
        default: return "unknown:" + type.get(id);
      }
    }
}
