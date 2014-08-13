package com.pulapirata.core.utils;
import com.pulapirata.core.utils.PetAttributes;

import static playn.core.PlayN.log;
import static playn.core.PlayN.json;

import playn.core.GroupLayer;
import playn.core.util.Callback;
import playn.core.Json;
//-----------------
import playn.core.AssetWatcher;
import playn.core.PlayN;





public class PetJson {

  // prevent instantiation
  private PetJson() {
  }

//  public static PetAttributes parseJson(String jsonPath, String field) {
  public static float parseJson(String jsonPath, String field) {
//1 -------------------------------------------------------
    /*//final PeaWorld peaWorld = new PeaWorld(worldLayer);
    // load the level

    PlayN.assets().getText(jsonPath, new Callback.Chain<String>(null) {
      @Override
      public void onSuccess(String resource) {
        // create an asset watcher that will call our callback when all assets
        // are loaded
        AssetWatcher assetWatcher = new AssetWatcher(new AssetWatcher.Listener() {
          @Override
          public void done() {
            //callback.onSuccess(peaWorld);
          }

          @Override
          public void error(Throwable e) {
            //callback.onFailure(e);
          }
        });

        // parse the level
        Json.Object document = PlayN.json().parse(resource);

        // previous Portal (used for linking portals)
        //Portal lastPortal = null;
  
	float fome = 0;	
        // parse the entities, adding each asset to the asset watcher
        Json.Array jsonEntities = document.getArray("Entities");
        for (int i = 0; i < jsonEntities.length(); i++) {
          Json.Object jsonEntity = jsonEntities.getObject(i);          
	  System.out.println("Fome via arquivo: "+fome);
          fome = jsonEntity.getNumber(field);
	}
      }
    });*/

  final PetAttributes atributos = new PetAttributes();
//2----------------------------------------------------------------------------
  PlayN.assets().getText(jsonPath, new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        try {
	    Json.Object document = PlayN.json().parse(json);
	    // parse the sprite images
	    Json.Array jsonEntities = document.getArray("Entities");
	    for (int i = 0; i < jsonEntities.length(); i++) {
	      Json.Object jsonEntity = jsonEntities.getObject(i);
	      atributos.set_fome(jsonEntity.getNumber("fome"));
	      //atributos.set_fome(jsonEntity.getNumber(field));
            }	    
	}
	catch (Throwable err) {
          //sprite.error(err);
          return;
        }
      }
      @Override
      public void onFailure(Throwable err) {
      }
  });

//3----------------------------------------------------------------------------
   // parse the level
    /*Json.Object document = PlayN.json().parse(jsonPath);
    float fome = 0;
    // parse the entities, adding each asset to the asset watcher
    Json.Array jsonEntities = document.getArray("Entities");
    for (int i = 0; i < jsonEntities.length(); i++) {
      Json.Object jsonEntity = jsonEntities.getObject(i);
      fome = jsonEntity.getNumber(field);
    }*/ 
    return atributos.fome();
  }
}
