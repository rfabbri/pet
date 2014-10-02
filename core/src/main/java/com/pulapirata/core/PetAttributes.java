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

    public Map<String, PetAttribute> m = new HashMap<String, PetAttribute>();

    /*-------------------------------------------------------------------------------*/
    /** Qualitative attributes holding states for each attrib */

    private PetQAttribute qalcool_;  // hook to alcool_

    public enum QualitativeAttributeMode {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO, // alcool
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE // humor
    }

    public enum VisibleCondition {
        NORMAL,
        BEBADO, VOMITANDO,
        DOENTE,
        MORTO
    }

    IntValue vis_ = new IntValue(NORMAL);  // reactive ids into VisibleCondition

    /**
     * map by name
     */
    void mapAttrib(PetAttribute att) {
        m.put(att.name(), att);
    }

    public PetAttributes(double beatsCoelhoHora) {
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

        // TODO read satelist, intervals from Json.
        // perhaps populateFromJson();

        // I don't put into an array because I want a direct handle on each
        // attribute state when programming. Actual logic can't be generic,
        // after all the game has a well-defined personality!
        PetAttributeState sAlcool_(alcool(), statelist, intervals);
        PetAttributeState sFome_(alcool(), statelist, intervals);
        // .... TODO//

        /* Dominant appearance to the outside world */
        determineVisibleCondition();
    }

    /**
     * returns the mode for qualitative attribute with id enum AttributeID
     */
    public QualitativeAttributeMode mode(AttributeID id) {
        return qattr_(id).mode();
    }

    /**
     * returns the dominant mode among the ones available
     */
    public VisibleCondition visibleCondition() {
        return vis_;
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

        // XXX
        vis_ = ;
        return vis_;
    }


    /**
     * example of game logic depending on multiple attributes
     */
    public void print() {
        for (String key : m.keySet()) {
          m.get(key).print();
        }
        // TODO print remaining - qattribs
    }
}
