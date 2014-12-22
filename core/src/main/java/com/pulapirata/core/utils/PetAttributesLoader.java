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
