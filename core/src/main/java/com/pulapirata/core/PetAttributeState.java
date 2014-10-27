package com.pulapirata.core;

import java.util.ArrayList;
import react.Slot;
import react.IntValue;
import com.pulapirata.core.PetAttributeEnum;
import com.pulapirata.core.PetAttributes;


/**
 * A class that just partitions values into qualitative states.
 * For instance: Alcool: drunk, sober, hangover etc.
 *
 * Inside a rule that depends on a PetAttributeState, there will be a
 * slot() function. PetAttributeState will emit a signal only when
 * its qualitative state changes.
 *
 */
public class PetAttributeState<State extends Enum<State>>  extends PetAttributeEnum<State> {

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
    public void set(PetAttribute att) {
        att_ = att;
    }

    /**
     * Listen to the associated attribute for changes.
     */
    public void listen() {
        att_.connect(slot());
    }

    public void set(ArrayList<State> states, ArrayList<Integer> intervals) {
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
    @Override public Slot<Integer> slot () {
        return new Slot<Integer> () {
            @Override public void onEmit (Integer value) {
                updateState(value);
            }
        };
    }

    State updateState(int v) {
        assert att_.inRange(v) : "received signal must be in this attribute's range";

        State s = null;

        for (int i = 0; i < intervals_.size(); ++i)  // TODO: binary search
            if (v <= intervals_.get(i)) {
                s = states_.get(i);
                break;
            }
        assert s != null : "state not set in updateState..";

        State cs = updateState(s);
        //print();
        //System.out.println("_______________________ ENDState: " + cs);
        return cs;
    }

    public boolean isInitialized() {
        return initialized_;
    }

    @Override public void print() {
        //super.print();
        //if (att_.name().equals("Nutricao")) {
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXX State:");
        System.out.println("associated att name: " + att_.name() + " state: " + get());
        System.out.println("associated att val: " + att_.val());
        System.out.println("possible states and corresp intervals: ");

        System.out.println("        state: " + states_.get(0) + " interval: " + att_.min() + " to " + intervals_.get(0));

        for (int i = 1; i < states_.size(); ++i) {
            System.out.println("        state: " + states_.get(i) + " interval: " + (intervals_.get(i-1)+1) + " to " + intervals_.get(i));
        }
        //}
    }

    // pointer to the attribute corresponding to this state
    public PetAttribute att_;
    ArrayList<State> states_;
    ArrayList<Integer> intervals_;
    boolean initialized_ = false;
}
