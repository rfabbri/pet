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

    public static void CreateTriggers(String path, final double beatsCoelhoHora,
                                        final Callback<PetAttributes> callback) {
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
                        callback.onSuccess(attribs);
                    }

                    @Override
                    public void error(Throwable e) {
                        callback.onFailure(e);
                    }
                });

                Json.Object document = PlayN.json().parse(resource);

                // parse the attributes, adding each asset to the asset watcher
                Json.Array jsonTriggers = document.getArray("Triggers");
                for (int i = 0; i < jsonTriggers.length(); i++) {
                    Json.Object jtr = jsonAttributes.getObject(i);
                    String triggerName = jtr.getString("name");
                    dprint("reading name: " + triggerName);

                    // set internal atributes ---

                    triggers.get(triggerName).setDuration(jtr.getInt("duration"));
                    triggers.get(triggerName).setCost(jtr.getInt("cost"));

                    // set modifiers ---
                    Modifiers m = new Modifiers();
                    Json.Array jmods = jsonAttributes.getObject(i).getArray("Modifiers");
                    assert jmod != null : "[triggerLoader] required modifiers not found";

                    for (k = 0; k < jmod.length(); ++k) { // for each element in "Modificadores"
                        Json.Object jmatt = jsonStates.getObject(k);
                        for (AttributeID a : AttributeID.values()) {  // for each possible attribute / modifier value
                            switch (a) {
                                case TIPO_COCO:
                                case CELULAR:
                                    dprint("[triggerLoader] Warning: modifier not implemented for " + a);
                                    break;
                                default:
                                     // simple delta case
                                     int ai = jmatt.getInt(a.toString());

                                     if (ai == 0)
                                         dprint("[triggerLoader] Log: modifier for attribute " + a +  " not found, assuming default or jSON comment.");
                                     else {
                                         boolean retval = m.setDeltaValue(a, ai);
                                         assert retval;
                                     }
                                    break;
                                 // other cases:
                                 //    - m.setPassivoDelta(attr, v);
                            }
                        }
                    }
                    triggers.get(triggerName).setModifiers(m);

                    // set agestage ---
                    Json.Array jas;
                    jas = jsonAttributes.getObject(i).getArray("AgeStage");
                    if (jas == null) {
                        dprint("Tryig Age Stage with space");
                        jas = jsonAttributes.getObject(i).getArray("Age Stage");
                        assert jas != null : "[triggerLoader] required AgeStage not found";
                    }

                    for (AgeStage ass : AgeStage.values())  {
                        int as = jmatt.getString(ass.toString());
                        if (as == 0)
                            dprint("[triggerLoader] Log: age state " + ass +  " NOT blocked or defaulted.");
                        else {
                            if (jmat.getString(as) == "blocked") {
                                triggers.get(triggerName).blackList(as);
                            } else {
                                dprint("[triggerLoader] Log: not found blocked for " + ass +  ", assuming blocked.");
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
