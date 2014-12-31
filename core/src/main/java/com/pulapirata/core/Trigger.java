/**
 * Pet - a comic pet simulator game
 * Copyright (C) 2013-2015 Ricardo Fabbri and Edson "Presto" Correa
 *
 * This program is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version. A different license may be requested
 * to the copyright holders.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>
 *
 * NOTE: this file may contain code derived from the PlayN, Tripleplay, and
 * React libraries, all (C) The PlayN Authors or Three Rings Design, Inc.  The
 * original PlayN is released under the Apache, Version 2.0 License, and
 * TriplePlay and React have their licenses listed in COPYING-OOO. PlayN and
 * Three Rings are NOT responsible for the changes found herein, but are
 * thankfully acknowledged.
 */
package com.pulapirata.core;
import java.util.EnumMap;
import com.pulapirata.core.PetAttribute;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.AttributeID;
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.PetAttributes.AgeStage;
import com.pulapirata.core.Action;
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
    public boolean enabled_ = true;
    public boolean enabled() { return enabled_; }
    public void enable()  { enabled_ = true; }
    public void disable() { enabled_ = false; }

    public boolean isInitialized() {
        return true;
    }

    /**
     * Pull the trigger.
     * Returns true if action and postcondition finished successfully (and were
     * not aborted).
     */
    public boolean fire(PetAttributes a) {
        if (!enabled_)
            return false;
        assert a != null : "[trigger] null";
        // - schedule Action
        act_ = new Action(action_, a);   // perhaps pass a
        act_.start(duration_);
        return true;
    }

    /**
     * Takes care of action timing
     */
    public void update(int delta) {
        if (act_ != null && !act_.finished()) {
            act_.update(delta);
            if (act_.finished()) {
                printd("[trigger] action" + act_.get() + " finished.");
                if (act_.wasInterrupted()) { // TODO we'll refine this later
                    printd("[trigger] action was interrupted. No modifiers applied.");
                    return;
                }
                // finished normally
                // - apply modifiers
                // but for the future, we'll be queueing actions,
                // so we check if it is still null
                m_.modify(act_.petAttributes());
            }
        }
    }

    /**
     * Pull the trigger.
     * And returns false if not allowed on an age.
     */
    public boolean fireIfAllowed(PetAttributes at) {
        if (!enabled_)
            return false;
        if (blackListed(at.sAge().getState())) {
            pprint("[trigger] trigger not allowed at AgeStage " + at.sAge().getState());
            return false;
        }
        fire(at);
        return true;
    }

    /**
     * Returns false if trigger not allowed on age.
     */
    public boolean blackListed(AgeStage a) {
        return (blackList_ & a.index()) != 0;
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
    public void blackList(AgeStage a) { blackList_ |= a.index(); }

    /*-------------------------------------------------------------------------------*/
    /** Action-specific */

    private ActionState action_ = ActionState.DEFAULT; // internal pointer to the action
    public void setAction(ActionState a) { action_ = a; }
    private Action act_; // could be alist of action for queueing?
    public Action action() { return act_; }

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
    private Modifiers m_ = new Modifiers();
    public Modifiers m() { return m_; }
    public void setModifiers(Modifiers m) { m_ = m; }

    public class Modifiers {

        // map from attribute to modifier class
        protected EnumMap<AttributeID, Modifier> map_ = new EnumMap<AttributeID, Modifier> (AttributeID.class);

        public boolean setDeltaValue(AttributeID a, int delta) {
            Modifier mod = map_.get(a);
            if (mod == null) {
                dprint("[modifiers] modifier for key " + a + " not available");
                return false;
            }
            mod.setDeltaValue(delta);
            return true;
        }

        public boolean setValue(AttributeID a, int v) {
            Modifier mod = map_.get(a);
            if (mod == null) {
                dprint("[modifiers] modifier for key " + a + " not available");
                return false;
            }
            mod.setValue(v);
            return true;
        }

        public void initModifier(AttributeID a) {
            map_.put(a, new Modifier());
        }

        /**
         * Applies modifiers to all attributes.
         */
        public boolean modify(PetAttributes a) {
            for (AttributeID id : map_.keySet()) {  // for each possible attribute / modifier value
                if (id == AttributeID.ACTION) {
                    dprint("[modify] attribute action have no modifiers, currently ignored.");
                    continue;
                }

                //for (AttributeID id : map.keySet())  // for each possible attribute / modifier value
                /* Apply modifier to all attribute properties, eg., value, passive */
                Modifier mod = map_.get(id);
                if (mod == null) {
                    dprint("[modify] no modifier for attribute " + id + ", using default");
                    continue;
                }
                dprint("[modify] id " + id);
                if (id == AttributeID.TIPO_COCO) {
                    dprint("[modify] updating coco to id " + mod.get() +
                           " enum " + PetAttributes.TipoCoco.values()[mod.get()]);
                    a.sCoco().updateState(PetAttributes.TipoCoco.values()[mod.get()]);
                } else {
                    // regular attributes
                    PetAttribute at = a.get(id);
                    if (at == null) {
                        pprint("[modify] Error: check wiring of id above in PetAttribtes");
                        assert a != null;
                    }
                    mod.modifyAllProperties(at);
                }
            }
            return true;
        }

        /**
         * Determines how each attrib will be modified.
         * Default is just to sum up a value or set an attribute
         */
        public class Modifier {
            private int value_;

            public int get() { return value_; }

            boolean setValueDirectly_;

            public void setDeltaValue(int delta) { value_ = delta; setValueDirectly_ = false;}
            public void setValue(int delta) { value_ = delta; setValueDirectly_ = true; }

            // put other deltas here - deltaPassivo_....

            // type: sum, set
            public void modify(PetAttribute a) {
                // handles a simple sum of values.
                // Other types are handled differently, case by case.
                // - if modifier is of certain kind, then set instead of sum.
                if (setValueDirectly_)
                    a.set(value_);
                else
                    a.sum(value_);
            }

            /** Apply modifier to all attribute properties.
             * eg., value, passive */
            public void modifyAllProperties(PetAttribute a) {
                pprint("[modifier] " + a);
                // for each regular attribute enum
                //  - a.atributemap(e).sum()
                modify(a);
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

    Trigger(ActionState a, int duration, Modifiers modifiers) {
        set(a, duration, modifiers);
    }

    /**
     * To be called by suitable constructor code.
     * Used in TriggerLoader.
     */
    public void set(ActionState a, int d, Modifiers m) {
        action_ = a;
        duration_ = d;
        m_ = m;
    }
}
