package com.pulapirata.core;

import java.util.Map;
import java.util.HashMap;


import playn.core.Json;
import static playn.core.PlayN.log;
import playn.core.util.Callback;

import com.pulapirata.core.PetAttribute;

/**
 * A simple set of classes for character attributes.
 *
 * This is a simple class holding and managing a list of attributes, which is
 * basically the game state.
 *
 * - this takes attribute listing and handling off of the main update() loop
 *
 * - this also validates sets
 *
 * - after each "set" in the attribute, the attribute updates
 * its state to a qualitative one based on set boundaries,
 * eg, for alcool the qualitative state can be "coma", "vomiting",
 * "dizzy" or "normal".
 *
 * - when updating the game sprite, we follow sprite update logic in
 * update(), e.g., we check the status of all attributes and follow a table of
 * priority to set the current sprite based on that.
 *   - based on such priority we also determine which attributes should be
 *   listed on the statusbar.
 *   - determine also if an alert should be placed.
 *   - each attribute can claim a certain priority level, and the policy to
 *   decide between more than one w same priority can be handled externally,
 *   eg, show alternating messages. But a final external table should order up each
 *   attribute in case of conflicts.
 *   - the state of the game is determined by the collection of attributes
 *   - based on the attributes we determine which actions are allowed or not
 */
public class PetAttributes {
    public static String JSON = "pet/jsons/atributos.json";

    public enum VisibleCondition {
        NORMAL,
        BEBADO, VOMITANDO,
        DOENTE,
        MORTO,
        UNDETERMINED
    }

    /** lists all attributes in the form of enum. */
    public enum AttributeID {
        ALCOOL,
        FOME,
        HUMOR,
        SEDE,
        SOCIAL,
        HIGIENE,
        ESTUDO,
        SAUDE,
        DISCIPLINA,
        ACTION
    }

    private PetAttribute alcool_;
    public PetAttribute alcool() { return alcool_; }
    private PetAttribute fome_;
    public PetAttribute fome() { return fome_; }
    private PetAttribute humor_;
    public PetAttribute humor() { return humor_; }
    private PetAttribute sede_;
    public PetAttribute sede() { return sede_; }
    private PetAttribute social_;
    public PetAttribute social() { return social_; }
    private PetAttribute higiene_;
    public PetAttribute higiene() { return higiene_; }
    private PetAttribute estudo_;
    public PetAttribute estudo() { return estudo_; }
    private PetAttribute saude_;
    public PetAttribute saude() { return saude_; }
    private PetAttribute disciplina_;
    public PetAttribute disciplina() { return disciplina_; }

    public Map<String, PetAttribute> m_ = new HashMap<String, PetAttribute>();
    public PetAttribute get(String s) { return m_.get(s); }

    public Map<String, PetAttributeState> ms_ = new HashMap<String, PetAttributeState>();
    public PetAttribute sAtt(String s) { return ms_.get(s); }

    public Map<AttributeID, PetAttributeState> sAtt_ = new HashMap<AttributeID, PetAttributeState>();
    public PetAttribute sAtt(AttributeID id) { return sAtt_.get(id); }

    /*-------------------------------------------------------------------------------*/
    /** Qualitative attributes holding states for each attrib */

    private PetAttributeState sAlcool_;   // hooks to alcool_
    public PetAttributeState sAlcool() { return sAlcool_; }

    /*-------------------------------------------------------------------------------*/
    /** Appearance from inner state */

    /**
     * Priority of the visible conditions.
     *
     * Each visible condition has an int priority value associated to it. The
     * greater this value, the more priority that visible condition has over the
     * others to be displayed in the game.
     * TODO: use more efficient array structure. This is readable, tho.
     */
    public Map<VisibleCondition, int> prio_ = new HashMap<VisibleCondition, int>();

    /** Maps {@link PetAttributeState}s to visible conditions. */
    public Map<PetAttributeState.State, VisibleCondition> s2vis_
        = new HashMap<PetAttributeState.State, VisibleCondition>();

    IntValue vis_ = new IntValue(NORMAL);  // reactive ids into VisibleCondition

    /**
     * Constructor
     */
    public PetAttributes() {
        prio.put(MORTO, 500);
        prio.put(VOMITANDO, 300);
        prio.put(BEBADO, 400);
        prio.put(DOENTE, 40);
        // ... XXX

        // defalt values. values in the json will take precedence if available
        alcool_     = new PetAttribute("Alcool");
        fome_       = new PetAttribute("Fome");
        humor_      = new PetAttribute("Humor");
        sede_       = new PetAttribute("Sede");
        social_     = new PetAttribute("Social");
        higiene_    = new PetAttribute("Higiene");
        estudo_     = new PetAttribute("Estudo");
        saude_      = new PetAttribute("Saude");
        disciplina_ = new PetAttribute("Disciplina");

        mapAttrib(alcool());
        mapAttrib(fome());
        mapAttrib(humor());
        mapAttrib(sede());
        mapAttrib(social());
        mapAttrib(higiene());
        mapAttrib(estudo());
        mapAttrib(saude());
        mapAttrib(disciplina());

        s2vis_.put(PetAttributeState.FAMINTO, UNDETERMINED);
        s2vis_.put(MUITA_FOME, UNDETERMINED);
        s2vis_.put(FOME, UNDETERMINED);
        s2vis_.put(SATISFEITO, UNDETERMINED);
        s2vis_.put(RESSACA, VOMITANDO);
        // XXX

        // TODO read satelist, intervals from Json.
        // perhaps populateFromJson();

        sAlcool_.set(alcool());
        sFome_.set(fome());

        sAttr.put(AttributeID.ALCOOL, sAlcool());
        sAttr.put(AttributeID.FOME, sFome());

        mapAttrib(sAlcool());
        // .... TODO//

        /* Dominant appearance to the outside world */
        determineVisibleCondition();
    }

    /**
     * maps {@link PetAttribute}s by name.
     */
    void mapAttrib(PetAttribute att) {
        m_.put(att.name(), att);
    }

    void mapAttrib(PetAttributeState sAtt) {
        ms_.put(sAtt.att.name(), sAtt);
    }

    /**
     * returns the mode for qualitative attribute with id enum AttributeID
     */
    public PetAttributeState.State mode(AttributeID id) {
        return sAttr_(id).get();
    }

    /**
     * computes the dominant appearance from the internal attribute state.
     * there might be external rules as well, and for such cases there might be a
     * variant of this function with the needed extra parameters (ie, weather
     * state, latitude, event history, etc..). However, for now this function
     * solely determines visible state from the internal states from a priority
     * table. It does not do any logic, which should first reflect internal
     * states, not the visible state directly.
     */
    VisibleCondition determineVisibleCondition() {
        // priority

        int maxPrio = -1;
        for (AttributeState state : ms.keySet()) {
            if (prio[s2vis_[state.get()]] > maxPrio) {
                vis_ = s2vis_[state.get()];
                maxPrio = prio[vis_.get()];
            }
        }
        assert maxPrio != -1 : "either ms is empty or prio vector has negative entries";
        return vis_;
    }

    boolean isInitialized() {
        for (String key : ms.keySet()) {
            if (!ms_.get(key).isInitialized())
                return false;
        }
        return true;
    }

    /**
     * example of game logic depending on multiple attributes
     */
    public void print() {
        for (String key : m_.keySet()) {
            m_.get(key).print();
        }
        for (String key : ms_.keySet()) {
            ms_.get(key).print();
        }
        // TODO print remaining - sAttribs
    }
}
