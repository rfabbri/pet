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
import com.pulapirata.core.PetAttributes;
import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;
import static com.pulapirata.core.utils.Puts.*;


/**
 * Reads game attribute data from a .json file.
 * mimmicks PeaLoader.java
 */
public class PetAttributesLoader {

    public static void CreateAttributes(String path, final double beatsCoelhoHora,
                                        final Callback<PetAttributes> callback) {
        final PetAttributes attribs = new PetAttributes();
        attribs.setSimulationSpeed(beatsCoelhoHora);

        // load the attributes
        PlayN.assets().getText(path, new Callback.Chain<String>(callback) {
            @Override
            public void onSuccess(String resource) {
                // create an asset watcher that will call our callback when all assets
                // are loaded
                AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
                    @Override
                    public void done() {
                        callback.onSuccess(attribs);
                    }

                    @Override
                    public void error(Throwable e) {
                        callback.onFailure(e);
                    }
                });

                Json.Object document = PlayN.json().parse(resource);

                // parse the attributes, adding each asset to the asset watcher
                Json.Array jsonAttributes = document.getArray("Attributes");
                for (int i = 0; i < jsonAttributes.length(); i++) {
                    Json.Object jatt = jsonAttributes.getObject(i);
//                    System.out.println("reading name: " + jatt.getString("name"));
                    attribs.get(jatt.getString("name")).set(
                        jatt.getString("name"),
                        jatt.getInt("startValue"),
                        jatt.getInt("min"),
                        jatt.getInt("max"),
                        jatt.getInt("passiveDay"),
			jatt.getInt("passiveNight"),
                        jatt.getDouble("passiveBeats")*beatsCoelhoHora
                    );

                    Json.Array jsonStates = jsonAttributes.getObject(i).getArray("States");
                    if (jsonStates == null)
                       continue;

                    ArrayList<PetAttributes.State> s = new ArrayList<PetAttributes.State>();
                    ArrayList<Integer> iv = new ArrayList<Integer>();
                    for (int k = 0; k < jsonStates.length(); k++) {
                        Json.Object js = jsonStates.getObject(k);
//                        System.out.println("reading state: " + js.getString("name"));
                        s.add(PetAttributes.State.valueOf(js.getString("name").toUpperCase().replace(' ', '_')));
                        iv.add(js.getInt("max"));
                        assert k != 0 || js.getInt("min") == attribs.get(jatt.getString("name")).min()
                            : "json not consistent with assumption of min of interval equal min of first state";
                    }

                    attribs.sAtt(jatt.getString("name")).set(s, iv);
                }
                attribs.hookupReactiveWires();

                assert attribs.isInitialized() : "not all attributes initialized";

                // start the watcher (it will call the callback when everything is
                // loaded)
                assetWatcher.start();
            }
        });
    }
}
