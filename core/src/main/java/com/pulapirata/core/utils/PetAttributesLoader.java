package com.pulapirata.core.utils;

import playn.core.AssetWatcher;
import playn.core.Json;
import playn.core.PlayN;
import playn.core.util.Callback;



// mimmicks PeaLoader.java
public class PetAttributesLoader {

  public static void CreateAttributes(String path, final double beatsCoelhoHora,
  final Callback<PetAttributes> callback) {
    final PetAttributes attribs = new PetAttributes(beatsCoelhoHora);

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

        // parse the level
        Json.Object document = PlayN.json().parse(resource);

        // parse the attributes, adding each asset to the asset watcher
        Json.Array jsonAttributes = document.getArray("Attributes");
        for (int i = 0; i < jsonAttributes.length(); i++) {
          Json.Object jatt = jsonAttributes.getObject(i);
          //String type = jsonEntity.getString("type");
          attribs.m.get(jatt.getString("name")).set(
            jatt.getString("name"),
            jatt.getInt("startValue"),
            jatt.getInt("min"),
            jatt.getInt("max"),
            jatt.getInt("passive"),
            jatt.getInt("passiveBeats")
          );
        }

        // start the watcher (it will call the callback when everything is
        // loaded)
        assetWatcher.start();
      }
    });
  }
}
