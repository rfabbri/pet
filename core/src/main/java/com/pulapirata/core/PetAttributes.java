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

    /**
     * Visible conditions and priority.
     *
     * Each visible condition has an int priority value associated to it, which
     * is just the order in the enum. The greater this value, the more priority
     * that visible condition has over the others to be displayed in the game.
     */
    public enum VisibleCondition {
        NORMAL,
        TRISTE,
        CHORANDO,
        IRRITADO,
        BRAVO,
        PULANDO,
        DOENTE,
        MACHUCADO,
        MUITO_MACHUCADO,
        BEBADO,
        NORMAL_COM_VOMITO,
        VOMITANDO,
        BEBADO_VOMITANDO,
        COMA,
        COMA_ALCOOLICO,
        MORTO,
        COM_MOSQUITO,
        COM_STINKY_MOSQUITO,
        UNDETERMINED
    }

    /** lists all attributes in the form of enum. */
    public enum AttributeID {
        NUTRICAO,
        HUMOR,
        SOCIAL,
        HIGIENE,
        ESTUDO,
        SAUDE,
        DISCIPLINA,
        ALCOOL,
        VIDA,
        SEXUALIDADE,
        FE,
        DINHEIRO,
        ACTION
    }

    public enum State {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO,                                 // Nutricao
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE,                          // Humor
        DEPRESSAO, SOZINHO, POUCOS_AMIGOS, POPULAR, SUPERSTAR,                                // Social
        IMUNDO, MUITO_SUJO, SUJO, LIMPO, MUITO_LIMPO,                                         // Higiene
        EXPULSO, REPROVADO, RECUPERACAO, MEDIANO, BOM_ALUNO, MELHOR_DA_SALA,                  // Estudo
        DOENTE_TERMINAL, DOENTE, FRAGIL, ESTAVEL, SAUDAVEL, ATLETA,                           // Saude
        CRIMINOSO, REBELDE, TEIMOSO, OBEDIENTE, RESPONSAVEL, MAQUINA_DISCIPLINAR,             // Disciplina
        SOBRIO, TONTO, BEBADO, MUITO_BEBADO, COMA_ALCOOLICO,                                  // Alcool
        COMA, DILACERADO, MUITO_MACHUCADO, MACHUCADO, FERIDO,                                 // Vida
        ASSEXUADO, VIRGEM, APRECIADOR_DO_SEXO, SEXO_SELVAGEM, MICHAEL_DOUGLAS, NINFOMANIACO,  // Sexualidade
        ANTI_CRISTO, FIEL_FERVOROSO,                                                          // Fe
        NORMAL,
        ONONOONO // impossivel - invalido
        ;
    }

    public enum ActionState {
        DEFAULT,
        VARRENDO,
        JOGANDO_BOLA,
        COMENDO,
        LIMPANDO,
        JOGANDO_VIDEOGAME,
        ASSITINDO_TV,
        LENDO_QUADRINHOS,
        LENDO_LIVRO,
        ASSISTINDO_ANIME,
        ASSISTINDO_CINE_PRIVE,
        DESENHANDO,
        USANDO_CELULAR,
        USANDO_REDE_SOCIAL,
        JOGANDO_RPG,
        TOMANDO_BANHO,
        ESCOVANDO_DENTES,
        PASSANDO_PERFUME,
        FUMANDO,
        MASTURBANDO,
        SAINDO_DE_CASA,
        TOMANDO_GLICOSE,
        TOMANDO_REMEDIO,
        LEVANDO_CURATIVO,
        TOMANDO_VACINA,
        TOMANDO_ESTOMAZIL,
        TOMANDO_ENGOV,
        TOMANDO_VIAGRA,
        LEVANDO_CHINELADA,
        OUVIDO_GRITO,
        SENDO_CASTIGADO,
        LEVANDO_CHICOTADA,
        GORFANDO  // vomitando de vez em quando soltando vomito
    }

    private PetAttribute nutricao_;
    public  PetAttribute nutricao() { return nutricao_; }
    private PetAttribute humor_;
    public  PetAttribute humor() { return humor_; }
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
    private PetAttribute alcool_;
    public  PetAttribute alcool() { return alcool_; }
    private PetAttribute vida_;
    public  PetAttribute vida() { return vida_; }
    private PetAttribute sexualidade_;
    public  PetAttribute sexualidade() { return sexualidade_; }
    private PetAttribute fe_;
    public  PetAttribute fe() { return fe_; }

    private PetAttribute dinheiro_;
    public  PetAttribute dinheiro() { return dinheiro_; }

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

    private PetAttributeState<State> sNutricao_;
    public  PetAttributeState<State> sNutricao() { return sNutricao_; }
    private PetAttributeState<State> sHumor_;
    public  PetAttributeState<State> sHumor() { return sHumor_; }
    private PetAttributeState<State> sSocial_;
    public  PetAttributeState<State> sSocial() { return sSocial_; }
    private PetAttributeState<State> sHigiene_;
    public  PetAttributeState<State> sHigiene() { return sHigiene_; }
    private PetAttributeState<State> sEstudo_;
    public  PetAttributeState<State> sEstudo() { return sEstudo_; }
    private PetAttributeState<State> sSaude_;
    public  PetAttributeState<State> sSaude() { return sSaude_; }
    private PetAttributeState<State> sDisciplina_;
    public  PetAttributeState<State> sDisciplina() { return sDisciplina_; }
    private PetAttributeState<State> sAlcool_;
    public  PetAttributeState<State> sAlcool() { return sAlcool_; }
    private PetAttributeState<State> sVida_;
    public  PetAttributeState<State> sVida() { return sVida_; }
    private PetAttributeState<State> sSexualidade_;
    public  PetAttributeState<State> sSexualidade() { return sSexualidade_; }
    private PetAttributeState<State> sFe_;
    public  PetAttributeState<State> sFe() { return sFe_; }
    private PetAttributeEnum<ActionState> sAction_ = new PetAttributeEnum<ActionState>();
    public  PetAttributeEnum<ActionState> sAction() { return sAction_; }

    /*-------------------------------------------------------------------------------*/
    /** Logical appearance from inner state */


    /** Maps {@link PetAttributeState}s to visible conditions. */
    public EnumMap<State, VisibleCondition> s2vis_ = new EnumMap<State, VisibleCondition>(State.class);

    protected IntValue vis_ = new IntValue(VisibleCondition.NORMAL.ordinal());  // reactive ids into VisibleCondition

    public IntValue vis() { return vis_; }

    /**
     * Constructor
     */
    public PetAttributes() {
        // defalt values. values in the json will take precedence if available
        nutricao_       = new PetAttribute("Nutricao");
        humor_          = new PetAttribute("Humor");
        social_         = new PetAttribute("Social");
        higiene_        = new PetAttribute("Higiene");
        estudo_         = new PetAttribute("Estudo");
        saude_          = new PetAttribute("Saude");
        disciplina_     = new PetAttribute("Disciplina");
        alcool_         = new PetAttribute("Alcool");
        vida_           = new PetAttribute("Vida");
        sexualidade_    = new PetAttribute("Sexualidade");
        fe_             = new PetAttribute("Fe");
        dinheiro_       = new PetAttribute("Dinheiro");

        mapAttrib(nutricao());
        mapAttrib(humor());
        mapAttrib(social());
        mapAttrib(higiene());
        mapAttrib(estudo());
        mapAttrib(saude());
        mapAttrib(disciplina());
        mapAttrib(alcool());
        mapAttrib(vida());
        mapAttrib(sexualidade());
        mapAttrib(fe());
        mapAttrib(dinheiro());

        s2vis_.put(State.FAMINTO, VisibleCondition.CHORANDO);
         s2vis_.put(State.MUITA_FOME, VisibleCondition.CHORANDO);
         s2vis_.put(State.FOME, VisibleCondition.TRISTE);
         s2vis_.put(State.SATISFEITO, VisibleCondition.NORMAL);
         s2vis_.put(State.CHEIO, VisibleCondition.NORMAL);
         s2vis_.put(State.LOTADO, VisibleCondition.NORMAL_COM_VOMITO);
        s2vis_.put(State.BRAVO, VisibleCondition.BRAVO);
         s2vis_.put(State.IRRITADO, VisibleCondition.IRRITADO);
         s2vis_.put(State.ENTEDIADO, VisibleCondition.NORMAL);
         s2vis_.put(State.ENTRETIDO, VisibleCondition.NORMAL);
         s2vis_.put(State.ALEGRE, VisibleCondition.NORMAL);
         s2vis_.put(State.MUITO_ALEGRE, VisibleCondition.NORMAL);
        s2vis_.put(State.DEPRESSAO, VisibleCondition.CHORANDO);
         s2vis_.put(State.SOZINHO, VisibleCondition.TRISTE);
         s2vis_.put(State.POUCOS_AMIGOS, VisibleCondition.NORMAL);
         s2vis_.put(State.POPULAR, VisibleCondition.NORMAL);
         s2vis_.put(State.SUPERSTAR, VisibleCondition.NORMAL);
        s2vis_.put(State.IMUNDO, VisibleCondition.COM_STINKY_MOSQUITO);
         s2vis_.put(State.MUITO_SUJO, VisibleCondition.COM_MOSQUITO);
         s2vis_.put(State.SUJO, VisibleCondition.COM_MOSQUITO);
         s2vis_.put(State.LIMPO, VisibleCondition.NORMAL);
         s2vis_.put(State.MUITO_LIMPO, VisibleCondition.NORMAL);
        s2vis_.put(State.EXPULSO, VisibleCondition.NORMAL);
         s2vis_.put(State.REPROVADO, VisibleCondition.NORMAL);
         s2vis_.put(State.RECUPERACAO, VisibleCondition.NORMAL);
         s2vis_.put(State.MEDIANO, VisibleCondition.NORMAL);
         s2vis_.put(State.BOM_ALUNO, VisibleCondition.NORMAL);
         s2vis_.put(State.MELHOR_DA_SALA, VisibleCondition.NORMAL);
        s2vis_.put(State.DOENTE_TERMINAL, VisibleCondition.DOENTE);
         s2vis_.put(State.DOENTE, VisibleCondition.DOENTE);
         s2vis_.put(State.FRAGIL, VisibleCondition.NORMAL);
         s2vis_.put(State.ESTAVEL, VisibleCondition.NORMAL);
         s2vis_.put(State.SAUDAVEL, VisibleCondition.NORMAL);
         s2vis_.put(State.ATLETA, VisibleCondition.NORMAL);
        s2vis_.put(State.CRIMINOSO, VisibleCondition.NORMAL);
         s2vis_.put(State.REBELDE, VisibleCondition.NORMAL);
         s2vis_.put(State.TEIMOSO, VisibleCondition.NORMAL);
         s2vis_.put(State.OBEDIENTE, VisibleCondition.NORMAL);
         s2vis_.put(State.RESPONSAVEL, VisibleCondition.NORMAL);
         s2vis_.put(State.MAQUINA_DISCIPLINAR, VisibleCondition.NORMAL);
        s2vis_.put(State.SOBRIO, VisibleCondition.NORMAL);
         s2vis_.put(State.TONTO, VisibleCondition.BEBADO);
         s2vis_.put(State.BEBADO, VisibleCondition.BEBADO);
         s2vis_.put(State.MUITO_BEBADO, VisibleCondition.BEBADO_VOMITANDO);
         s2vis_.put(State.COMA_ALCOOLICO, VisibleCondition.COMA_ALCOOLICO);
        s2vis_.put(State.COMA, VisibleCondition.COMA);
         s2vis_.put(State.DILACERADO, VisibleCondition.MUITO_MACHUCADO);
         s2vis_.put(State.MUITO_MACHUCADO, VisibleCondition.MUITO_MACHUCADO);
         s2vis_.put(State.FERIDO, VisibleCondition.MACHUCADO);
        s2vis_.put(State.ASSEXUADO, VisibleCondition.NORMAL);
         s2vis_.put(State.VIRGEM, VisibleCondition.NORMAL);
         s2vis_.put(State.APRECIADOR_DO_SEXO, VisibleCondition.NORMAL);
         s2vis_.put(State.SEXO_SELVAGEM, VisibleCondition.NORMAL);
         s2vis_.put(State.MICHAEL_DOUGLAS, VisibleCondition.NORMAL);
         s2vis_.put(State.NINFOMANIACO, VisibleCondition.NORMAL);
        s2vis_.put(State.ANTI_CRISTO, VisibleCondition.NORMAL);
         s2vis_.put(State.FIEL_FERVOROSO, VisibleCondition.NORMAL);
        s2vis_.put(State.NORMAL, VisibleCondition.NORMAL);
        s2vis_.put(State.ONONOONO, VisibleCondition.NORMAL);

        sNutricao_ = new PetAttributeState();
        sHumor_ = new PetAttributeState();
        sSocial_ = new PetAttributeState();
        sHigiene_ = new PetAttributeState();
        sEstudo_ = new PetAttributeState();
        sSaude_ = new PetAttributeState();
        sDisciplina_ = new PetAttributeState();
        sAlcool_ = new PetAttributeState();
        sVida_ = new PetAttributeState();
        sSexualidade_ = new PetAttributeState();
        sFe_ = new PetAttributeState();

        sAction_.updateState(ActionState.DEFAULT);

        // intervals are set from json in PetAttributesLoader

        sNutricao_.set(nutricao());
            mapAttrib(sNutricao());
        sHumor_.set(humor());
            mapAttrib(sHumor());
        sSocial_.set(social());
            mapAttrib(sSocial());
        sHigiene_.set(higiene());
            mapAttrib(sHigiene());
        sEstudo_.set(estudo());
            mapAttrib(sEstudo());
        sSaude_.set(saude());
            mapAttrib(sSaude());
        sDisciplina_.set(disciplina());
            mapAttrib(sDisciplina());
        sAlcool_.set(alcool());
            mapAttrib(sAlcool());
        sVida_.set(vida());
            mapAttrib(sVida());
        sSexualidade_.set(sexualidade());
            mapAttrib(sSexualidade());
        sFe_.set(fe());
            mapAttrib(sFe());

        sAtt_.put(AttributeID.NUTRICAO, sNutricao());
        sAtt_.put(AttributeID.HUMOR, sHumor());
        sAtt_.put(AttributeID.SOCIAL, sSocial());
        sAtt_.put(AttributeID.HIGIENE, sHigiene());
        sAtt_.put(AttributeID.ESTUDO, sEstudo());
        sAtt_.put(AttributeID.SAUDE, sSaude());
        sAtt_.put(AttributeID.DISCIPLINA, sDisciplina());
        sAtt_.put(AttributeID.ALCOOL, sAlcool());
        sAtt_.put(AttributeID.VIDA, sVida());
        sAtt_.put(AttributeID.SEXUALIDADE, sSexualidade());
        sAtt_.put(AttributeID.FE, sFe());
    }

    /**
     * To be called in PetAttributesLoader.
     *
     * This enables one to interconnect signals/slots after all basic components
     * have been constructed.
     */
    public void hookupReactiveWires() {
        // hookup AttributeStates to respond to Attributes
        sAlcool_.listen();
        sNutricao_.listen();
    }

    /**
     * maps {@link PetAttribute}s by name.
     */
    protected void mapAttrib(PetAttribute att) {
        m_.put(att.name(), att);
    }

    protected void mapAttrib(PetAttributeState s) {
        ms_.put(s.att_.name(), s);
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
        // TODO: todos entre -20 e 0 tem prioridade mais alta que o resto.

        int maxPrio = -1;
        for (AttributeID a : sAtt_.keySet()) {
            sAtt_.get(a).print();
            System.out.println("_+_+state: " + sAtt_.get(a).getState() + " attId: " + a);
            if (sAtt_.get(a).getState() == null)
                continue;
            if (s2vis_.get(sAtt_.get(a).getState()).ordinal() > maxPrio) {
                vis_.update(s2vis_.get(sAtt_.get(a).getState()).ordinal());
                maxPrio = vis_.get();
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
