package com.pulapirata.core;

import react.IntValue;
import react.Slot;


/**
 * An attribute class that just stores qualitiative/partition values.
 * The values are stored in a reactive int which is the ordinal value of an
 * enum.
 */
public class PetAttributeEnum<State extends Enum<State>> extends IntValue {
    /**
     * start with easily identifiable dummy default values
     */
    public PetAttributeEnum() {
        super(-696);
    }

    public State getState() {
        return State.values()[get()];
    }

    public void print() {
        System.out.println(java.util.Arrays.asList(State.values()));
    }
}
