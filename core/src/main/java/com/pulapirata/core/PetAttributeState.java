package com.pulapirata.core;

import java.util.ArrayList;
import react.IntValue;
import react.Slot;


/**
 * A class that just partitions values into qualitative states.
 * For instance: Alcool: drunk, sober, hangover etc.
 *
 * Inside a rule that depends on QualitativeAttribute, there will be a
 * slot() function. PetAttributeState will fire its value only when
 * the qualitative state changes.
 *
 * TODO implement.
 */
class PetAttributeState extends IntValue {
    public enum State {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO, // nutricao
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE, // humor
        SOBRIO, BEBADO,
        RESSACA, COMA,  // alcool
        ONONOONO // impossivel - invalido
        ;
        //XXX finish

        /*
        private final int value_;

        private State(int value) {
            this.value_ = value;
        }

        public int value() {
            return value_;
        }
        */
    }

    /**
     * start with easily identifiable dummy default values
     */
    public PetAttributeState() {
      super(-696);
    }


    /**
     * Construct for a given {@link PetAttribute} att.
     *
     * @param att      the image to be drawn
     * @param states an array of PetAttributeState.State for that att
     * @param intervals an array boundaries partitioning att's range. For
     * instance, if states == (A,B), then the interval will be:
     *      A is from att.min() inclusive to intervals[0] inclusive
     *      B is from intervals[0]+1 inclusive to intervals[1] inclusive
     */
    void set(PetAttribute att) {
        att_ = att;
        att_.connect(slot());
    }

    void set(ArrayList<State> states, ArrayList<Integer> intervals) {
        // make sure supplied intervals partitions the range of that
        // parameter:
        assert intervals.size() == states.size() : "Intervals and stateslist must be same-sized.";

        assert intervals.get(0) > att_.min() : "Interval 0 is not beyond att.min(), which should be the first interval boundary";

        for (int i = 1; i < intervals.size(); ++i) {
            assert intervals.get(i) > intervals.get(i-1) : "entries in intervals vector must be decreasing";
        }

        assert intervals.get(intervals.size()-1) == att_.max() : "last element must be att max";

        /* hook qualitative attributes to reduce ifs - make logic more
         * declarative */
        intervals_ = intervals;
        states_ = states;
        initialized_ = true;
    }

    /**
     * Returns a slot which can be used to wire this value to the emissions of a {@link Signal} or
     * another value.
     */
    public Slot<Integer> slot () {
        return new Slot<Integer> () {
            @Override public void onEmit (Integer value) {
                updateState(value);
            }
        };
    }

    State updateState(int v) {
        assert att_.inRange(v) : "received signal must be in this attribute's range";

        State s = State.ONONOONO;

        for (int i = 0; i < intervals_.size(); ++i)  // TODO: binary search
            if (v <= intervals_.get(i))
                s = states_.get(i);

        updateInt(s.ordinal());
        return s;
    }

    public State getState() {
        return State.values()[get()];
    }

    public boolean isInitialized() {
        return initialized_;
    }

    public void print() {
        System.out.println("associated att name: " + att_.name() + " state: " + get());
        System.out.println("associated att val: " + att_.val());
        System.out.println("possible states and corresp intervals:");

        System.out.println("state: " + states_.get(0) + "interval: " + att_.min() + " to " + intervals_.get(0));

        for (int i = 0; i < states_.size(); ++i) {
            System.out.println("state: " + states_.get(i) + "interval: " + att_.min() + " to " + intervals_.get(i));
        }
    }

    // pointer to the attribute corresponding to this state
    public PetAttribute att_;
    ArrayList<State> states_;
    ArrayList<Integer> intervals_;
    boolean initialized_ = false;
}
