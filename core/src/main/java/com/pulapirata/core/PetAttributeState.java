package com.pulapirata.core;

import react.IntValue;


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
        SOBRIO, BEBADO, RESSACA, COMA  // alcool
        //XXX finish
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
    public PetAttributeState(
                PetAttribute att, ArrayList<State> states, ArrayList<int> intervals) {
        this(att);
        set(states, intervals);
    }

    public PetAttributeState(PetAttribute att)
    {
        att_ = att;
        att_.connect(slot());
    }

    void set(PetAttribute att) {
        att_ = att;
        att_.connect(slot());
    }

    void set(ArrayList<State> states, ArrayList<int> intervals) {
        // make sure supplied intervals partitions the range of that
        // parameter:
        assert intervals.size() == states.size() : "Intervals and stateslist must be same-sized.";

        assert intervals[0] > att.min() : "Interval 0 is not beyond att.min(), which should be the first interval boundary";

        for (i = 1; i < intervals.size(); ++i) {
            assert intervals[i] > intervals[i-1] : "entries in intervals vector must be decreasing";
        }

        assert intervals.last() == att.max() : "last element must be att max";

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
    public Slot<int> slot () {
        return new Slot<int> () {
            @Override public void onEmit (int value) {
                updateState(value);
            }
        };
    }

    State get() { return s_; }

    State updateState(int v) {
        assert att_.inRange(v) : "received signal must be in this attribute's range";

        for (int i = 0; i < intervals; ++i)  // TODO: binary search
            if (v <= intervals_[i])
                s_ = states_[i];
    }

    public void print() {
        System.out.println("associated att name: " + att_.name() + " state: " + get());
        System.out.println("associated att val: " + att_.val());
        System.out.println("possible states and corresp intervals:");

        System.out.println("state: " + states_[0] + "interval: " + att_.min() + " to " + intervals_[0]);

        for (i = 0; i < states_.size(); ++i) {
            System.out.println("state: " + states_[i] + "interval: " + att_.min() + " to " + intervals_[i]);
        }
    }

    // pointer to the attribute corresponding to this state
    public PetAttribute att_;
    private State s_;
    ArrayList<State> states_;
    ArrayList<int> intervals_;
    boolean initialized_ = false;
}
