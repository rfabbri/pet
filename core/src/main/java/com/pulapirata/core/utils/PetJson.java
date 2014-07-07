package com.pulapirata.core.utils;

import static playn.core.PlayN.log;
import static playn.core.PlayN.json;

import playn.core.GroupLayer;
import playn.core.util.Callback;
import playn.core.Json;
public class PetJson{

public static int parseJson(String json, String field) {//Image[] images, Sprite sprite, 
    Json.Object document = json().parse(json);

    // parse image urls, if necessary
    /*if (images == null || images.length == 0) {
      Json.Array urls = document.getArray("urls");
      Asserts.checkNotNull(urls, "No urls provided for sprite images");
      images = new Image[urls.length()];
      for (int i = 0; i < urls.length(); i++) {
      images[i] = assets().getImage(urls.getString(i));
      }
      }*/

    // parse the sprite images
    //Json.Array spriteImages = document.getArray("sprites");
    //for (int i = 0; i < spriteImages.length(); i++) {
    Json.Object valueObject = document.getObject(field);
    int value = valueObject.getInt(field);
    System.out.println(value);
    //int imageId = jsonSpriteImage.getInt("url"); // will return 0 if not specified
    //Asserts.checkElementIndex(imageId, images.length, "URL must be an index into the URLs array");
    //int x = jsonSpriteImage.getInt("x");
    //int y = jsonSpriteImage.getInt("y");
    //int width = jsonSpriteImage.getInt("w");
    //int height = jsonSpriteImage.getInt("h");
    //SpriteImage spriteImage = new SpriteImage(images[imageId], x, y, width, height);
    //sprite.addSpriteImage(id, spriteImage);
    //}
    return value;
  }
}
