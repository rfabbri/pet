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
package com.pulapirata.core.utils;
import java.util.ArrayList;
import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.AttributeID;
import com.pulapirata.core.PetAttributes.AgeStage;
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.Triggers;
import com.pulapirata.core.Trigger.Modifiers;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Reads game attribute data from a .json file.
 * mimmicks PeaLoader.java
 */
public class TriggerLoader {

    public static void CreateTriggers(String path, final Callback<Triggers> callback) {
        final Triggers triggers = new Triggers();

        // load the attributes
        PlayN.assets().getText(path, new Callback.Chain<String>(callback) {
            @Override
            public void onSuccess(String resource) {
                // create an asset watcher that will call our callback when all assets
                // are loaded
                AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
                    @Override
                    public void done() {
                        callback.onSuccess(triggers);
                    }

                    @Override
                    public void error(Throwable e) {
                        callback.onFailure(e);
                    }
                });

                Json.Object document = PlayN.json().parse(resource);

                // parse the attributes, adding each asset to the asset watcher
                Json.Array jsonTriggers = document.getArray("Triggers");
                assert jsonTriggers != null : "Triggers tag not found";
                for (int i = 0; i < jsonTriggers.length(); i++) {
                    Json.Object jtr = jsonTriggers.getObject(i);
                    String triggerName = jtr.getString("name").toUpperCase().replace(' ', '_');
                    dprint("[triggerloader] reading name: " + triggerName);

                    // set internal atributes ---
                    dprint("[triggerloader] " + triggers.get(triggerName));
                    triggers.get(triggerName).setAction(
                            ActionState.valueOf(jtr.getString("action").toUpperCase().replace(' ', '_')));
                    triggers.get(triggerName).setDuration(jtr.getInt("duration"));
                    triggers.get(triggerName).setCost(jtr.getInt("cost"));

                    // set modifiers ---
                    Modifiers m = triggers.get(triggerName).m();
                    Json.Object jmod = jsonTriggers.getObject(i).getArray("Modifiers").getObject(0);
                    assert jmod != null : "[triggerLoader] required modifiers not found";

                    for (AttributeID a : AttributeID.values()) {  // for each possible attribute / modifier value
                        switch (a) {
                            /* These are not in AttributeID yet TODO */
                            case TIPO_COCO:
                            case TIPO_CELULAR:
                                 if (jmod.getString(a.toString()) != null) {
                                    String t = jmod.getString(a.toString()).toUpperCase().replace(' ', '_');
                                    m.initModifier(a);
                                    m.setValue(a, PetAttributes.TipoCoco.valueOf(t).ordinal());
                                    dprint("[triggerLoader] celular in json : " + a);
                                 } else {
                                    dprint("[triggerLoader] not found modifiers in current modifier for : " + a);
                                 }
                                break;
                            default:
                                 // simple delta case
                                 int ai = jmod.getInt(a.toString().toLowerCase());
//                                     dprint("[triggerLoader] Log: modifier for attribute " + a +
//                                             " not found, assuming default or jSON comment.");
                                 m.initModifier(a);
                                 m.setDeltaValue(a, ai);
                                break;
                             // other cases:
                             //    - m.setPassivoDelta(attr, v);
                        }
                        // TODO read tipo coco here
                        dprint("[triggerloader] todo: parse tipo coco tipo celular");
                    }
                    triggers.get(triggerName).setModifiers(m);

                    // set agestage ---
                    Json.Object jas = jsonTriggers.getObject(i).getArray("AgeStage").getObject(0);
                    assert jas != null : "[triggerLoader] required AgeStage not found";

                    for (AgeStage ass : AgeStage.values())  {
                        String b = jas.getString(ass.toString());

                        // try each age state
                        if (b == null)
                            dprint("[triggerLoader] Log: age state " + ass +  " NOT blocked or defaulted.");
                        else {
                            if (b.equals("lock") || b.equals("locked")) {
                                triggers.get(triggerName).blackList(ass);
                            } else {
                                dprint("[triggerLoader] Log: not found \"lock\" for " + ass +  ", found " + b + " : assuming unlocked.");
                            }
                        }
                    }
                }

                assert triggers.isInitialized() : "[triggerLoader] not all triggers initialized";

                // start the watcher (it will call the callback when everything is
                // loaded)
                assetWatcher.start();
            }
        });
    }
}
