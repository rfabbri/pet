package com.pulapirata.core.sprites;
import com.pulapirata.core.Trigger;
import static com.pulapirata.core.utils.Puts.*;

/**
 * The collection of the game's triggers.
 * Indexed by the trigger enum.
 */
public class Triggers {
    public static String JSON_PATH = "pet/jsons/triggers.json";

    public enum TriggerType {
        SOPA_DE_CENOURA,
        SOPA_DE_BACON,
        SALADA_COM_LEGUMES,
        CHOCOLATE,
        PIZZA,
        AGUA,
        LEITE,
        BOLA,
        QUADRINHOS,
        LIVRO,
        VIDEOGAME,
        TV,
        ANIME,
        CINE_PRIVE,
        DESENHAR,
        LIGAR_PARA_AMIGO,
        CONVIDAR_COLEGA,
        REDE_SOCIAL,
        FAZER_FESTA,
        JOGAR_RPG,
        BOOTY_CALL,
        TOMAR_BANHO,
        ESCOVAR_DENTES,
        VARRER,
        PASSAR_PERFUME,
        PINGUINHA_NA_CHUPETA,
        BOMBOM_DE_LICOR,
        CERVEJA,
        SUCO_DE_MACACO,
        CIGARRO,
        MASTURBAR,
        SAIR_PARA_ESCOLA,
        SAIR_PARA_PARQUE,
        SAIR_PARA_IGREJA,
        DAR_GLICOSE,
        REMEDIO,
        CURATIVO,
        CHINELADA,
        ESTUDAR,
        GRITAR,
        CASTIGAR,
        CHICOTEAR
    }

    /** map from TriggerType to modifier class.
     * Stores the triggers, indexed by TriggerType */
    protected EnumMap<TriggerType, Trigger> map_ = new EnumMap<TriggerType, Trigger> (AttributeID.class);

    public Trigger() {
        for (TriggerType tt : TriggerType.values()) {
            Trigger trig = new Trigger();
            map_.put(tt, trig);
        }
    }

    public Trigger get(TriggerType t) { map_.get(t) }
}
