package com.pulapirata.core.sprites;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Schedules activities and post-modifiers.
 * Usually initiated by a button.
 */
public class Trigger {
    public static String JSON_PATH = "pet/jsons/triggers.json";

    /*-------------------------------------------------------------------------------*/
    /** Trigger-specific */

    int cost;
    boolean enabled;

    /**
     * Stages when this trigger will be get enabled to the user.
     * Boolean field on AgeStages.
     * ages_(CRIANCA) == true if this trigger is enabled at CRIANCA age.
     * Use case:
     *  -
     */
    AgeStage ages_;

    /*-------------------------------------------------------------------------------*/
    /** Action-specific */

    Action action_; // internal pointer to the action

    int duration_; // map from ActionState to duration. in CoelhoSegundos.  World will manage it


    /*-------------------------------------------------------------------------------*/
    /** Post-condition */


    /**
     * The desired deltas in each attrib's properties.
     * Maps string to Modifier class, attribute name to deltas in attributes.
     * Applied in post-condition.
     */
    modifiers;  // map from attribute to modifier class

    /**
     * Determines how each attrib will be modified.
     * Default is just to sum up a value or set an attribute
     */
    class Modifier {
        // type: sum, set
        void apply(PetAttribute a) {
            // handles a simple sum of values.
            // Other types are handled differently, case by case.
            // - if modifier is of certain kind, then set instead of sum.
            a.set(deltaVal_);
        }
    }

    /*-------------------------------------------------------------------------------*/
    /** Misc */

    Trigger(Action a, duration, modifiers);
}
