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
                    pprint("[messageloader] reading message for state: " + messageState);

                    // set internal atributes ---

                    messages.ms_.put(messageState, jm.getString("message"));
                }

                messages.initMessages();

                // start the watcher (it will call the callback when everything is
                // loaded)
                assetWatcher.start();
            }
        });
    }
}
