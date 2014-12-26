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
import com.pulapirata.core.Trigger;
//import com.pulapirata.core.PetAttributes.AttributeID;
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
        CHICOTEAR,
        INVALID,
        NOT_IMPLEMENTED
    }

    /** map from TriggerType to modifier class.
     * Stores the triggers, indexed by TriggerType */
    protected EnumMap<TriggerType, Trigger> map_ = new EnumMap<TriggerType, Trigger> (TriggerType.class);

    public Triggers() {
        for (TriggerType tt : TriggerType.values()) {
            Trigger trig = new Trigger();
            map_.put(tt, trig);
        }
    }

    public Trigger get(TriggerType t) { return map_.get(t); }
    public Trigger get(String ts)     { return get(TriggerType.valueOf(ts)); }
    public boolean isInitialized() {
        for (TriggerType tt : map_.keySet()) {
            if (!map_.get(tt).isInitialized())
                return false;
        }
        return true;
    }

    /**
     * Takes care of action timing
     */
    public void update(int delta) {
        if (!isInitialized())
            return;
        for (TriggerType tt : map_.keySet()) {
            map_.get(tt).update(delta);
        }
    }
}
