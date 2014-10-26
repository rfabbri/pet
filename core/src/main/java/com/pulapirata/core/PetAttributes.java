package com.pulapirata.core;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import react.IntValue;
import playn.core.Json;
import static playn.core.PlayN.log;
import playn.core.util.Callback;
import com.pulapirata.core.PetAttribute;

/**
 * A simple set of character attributes.
 *
 * This is a simple class holding and managing a list of attributes, which is
 * basically the game state internal to Pet.
 *
 * - takes attribute plumbing, listing and handling code out of the main
 *   update() loop
 *
 * - after each "set" in the attribute, it updates its state to a qualitative
 *   one based on interval boundaries, eg, for alcool the qualitative state can be
 *   "coma", "vomiting", "dizzy" or "normal". Hooking up of the attribute to its
 *   attributeState is done by this class.
 *
 * - when updating the game sprite, we follow sprite update logic in update(),
 *   e.g., we check the status of all attributes and follow a table of priority
 *   to set the current sprite based on that.
 *      - based on such priority we also determine which attributes should be
 *      listed on the statusbar.
 *      - determine also if an alert should be placed.
 *      - each attribute can claim a certain priority level, and the policy to
 *      decide between more than one w same priority can be handled externally,
 *      eg, show alternating messages. But a final external table should order up each
 *      attribute in case of conflicts.
 *      - the inner state of the game is determined by the collection of attributes
 *      - based on the attributes we determine which actions are allowed or not
 *      - but there's also the running table of statistics (attributesCheck)..
 */
public class PetAttributes {
    public static String JSON_PATH = "pet/jsons/atributos.json";

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
        NUTRICAO,
        HUMOR,
        SEDE,
        SOCIAL,
        HIGIENE,
        ESTUDO,
        SAUDE,
        DISCIPLINA,
        ACTION
    }

    public enum State {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO, // nutricao
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE, // humor
        SOBRIO, BEBADO, RESSACA, COMA,  // alcool
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

    public enum ActionState {
        DEFAULT, VARRENDO, JOGANDO, CLEANING
        // XXX finish
    }

    private PetAttribute alcool_;
    public  PetAttribute alcool() { return alcool_; }
    private PetAttribute nutricao_;
    public  PetAttribute nutricao() { return nutricao_; }
    private PetAttribute humor_;
    public  PetAttribute humor() { return humor_; }
    private PetAttribute sede_;
    public  PetAttribute sede() { return sede_; }
    private PetAttribute social_;
    public  PetAttribute social() { return social_; }
    private PetAttribute higiene_;
    public  PetAttribute higiene() { return higiene_; }
    private PetAttribute estudo_;
    public  PetAttribute estudo() { return estudo_; }
    private PetAttribute saude_;
    public  PetAttribute saude() { return saude_; }
    private PetAttribute disciplina_;
    public  PetAttribute disciplina() { return disciplina_; }

    /** attribute name to object map */
    public Map<String, PetAttribute> m_ = new HashMap<String, PetAttribute>();
    public PetAttribute get(String s) { return m_.get(s); }

    /** attributeState name to object map */
    public Map<String, PetAttributeState> ms_ = new HashMap<String, PetAttributeState>();
    public PetAttributeState<State> sAtt(String s) { return ms_.get(s); }

    /** attributeID enum to attributeState object map */
    public EnumMap<AttributeID, PetAttributeState> sAtt_ = new EnumMap<AttributeID, PetAttributeState>(AttributeID.class);
    public PetAttributeState<State> sAtt(AttributeID id) { return sAtt_.get(id); }

    /*-------------------------------------------------------------------------------*/
    /** Qualitative attributes holding states for each attrib */

    private PetAttributeState<State> sAlcool_;
    public  PetAttributeState<State> sAlcool() { return sAlcool_; }
    private PetAttributeState<State> sNutricao_;
    public  PetAttributeState<State> sNutricao() { return sNutricao_; }
    private PetAttributeEnum<ActionState> sAction_;
    public  PetAttributeEnum<ActionState> sAction() { return sAction_; }

    /*-------------------------------------------------------------------------------*/
    /** Logical appearance from inner state */

    /**
     * Priority of the visible conditions.
     *
     * Each visible condition has an int priority value associated to it. The
     * greater this value, the more priority that visible condition has over the
     * others to be displayed in the game.
     * TODO: use more efficient array structure. This is readable, tho.
     */
    public EnumMap<VisibleCondition, Integer> prio_ = new EnumMap<VisibleCondition, Integer>(VisibleCondition.class);

    /** Maps {@link PetAttributeState}s to visible conditions. */
    public EnumMap<State, VisibleCondition> s2vis_ = new EnumMap<State, VisibleCondition>(State.class);

    protected IntValue vis_ = new IntValue(VisibleCondition.NORMAL.ordinal());  // reactive ids into VisibleCondition

    public IntValue vis() { return vis_; }

    /**
     * Constructor
     */
    public PetAttributes() {
        prio_.put(VisibleCondition.MORTO, 500);
        prio_.put(VisibleCondition.VOMITANDO, 300);
        prio_.put(VisibleCondition.BEBADO, 400);
        prio_.put(VisibleCondition.DOENTE, 40);
        // ... XXX

        // defalt values. values in the json will take precedence if available
        alcool_     = new PetAttribute("Alcool");
        nutricao_   = new PetAttribute("Nutricao");
        humor_      = new PetAttribute("Humor");
        sede_       = new PetAttribute("Sede");
        social_     = new PetAttribute("Social");
        higiene_    = new PetAttribute("Higiene");
        estudo_     = new PetAttribute("Estudo");
        saude_      = new PetAttribute("Saude");
        disciplina_ = new PetAttribute("Disciplina");

        mapAttrib(alcool());
        mapAttrib(nutricao());
        mapAttrib(humor());
        mapAttrib(sede());
        mapAttrib(social());
        mapAttrib(higiene());
        mapAttrib(estudo());
        mapAttrib(saude());
        mapAttrib(disciplina());

        s2vis_.put(State.FAMINTO, VisibleCondition.UNDETERMINED);
        s2vis_.put(State.MUITA_FOME, VisibleCondition.UNDETERMINED);
        s2vis_.put(State.FOME, VisibleCondition.UNDETERMINED);
        s2vis_.put(State.SATISFEITO, VisibleCondition.UNDETERMINED);
        s2vis_.put(State.RESSACA, VisibleCondition.VOMITANDO);
        // XXX

        sAlcool_   = new PetAttributeState();
        sNutricao_ = new PetAttributeState();
        sAction_.updateState(ActionState.DEFAULT);

        sAlcool_.set(alcool());
        sNutricao_.set(nutricao());
        // intervals are set from json in PetAttributesLoader

        sAtt_.put(AttributeID.ALCOOL, sAlcool());
        sAtt_.put(AttributeID.NUTRICAO, sNutricao());

        mapAttrib(sAlcool());
        mapAttrib(sNutricao());
        // .... TODO//

        /* Dominant appearance to the outside world */
        determineVisibleCondition();
    }

    /**
     * maps {@link PetAttribute}s by name.
     */
    protected void mapAttrib(PetAttribute att) {
        m_.put(att.name(), att);
    }

    protected void mapAttrib(PetAttributeState sAtt) {
        ms_.put(sAtt.att_.name(), sAtt);
    }

    /**
     * returns the mode for qualitative attribute with id enum AttributeID
     */
    public State mode(AttributeID id) {
        return sAtt(id).getState();
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
        for (AttributeID a : sAtt_.keySet()) {
            if (prio_.get(s2vis_.get(sAtt_.get(a).getState())) > maxPrio) {
                vis_.update(s2vis_.get(sAtt_.get(a).getState()).ordinal());
                maxPrio = prio_.get(vis_);
            }
        }
        assert maxPrio != -1 : "either ms is empty or prio vector has negative entries";
        return VisibleCondition.values()[vis_.get()];
    }

    public boolean isInitialized() {
        for (String key : ms_.keySet()) {
            if (!ms_.get(key).isInitialized())
                return false;
        }
        return true;
    }

    /**
     * example of game logic depending on multiple attributes
     */
    public void print() {
        for (String key : m_.keySet())
            m_.get(key).print();
        for (String key : ms_.keySet())
            ms_.get(key).print();
        sAction().print();
    }
}
