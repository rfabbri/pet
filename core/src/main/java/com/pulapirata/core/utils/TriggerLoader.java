package com.pulapirata.core.sprites;
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
public class TriggerLoader {

    public static void CreateTriggerSet(String path, final double beatsCoelhoHora,
                                        final Callback<PetAttributes> callback) {
        final TriggerSet triggers = new TriggerSet();

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
                Json.Array jsonTriggers = document.getArray("Attributes");
                for (int i = 0; i < jsonTriggers.length(); i++) {
                    Json.Object jatt = jsonAttributes.getObject(i);
                    dprint("reading name: " + jatt.getString("name"));


                    Modifiers m;


                    // set agestage
                    for (i = 0; i < ) // for each element in "AgeStage"
                        triggers.blackList(AgeStage.valueOf(string));
                    // set modifiers
                    for (i = 0; i < ) // for each element in "Modificadores"
                        triggers.get(jatt.getString("name")).setModifier();

                    triggers.get(jatt.getString("name")).set(
                        jatt.getString("name"),
                        Action.valueOf(jatt.getInt("acao")),
                        m
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
