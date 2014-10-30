package com.pulapirata.core.sprites;
import java.util.EnumMap;
import com.pulapirata.core.PetAttribute;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.PetAttributes.AgeStage;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Schedules activities and post-modifiers.
 * Usually initiated by a button.
 */
public class Trigger {
    /*-------------------------------------------------------------------------------*/
    /** Trigger-specific */

    /** the cost of performing this action */
    private int cost_;
    public void setCost(int c) { cost_ = c; }

    /** is this trigger enabled in the game? */
    public boolean enabled_;
    public boolean enabled() { return enabled_; }
    public boolean enable()  { enabled_ = true; }
    public boolean disable() { enabled_ = false; }

    /**
     * Pull the trigger.
     * Returns true if action and postcondition finished successfully (and were
     * not aborted).
     */
    public boolean fire(PetAttributes a) {
        assert a != null : "[trigger] null";
        // - schedule Action
        Action act = new Action(a);
        act.start(duration_);
        if (act.wasInterrupted()) {
            printd("[trigger] action was interrupted. No modifiers applied.");
            return false;
        }
        printd("[trigger] action" + act.get() + " finished.");
        assert a != null : "[trigger] pet got null after/during action";
        // - apply modifiers
        // we lock pet. but for the future, we'll be queueing actions,
        // so we check if it is still null
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
        fire(a);
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

    private ActionState action_; // internal pointer to the action

    /** action duration in CoelhoSegundos.
     * Initialized using default map from ActionState to duration. World will manage it. */
    private int duration_;
    public  int duration() { return duration_; }
    public void setDuration(int d) { duration_ = d; }

    /*-------------------------------------------------------------------------------*/
    /** Post-condition */

    /**
     * The desired deltas in each attrib's properties.
     * Maps string to Modifier class, attribute name to deltas in attributes.
     * Applied in post-condition.
     */
    private Modifiers m_;
    public void setModifiers(Modifiers m) { m_ = m; }

    public class Modifiers {

        // map from attribute to modifier class
        protected EnumMap<AttributeID, Modifier> map_ = new EnumMap<AttributeID, Modifier> (AttributeID.class);

        public boolean setDeltaValue(AttributeID a, int delta) {
            return map_.get(a).setValueDelta(delta);
        }

        /**
         * Applies modifiers to all attributes.
         */
        public boolean modify(PetAttributes a) {
            for (AttributeID id : map.keySet()) {  // for each possible attribute / modifier value
                //for (AttributeID id : map.keySet())  // for each possible attribute / modifier value
                /* Apply modifier to all attribute properties, eg., value, passive */
                Modifier mod = map_.get(id);
                if (!mod) {
                    dprint("[trigger] no modifier for attribute " + id + ", using default");
                    continue;
                }
                mod.modifyAllProperties(a.attr_(id));
                dprint("[trigger] either modifier rule for attribute " + a + "unavailable or some other error");
                return false;
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
            public void modifyAllProperties(PetAttributes a) {
                // for each regular attribute enum
                //  - a.atributemap(e).sum()
                a.sum(deltaVal_);
                // future: modify remaining properties
                // a.setPassivo(deltaPassivo_);
            }
        }
    }

    /*-------------------------------------------------------------------------------*/
    /** Misc */

    Trigger () {
        // do nothing
    }

    Trigger(Action a, int duration, Modifiers modifiers) {
        set(a, duration, modifiers);
    }

    /**
     * To be called by suitable constructor code.
     * Used in TriggerLoader.
     */
    public void set(ActionState a, int d, Modifiers m) {
        action_ = a;
        duration_ = d;
        modifiers_ = m;
    }
}
