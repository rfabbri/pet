package com.pulapirata.core.sprites;
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.PetAttributes.AgeStage;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Schedules activities and post-modifiers.
 * Usually initiated by a button.
 */
public class Trigger {
    public static String JSON_PATH = "pet/jsons/triggers.json";

    /*-------------------------------------------------------------------------------*/
    /** Trigger-specific */

    /** the cost of performing this action */
    int cost_;
    public void setCost(int c) { cost_ = c; }

    /** is this trigger enabled in the game? */
    public boolean enabled_;
    public boolean enabled() { return enabled_; }
    public boolean enable() { enabled_ = true; }
    public boolean disable() { enabled_ = false; }

    /**
     * Pull the trigger.
     * Returns true if action and postcondition finished successfully (and were
     * not aborted).
     */
    public boolean fire(PetAttributes a) {
        assert a != null : "[trigger] null";
        // - schedule Action
        action_.start(duration_);
        if (action_.wasInterrupted()) {
            printd("[trigger] action was interrupted. No modifiers applied.");
            return false;
        }
        // - apply modifiers
        printd("[trigger] action was interrupted. No modifiers applied.");
        // we lock pet. but for the future, we'll be queueing actions,
        // so we check if it is still null
        assert a != null : "[trigger] pet got null after/during action";
        m_.modify(a);
        return true;
    }

    /**
     * Pull the trigger.
     * And returns false if not allowed on an age.
     */
    public boolean fireIfAllowed(AgeStage a) {
        if (blackList(a))
            return false;
        fire();
        return true;
    }

    /**
     * Returns false if trigger not allowed on age.
     */
    public boolean blackListed(AgeStage a) {
        return blackList_ & a == 0;
    }

    /**
     * Stages when this trigger will be get enabled to the user.
     * Boolean field on AgeStages.
     * blackList(CRIANCA) == true if this trigger is *disabled* at CRIANCA age.
     * Use case:
     *      - button plumbing:
     *          - fire() test before enabling
     *          - json blackList_ | a
     */
    private int blackList_;  // mask
    public void blackList(AgeStage a) { blacklist_ |= a; }

    /*-------------------------------------------------------------------------------*/
    /** Action-specific */

    ActionState action_; // internal pointer to the action

    /** action duration in CoelhoSegundos.
     * Initialized using default map from ActionState to duration. World will manage it. */
    private int duration_;
    public int duration() { return duration_; }
    public void setDuration(int d) { duration_ = d; }

    /*-------------------------------------------------------------------------------*/
    /** Post-condition */

    /**
     * The desired deltas in each attrib's properties.
     * Maps string to Modifier class, attribute name to deltas in attributes.
     * Applied in post-condition.
     */
    Modifiers m_;
    public void setModifiers(Modifiers m) {m_ = m}

    public class Modifiers {

        // map from attribute to modifier class
        protected EnumMap<AttributeID, Modifier> map_ = new EnumMap<AttributeID, Modifier> (AttributeID.class);

        boolean setDeltaValue(AttributeID a, int delta) {
            return map_.get(a).setValueDelta(delta);
        }

        /**
         * Applies modifiers to all attributes.
         */
        boolean modify(PetAttributes a) {
            for (AttributeID id : AttributeID.values()) {  // for each possible attribute / modifier value
                /* Apply modifier to all attribute properties, eg., value, passive */
                boolean retval = map_.get(id).modifyAllProperties(a.attr_(id));
                if (!retval) {
                    dprint("[trigger] either modifier not available for " + a + " or some other error");
                    return false;
                }
            }
            return true;
        }

        /**
         * Determines how each attrib will be modified.
         * Default is just to sum up a value or set an attribute
         */
        public class Modifier {
            private int deltaVal_;
            public void setDeltaVal(int delta) { deltaVal_ = delta; }

            // put other deltas here - deltaPassivo_....

            // type: sum, set
            public void modifyDelta(PetAttribute a) {
                // handles a simple sum of values.
                // Other types are handled differently, case by case.
                // - if modifier is of certain kind, then set instead of sum.
                a.sum(deltaVal_);
            }


            /** Apply modifier to all attribute properties.
             * eg., value, passive */
            void modifyAllProperties(PetAttributes a) {
                // for each regular attribute enum
                //  - a.atributemap(e).sum()
                //
                //
                a.sum(deltaVal_);

                // future: modify remaining properties
                // a.setPassivo(deltaPassivo_);
            }
        }
    }

    /*-------------------------------------------------------------------------------*/
    /** Misc */

    Trigger(Action a, duration, modifiers)

    /**
     * To be called by suitable constructor code.
     * Used in TriggerLoader.
     */
    void set(Action a, int d, Modifiers m) {
        action_ = a;
        duration_ = d;
        modifiers_ = m;
    }
}
