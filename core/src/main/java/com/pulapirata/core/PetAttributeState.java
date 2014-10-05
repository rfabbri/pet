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
        SOBRIO, BEBADO, RESSACA, COMA // alcool
    }

    /**
     * Construct for a given {@link PetAttribute} attr.
     *
     * @param attr      the image to be drawn
     * @param statelist an array of PetAttributeState.State for that attr
     * @param intervals an array boundaries partitioning attr's range. For
     * instance, if statelist == (A,B), then the interval will be:
     *      A is from attr.min() inclusive to intervals[0] inclusive
     *      B is from intervals[0]+1 inclusive to intervals[1] inclusive
     */
    public PetAttributeState(
        PetAttribute attr,
        ArrayList<State> statelist,
        ArrayList<int> intervals) {
        // make sure supplied intervals partitions the range of that
        // parameter

        assert intervals.size() == statelist.size() : "Intervals and stateslist must be same-sized.":

        assert intervals[0] > attr.min();

        for (i = 1; i < intervals.size(); ++i) {
            assert intervals[i] > intervals[i-1] : "entries in intervals vector must be decreasing";
        }

        assert intervals.last() == attr.max() : "last element must be attr max";

        /* hook qualitative attributes to reduce ifs - make logic more
         * declarative */
        attr_ = attr;
        attr_.connect(slot());
    }



    public PetAttribute attr_;
}
