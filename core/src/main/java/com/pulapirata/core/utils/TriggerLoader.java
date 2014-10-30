package com.pulapirata.core.utils;
import java.util.ArrayList;
import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.AttributeID;
import com.pulapirata.core.PetAttributes.AgeStage;
import com.pulapirata.core.Triggers;
import com.pulapirata.core.Trigger.Modifiers;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Reads game attribute data from a .json file.
 * mimmicks PeaLoader.java
 */
public class TriggerLoader {

    public static void CreateTriggers(String path, final double beatsCoelhoHora,
                                        final Callback<Triggers> callback) {
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
                    pprint("[triggerloader] reading name: " + triggerName);

                    // set internal atributes ---

                    pprint("[triggerloader] " + triggers.get(triggerName));
                    triggers.get(triggerName).setDuration(jtr.getInt("duration"));
                    triggers.get(triggerName).setCost(jtr.getInt("cost"));

                    // set modifiers ---
                    Modifiers m = triggers.get(triggerName).m();
                    Json.Object jmod = jsonTriggers.getObject(i).getArray("Modifiers").getObject(0);
                    assert jmod != null : "[triggerLoader] required modifiers not found";

                    for (AttributeID a : AttributeID.values()) {  // for each possible attribute / modifier value
                        switch (a) {
                            /* These are not in AttributeID yet TODO
                            case TIPO_COCO:
                            case TIPO_CELULAR:
                                 if (jmatt.getObject(a.toString()) != null) {
                                    int t = jmod.getInt(a.toString());
                                    m.setValue(a, t);
                                    dprint("[triggerLoader] celular in json : " + a);
                                 } else {
                                    dprint("[triggerLoader] not found modifiers in current modifier for : " + a);
                                 }
                                break;
                                */
                            default:
                                 // simple delta case
                                 int ai = jmod.getInt(a.toString().toLowerCase());
                                 if (ai == 0)
                                     dprint("[triggerLoader] Log: modifier for attribute " + a +
                                             " not found, assuming default or jSON comment.");
                                 else {
                                     m.initModifier(a);
                                     m.setDeltaValue(a, ai);
                                 }
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
                            if (b == "blocked") {
                                triggers.get(triggerName).blackList(ass);
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
