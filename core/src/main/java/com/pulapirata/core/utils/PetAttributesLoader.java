package com.pulapirata.core.utils;

// mimmicks PeaLoader.java
public class PetAttributesLoader {

  public static void CreateAttributes(String path, final double beatsCoelhoHora
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

        // previous Portal (used for linking portals)
        Portal lastPortal = null;

        // parse the attributes, adding each asset to the asset watcher
        Json.Array jsonAttributes = document.getArray("Attributes");
        for (int i = 0; i < jsonAttributes.length(); i++) {
          Json.Object jsonAttribute = jsonAttributes.getObject(i);
          //String type = jsonEntity.getString("type");
          float x = jsonEntity.getNumber("fome");
          attribs.fome().set(x);
        }

        // start the watcher (it will call the callback when everything is
        // loaded)
        assetWatcher.start();
      }
    });
  }
}
