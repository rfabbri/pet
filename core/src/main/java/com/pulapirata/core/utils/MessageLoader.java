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
import com.pulapirata.core.Messages;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Reads game attribute data from a .json file.
 * mimmicks PeaLoader.java
 */
public class MessageLoader {

    public static void CreateMessages(String path, final Callback<Messages> callback) {
        final Messages messages = new Messages();

        // load the messages
        PlayN.assets().getText(path, new Callback.Chain<String>(callback) {
            @Override
            public void onSuccess(String resource) {
                // create an asset watcher that will call our callback when all assets
                // are loaded
                AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
                    @Override
                    public void done() {
                        callback.onSuccess(messages);
                    }

                    @Override
                    public void error(Throwable e) {
                        callback.onFailure(e);
                    }
                });

                Json.Object document = PlayN.json().parse(resource);

                // parse the attributes, adding each asset to the asset watcher
                Json.Array jsonMessages = document.getArray("StateMessages");
                assert jsonMessages != null : "StateMessages tag not found";
                for (int i = 0; i < jsonMessages.length(); i++) {
                    Json.Object jm = jsonMessages.getObject(i);
                    String messageState = jm.getString("state").toUpperCase().replace(' ', '_');
                    dprint("[messageloader] reading message for state: " + messageState);
                    dprint("[messageloader] message is: " + jm.getString("message"));

                    // set internal atributes ---

                    messages.ms_.put(PetAttributes.State.valueOf(messageState), jm.getString("message"));
                }

                // start the watcher (it will call the callback when everything is
                // loaded)
                assetWatcher.start();
            }
        });
    }
}
