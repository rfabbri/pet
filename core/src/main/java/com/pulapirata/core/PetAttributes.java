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
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import react.IntValue;
import playn.core.Json;
import static playn.core.PlayN.log;
import playn.core.util.Callback;
import com.pulapirata.core.PetAttribute;
import static com.pulapirata.core.utils.Puts.*;

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
        IGNORE,        // lowest prio - will usually be overriden by higher prio
        NORMAL,
        RESPIRANDO,    // pode ser parte de Normal.
        TRISTE,
        CHORANDO,
        IRRITADO,
        BRAVO,
        PULANDO,
        FEBRE,
        MACHUCADO,
        MUITO_MACHUCADO,
        BEBADO,
        DOENTE,
        RESSACA,  // TODO: implement internal ressaca attribute logic and link it to visible condition
        NORMAL_COM_VOMITO,
        VOMITANDO,
        BEBADO_VOMITANDO,

        //--- Maioria das acoes entram nesta prioridade
        COMENDO,
        DORMINDO,
        VARRENDO,

        //---
        COMA,
        COMA_ALCOOLICO,
        MORTO,
//        COM_MOSQUITO,  // tudo que comeca com COM_ eh overlay, administrado pelo petWorld.
//        COM_STINKY_MOSQUITO,
		INVISIBLE,
        UNDETERMINED
    }

    /** lists all attributes in the form of enum. */
    public enum AttributeID {
        /** main ones */
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
        INTESTINO,
        SANGUE,     // O quanto de sangue espirra dele a cada instante (vai sendo decrementado para zero).
        /** other types  */
        ACTION,
        TIPO_COCO,
        TIPO_CELULAR,
        RESSACA
    }

    public enum State {
        FAMINTO, MUITA_FOME, FOME, SATISFEITO, CHEIO, LOTADO,                                 				// Nutricao
        BRAVO, IRRITADO, ENTEDIADO, ENTRETIDO, ALEGRE, MUITO_ALEGRE,                          				// Humor
        DEPRESSAO, SOZINHO, POUCOS_AMIGOS, POPULAR, SUPERSTAR,                               				// Social
        IMUNDO, MUITO_SUJO, SUJO, LIMPO, MUITO_LIMPO,                                         				// Higiene
        EXPULSO, REPROVADO, RECUPERACAO, MEDIANO, BOM_ALUNO, MELHOR_DA_SALA,                  				// Estudo
        MORTO_DOENTE, DOENTE_TERMINAL, DOENTE, FRAGIL, ESTAVEL, SAUDAVEL, ATLETA,                           // Saude
        PRESO, CRIMINOSO, REBELDE, TEIMOSO, OBEDIENTE, RESPONSAVEL, MAQUINA_DISCIPLINAR,             		// Disciplina
        SOBRIO, TONTO, BEBADO, MUITO_BEBADO, COMA_ALCOOLICO,                                  				// Alcool
        COMA, DILACERADO, MUITO_MACHUCADO, MACHUCADO, FERIDO,                                 				// Vida
        ASSEXUADO, VIRGEM, INEXPERIENTE, APRECIADOR_DO_SEXO, SEXO_SELVAGEM, MICHAEL_DOUGLAS, NINFOMANIACO,  // Sexualidade
        ANTI_CRISTO, FIEL_FERVOROSO,                                                          				// Fe
                                                                                                            // Intestino
                                                                                                            // Sangue
        HOJE, AMANHA,                                                                                       // Ressaca
        NORMAL,
        ONONOONO 																							// impossivel - invalido
        ;
    }

    public enum TipoCoco {
        NORMAL,
        MOLE
    }

    public enum TipoVomito {
        NORMAL,
        AGUADO
    }

    public enum TipoCelular {
        NENHUM,
        GENERICO,
        TIJOLO,
        TELHA
    }

    public enum AgeStage {
        BEBE            (1 << 0),
        CRIANCA         (1 << 1),
        ADOLESCENTE     (1 << 2),
        ADULTO          (1 << 3),
        MEIA_IDADE      (1 << 4),
        TERCEIRA_IDADE  (1 << 5),
        ANCIAO          (1 << 6);

        private final int index;

        private AgeStage(int index) {
            this.index = index;
        }

        public int index() {
            return index;
        }
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
        LEVANDO_GRITO,
        SENDO_CASTIGADO,
        LEVANDO_CHICOTADA,
        LIGANDO,
        CONVIDANDO_COLEGA,
        MEXENDO_CELULAR,
        FAZENDO_FESTA,
        BOOTY_CALLING,
        ESTUDANDO,
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
    private PetAttribute intestino_;
    public  PetAttribute intestino() { return intestino_; }
    private PetAttribute sangue_;
    public  PetAttribute sangue() { return sangue_; }
    private PetAttribute ressaca_;
    public  PetAttribute ressaca() { return ressaca_; }

    /** attribute name to object map */
    public Map<String, PetAttribute> m_ = new HashMap<String, PetAttribute>();
    public PetAttribute get(String s) { return m_.get(s.toUpperCase().replace(' ', '_')); }
    public PetAttribute get(AttributeID id) { return get(id.toString()); }

    /** attributeState name to object map */
    public Map<String, PetAttributeState> ms_ = new HashMap<String, PetAttributeState>();
    public PetAttributeState<State> sAtt(String s) { return ms_.get(s.toUpperCase().replace(' ', '_')); }

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
    public  PetAttributeState<State> sRessaca_;
    public  PetAttributeState<State> sRessaca() { return sRessaca_; }
    private PetAttributeEnum<ActionState> sAction_ = new PetAttributeEnum<ActionState>();
    public  PetAttributeEnum<ActionState> sAction() { return sAction_; }
    private PetAttributeEnum<TipoCoco> sCoco_ = new PetAttributeEnum<TipoCoco>();
    public  PetAttributeEnum<TipoCoco> sCoco() { return sCoco_; }
    private PetAttributeEnum<TipoVomito> sVomito_ = new PetAttributeEnum<TipoVomito>();
    public  PetAttributeEnum<TipoVomito> sVomito() { return sVomito_; }
    private PetAttributeEnum<TipoCelular> sCelular_ = new PetAttributeEnum<TipoCelular>();
    public  PetAttributeEnum<TipoCelular> sCelular() { return sCelular_; }
    private PetAttributeEnum<AgeStage> sAge_ = new PetAttributeEnum<AgeStage>();
    public  PetAttributeEnum<AgeStage> sAge() { return sAge_; }

    /*-------------------------------------------------------------------------------*/
    /** Logical appearance from inner state */


    /** Maps {@link PetAttributeState}s to visible conditions. */
    public EnumMap<State, VisibleCondition> s2vis_ = new EnumMap<State, VisibleCondition>(State.class);

    /** Maps {@link ActionState}s to visible conditions. */
    public EnumMap<ActionState, VisibleCondition> a2vis_ = new EnumMap<ActionState, VisibleCondition>(ActionState.class);

    protected IntValue vis_ = new IntValue(VisibleCondition.NORMAL.ordinal());  // reactive ids into VisibleCondition

    public IntValue vis() { return vis_; }
    public VisibleCondition visibleCondition() { return VisibleCondition.values()[vis_.get()]; }

    boolean  blockedVisibleCondition_ = false;

    /*-------------------------------------------------------------------------------*/
    /** Misc Variables. */

    /** Initially set by attributes loader */
    static private double beatsCoelhoHora_ = 0d;

    /**
     * Constructor
     */
    public PetAttributes() {
        // defalt values. values in the json will take precedence if available
        nutricao_       = new PetAttribute("NUTRICAO");
        humor_          = new PetAttribute("HUMOR");
        social_         = new PetAttribute("SOCIAL");
        higiene_        = new PetAttribute("HIGIENE");
        estudo_         = new PetAttribute("ESTUDO");
        saude_          = new PetAttribute("SAUDE");
        disciplina_     = new PetAttribute("DISCIPLINA");
        alcool_         = new PetAttribute("ALCOOL");
        vida_           = new PetAttribute("VIDA");
        sexualidade_    = new PetAttribute("SEXUALIDADE");
        fe_             = new PetAttribute("FE");
        intestino_      = new PetAttribute("INTESTINO");
        sangue_         = new PetAttribute("SANGUE");
        dinheiro_       = new PetAttribute("DINHEIRO");
        ressaca_        = new PetAttribute("RESSACA");

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
        mapAttrib(intestino());
        mapAttrib(sangue());
        mapAttrib(dinheiro());
        mapAttrib(ressaca());

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
         s2vis_.put(State.POUCOS_AMIGOS, VisibleCondition.RESPIRANDO); // sem motivo, so testando
         s2vis_.put(State.POPULAR, VisibleCondition.NORMAL);
         s2vis_.put(State.SUPERSTAR, VisibleCondition.NORMAL);
        s2vis_.put(State.IMUNDO, VisibleCondition.NORMAL); // low prio, reverts to other anim
         s2vis_.put(State.MUITO_SUJO, VisibleCondition.NORMAL); // same comment
         s2vis_.put(State.SUJO, VisibleCondition.NORMAL); // same
         s2vis_.put(State.LIMPO, VisibleCondition.NORMAL);
         s2vis_.put(State.MUITO_LIMPO, VisibleCondition.NORMAL);
        s2vis_.put(State.EXPULSO, VisibleCondition.NORMAL);
         s2vis_.put(State.REPROVADO, VisibleCondition.NORMAL);
         s2vis_.put(State.RECUPERACAO, VisibleCondition.NORMAL);
         s2vis_.put(State.MEDIANO, VisibleCondition.NORMAL);
         s2vis_.put(State.BOM_ALUNO, VisibleCondition.NORMAL);
         s2vis_.put(State.MELHOR_DA_SALA, VisibleCondition.NORMAL);
		s2vis_.put(State.MORTO_DOENTE, VisibleCondition.MORTO);
         s2vis_.put(State.DOENTE_TERMINAL, VisibleCondition.DOENTE);
         s2vis_.put(State.DOENTE, VisibleCondition.DOENTE);
         s2vis_.put(State.FRAGIL, VisibleCondition.NORMAL);
         s2vis_.put(State.ESTAVEL, VisibleCondition.NORMAL);
         s2vis_.put(State.SAUDAVEL, VisibleCondition.NORMAL);
         s2vis_.put(State.ATLETA, VisibleCondition.NORMAL);
		s2vis_.put(State.PRESO, VisibleCondition.INVISIBLE);
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
		 s2vis_.put(State.INEXPERIENTE, VisibleCondition.NORMAL);
         s2vis_.put(State.APRECIADOR_DO_SEXO, VisibleCondition.NORMAL);
         s2vis_.put(State.SEXO_SELVAGEM, VisibleCondition.NORMAL);
         s2vis_.put(State.MICHAEL_DOUGLAS, VisibleCondition.NORMAL);
         s2vis_.put(State.NINFOMANIACO, VisibleCondition.NORMAL);
        s2vis_.put(State.ANTI_CRISTO, VisibleCondition.NORMAL);
         s2vis_.put(State.FIEL_FERVOROSO, VisibleCondition.NORMAL);
		s2vis_.put(State.NORMAL, VisibleCondition.NORMAL);
        s2vis_.put(State.ONONOONO, VisibleCondition.NORMAL);
        s2vis_.put(State.HOJE, VisibleCondition.RESSACA);
        s2vis_.put(State.AMANHA, VisibleCondition.NORMAL);
        s2vis_.put(State.NORMAL, VisibleCondition.NORMAL);

        a2vis_.put(ActionState.DEFAULT, VisibleCondition.NORMAL);
        a2vis_.put(ActionState.VARRENDO, VisibleCondition.VARRENDO);
        a2vis_.put(ActionState.COMENDO, VisibleCondition.COMENDO);
        a2vis_.put(ActionState.LIMPANDO, VisibleCondition.VARRENDO);
        a2vis_.put(ActionState.GORFANDO, VisibleCondition.VOMITANDO);  // vomitando de vez em quando soltando vomito
        a2vis_.put(ActionState.TOMANDO_BANHO, VisibleCondition.NORMAL);  // por hora..
        a2vis_.put(ActionState.TOMANDO_REMEDIO, VisibleCondition.COMENDO);  // por hora.. parece mastigar remedio
        a2vis_.put(ActionState.JOGANDO_BOLA,  VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.JOGANDO_VIDEOGAME,  VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.ASSITINDO_TV,  VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.LENDO_QUADRINHOS,  VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.LENDO_LIVRO,  VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.ASSISTINDO_ANIME, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.ASSISTINDO_CINE_PRIVE, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.DESENHANDO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.USANDO_CELULAR, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.USANDO_REDE_SOCIAL, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.JOGANDO_RPG, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.ESCOVANDO_DENTES, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.PASSANDO_PERFUME, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.FUMANDO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.MASTURBANDO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.SAINDO_DE_CASA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.TOMANDO_GLICOSE, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.LEVANDO_CURATIVO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.TOMANDO_VACINA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.TOMANDO_ESTOMAZIL, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.TOMANDO_ENGOV, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.TOMANDO_VIAGRA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.LEVANDO_CHINELADA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.LEVANDO_GRITO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.SENDO_CASTIGADO, VisibleCondition.CHORANDO);
        a2vis_.put(ActionState.LEVANDO_CHICOTADA, VisibleCondition.CHORANDO);
        a2vis_.put(ActionState.LIGANDO, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.CONVIDANDO_COLEGA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.MEXENDO_CELULAR, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.FAZENDO_FESTA, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.BOOTY_CALLING, VisibleCondition.UNDETERMINED);
        a2vis_.put(ActionState.ESTUDANDO, VisibleCondition.UNDETERMINED);


        sNutricao_      = new PetAttributeState();
        sHumor_         = new PetAttributeState();
        sSocial_        = new PetAttributeState();
        sHigiene_       = new PetAttributeState();
        sEstudo_        = new PetAttributeState();
        sSaude_         = new PetAttributeState();
        sDisciplina_    = new PetAttributeState();
        sAlcool_        = new PetAttributeState();
        sVida_          = new PetAttributeState();
        sSexualidade_   = new PetAttributeState();
        sFe_            = new PetAttributeState();
        sRessaca_       = new PetAttributeState();

        sAction_.updateState(ActionState.DEFAULT);
        sCoco_.updateState(TipoCoco.NORMAL);
        sVomito_.updateState(TipoVomito.NORMAL);
        sCelular_.updateState(TipoCelular.NENHUM);
        sAge_.updateState(AgeStage.BEBE);
        sRessaca_.updateState(AgeStage.);

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
        sRessaca_.set(ressaca());
            mapAttrib(sRessaca());

        sAtt_.put(AttributeID.NUTRICAO, sNutricao());
        dprint("[satr dbg]: " + sAtt_.get(AttributeID.NUTRICAO));

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
        sAtt_.put(AttributeID.RESSACA, sRessaca());

        // links Alcool state to Ressaca
        //sAlcool_.map().connect(ressaca.AlcoholSlot());

        sAlcool_.map().connect(new Slot<Integer> () {
            @Override public void onEmit (Integer value) {
                // if (value ==  State.COMA_ALCOOLICO.ordinal())
                if (sAlcool_.getState() == State.COMA_ALCOOLICO) {
                    if (ressaca_.get() == TODAY)
                        ressaca_.set(TODAY_TOMORROW);
                    else
                        ressaca_.set(TOMORROW);
                }
            }
        });
    }

    /**
     * To be called in PetAttributesLoader.
     *
     * This enables one to interconnect signals/slots after all basic components
     * have been constructed.
     */
    public void hookupReactiveWires() {
        // hookup AttributeStates to respond to Attributes
        //        sAlcool_.listen();
        //        sNutricao_.listen();
        for (AttributeID a : sAtt_.keySet()) {
            sAtt_.get(a).listen();
        }
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
    void setVisibleCondition(VisibleCondition v) {
        if (blockedVisibleCondition_)
            return;
        setVisibleConditionUnblocked(v);
    }

    void setVisibleConditionUnblocked(VisibleCondition v) {
        vis_.update(v.ordinal());
    }

    void blockVisibleCondition() {
        blockedVisibleCondition_ = true;
    }

    void unblockVisibleCondition() {
        blockedVisibleCondition_ = false;
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
        if (blockedVisibleCondition_)
            return visibleCondition();
        // priority
        // TODO: todos entre -20 e 0 tem prioridade mais alta que o resto.

        dprint( "[vis-priority]: -------------");
        int maxPrio = -1;
        for (AttributeID a : sAtt_.keySet()) {
//            sAtt_.get(a).print();
//            System.out.println("_+_+state: " + sAtt_.get(a).getState() + " attId: " + a);
            dprint("[vis-priority]: entering");
            if (sAtt_.get(a).getState() == null) {
                continue;
            }
            dprint("[vis-priority]: evaluating state, vis: " +
                    sAtt_.get(a).getState() + ", " + s2vis_.get(sAtt_.get(a).getState()));
            if (s2vis_.get(sAtt_.get(a).getState()).ordinal() > maxPrio)
                maxPrio = s2vis_.get(sAtt_.get(a).getState()).ordinal();
        }

        //  specific logic for certain attributes
        if (sAction().getState() != null) {
            dprint("[vis-priority]: evaluating state, vis: " +
                    "sAction" + ", " + a2vis_.get(sAction().getState()));
            assert a2vis_.get(sAction().getState()) != null : "no VisualCondition for Action " + sAction().getState();
            if (a2vis_.get(sAction().getState()).ordinal() > maxPrio)
                maxPrio = a2vis_.get(sAction().getState()).ordinal();
        }

        if (maxPrio == -1) {  // the attributeStates have not been updated yet by the underlying attrib.
            vis_.update(VisibleCondition.NORMAL.ordinal());
            return VisibleCondition.NORMAL;
        }
        dprint("[vis-priority]: resulting active visuals: " + VisibleCondition.values()[maxPrio]);
        assert maxPrio != -1 : "either ms is empty or prio vector has negative entries";
//        dprint("_+_+ vis: " + VisibleCondition.values()[vis_.get()]);
        vis_.update(maxPrio);
//        vis_.update(VisibleCondition.PULANDO.ordinal());
        return visibleCondition();
    }

    public boolean isInitialized() {
        for (String key : ms_.keySet()) {
            if (!ms_.get(key).isInitialized())
                return false;
        }
        return true;
    }

    /**
     * Sets the game speed.
     * This is initially set by the loader / constructor class.
     * But we sometimes want to dynamically update game speed.
     */
    public void setSimulationSpeed(double beatsCoelhoHoraNew_) {
        if (beatsCoelhoHora_ != 0)
            for (String key : m_.keySet()) // for each attribute, set its speed
                m_.get(key).setPassiveBeats(beatsCoelhoHoraNew_*m_.get(key).passiveBeats()/beatsCoelhoHora_);
        beatsCoelhoHora_ = beatsCoelhoHoraNew_;
    }

    /**
     * Updates attributes passively with time
     */
    public void passiveUpdateDay(int beat) {
        // for each attribute, sum passive.
        for (String key : m_.keySet())
            m_.get(key).updatePassiveDay(beat);
    }

    public void passiveUpdateNight(int beat) {
        // for each attribute, sum passive.
        for (String key : m_.keySet())
            m_.get(key).updatePassiveNight(beat);
    }

    /**
     * example of game logic depending on multiple attributes
     */
    public void print() {
        for (String key : m_.keySet())
            m_.get(key).print();
         for (String key : ms_.keySet())
            ms_.get(key).print();
        pprint("[sattr] ageStage: " + sAge().getState());
        pprint("[sattr] coco: " + sCoco().getState());
        pprint("[sattr] vomito: " + sVomito().getState());
        pprint("[sattr] celular: " + sCelular().getState());
        pprint("[sattr] action: " + sAction().getState());
    }
}
