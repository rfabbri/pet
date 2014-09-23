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


import java.util.Timer;

/**
 * Class for loading and parsing pet's attribute data
 *
 */
public class PetJson {
  public static int test = -4;

  // prevent instantiation
  private PetJson() {
  }

/*  public static PetAttributes parseJson(String jsonPath, String field) {
    System.out.println("entrou no arquivo");
    final PetAttributes atributos = new PetAttributes();
    System.out.println("Antes do onSuccess");
    PlayN.assets().getText(jsonPath, new Callback<String>() {
           @Override
  public void onSuccess(String json) {
    try {
      Json.Object document = PlayN.json().parse(json);
      // parse the sprite images
      Json.Array jsonEntities = document.getArray("Entities");
      for (int i = 0; i < jsonEntities.length(); i++) {
        Json.Object jsonEntity = jsonEntities.getObject(i);
        System.out.println("Fome via arquivo antes de atribuir: "+atributos.fome());
        atributos.set_fome((int)jsonEntity.getNumber("fome"));
              System.out.println("Fome via arquivo depois de atribuir: "+atributos.fome());
        //atributos.set_fome(jsonEntity.getNumber(field));
            }
      test = atributos.fome();
      System.out.println("Test depois de receber fome:"+test);
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
    System.out.println("Fome final do arquivo: "+atributos.fome());
    System.out.println("test final do arquivo: "+test);
    return atributos;
  }*/
  public static void pausa(){
    try {
        Thread.sleep(1000);
    }
    catch (Exception ignored) {}
  }

  public static PetAttributes readJson(String jsonPath, final String field, float beatsCoelhoHora) {
//    Timer time = new Timer();
    final PetAttributes atributos = new PetAttributes(beatsCoelhoHora);
    PlayN.assets().getText(jsonPath, new Callback<String>() {
//      Thread.yield();

      @Override
      public void onSuccess(String json) {
        try {
          Thread.yield();
          System.out.println("ENTROU NO TRY");
          parseJson(atributos, json, field);
          //atributos.set_fome(parseJson(atributos, json, field));
          ////Coloquei pra parseJson retornar int / PetAttributes, mas continuou o erro

          /*for (SpriteImage spriteImage : sprite.spriteImages()) {
            watcher.add(spriteImage.image());
          }
          watcher.start();*/
        } catch (Throwable err) {
          //sprite.error(err);
          return;
        }
        //sprite.doneLoadingData();
      }

      @Override
      public void onFailure(Throwable err) {
        System.out.println("Entrou no onFailure");
  //sprite.error(err);
      }
    });
//    pausa();
//    Thread.yield();
//    time.start();
    System.out.println("Fome final do metodo READJSON: "+atributos.fome());
    return atributos;
  }

  private static void parseJson(PetAttributes atributos, String json, String field) {
    System.out.println("entrou no arquivo");
    Json.Object document = PlayN.json().parse(json);
    // parse the sprite images
    Json.Array jsonEntities = document.getArray("Entities");
    for (int i = 0; i < jsonEntities.length(); i++) {
      Json.Object jsonEntity = jsonEntities.getObject(i);
      System.out.println("Fome via arquivo antes de atribuir: "+atributos.fome().val());
      atributos.fome().set((int)jsonEntity.getNumber(field));
      System.out.println("Fome via arquivo depois de atribuir: "+atributos.fome().val());
    }
    System.out.println("Fome final do metodo PARSEJSON: "+atributos.fome().val());
  }

}
