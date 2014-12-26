/**
 * Pet - a comic pet simulator game
 * Copyright (C) 2013-2015 Ricardo Fabbri and Edson "Presto" Correa
 *
 * This program is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version. A different license may be requested
 * to the copyright holders.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>
 *
 * NOTE: this file may contain code derived from the PlayN, Tripleplay, and
 * React libraries, all (C) The PlayN Authors or Three Rings Design, Inc.  The
 * original PlayN is released under the Apache, Version 2.0 License, and
 * TriplePlay and React have their licenses listed in COPYING-OOO. PlayN and
 * Three Rings are NOT responsible for the changes found herein, but are
 * thankfully acknowledged.
 */
/**
 * Copyright 2011 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.pulapirata.core.sprites;

import static playn.core.PlayN.assets;
import static playn.core.PlayN.graphics;
import static playn.core.PlayN.json;

import playn.core.Asserts;
import playn.core.AssetWatcher;
import playn.core.Image;
import playn.core.Json;
import playn.core.util.Callback;
import static com.pulapirata.core.utils.Puts.*;

/**
 * Class for loading and parsing sprite sheets.
 * <p>
 * To use, call {@link #getSprite(String imageUrl, String jsonUrl)} with an image path and json
 * data, or {@link #getSprite(String jsonUrl)} with json data containing image urls.
 */
// TODO(pdr): the two getSprite() methods are messy, clean them up.
public class SpriteLoader {

  // prevent instantiation
  private SpriteLoader() {
  }

  /**
   * Return a {@link Sprite}, given a path to the image and a path to the json sprite description.
   * <p>
   * json data should be in the following format:
   *
   * <pre>
   * {
   *   "sprites": [
   *     {"id": "sprite_0", "x": 30, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_1", "x": 67, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_2", "x": 104, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_3", "x": 141, "y": 30, "w": 37, "h": 37}
   * ]}
   * </pre>
   */
  public static Sprite getSprite(String imagePath, final  String jsonPath) {
    Image image = assets().getImage(imagePath);
    final Image[] images = new Image[]{image};
    // temp image to prevent NPE if using the Sprite's Layer (Sprite.getLayer()) before the image
    // has loaded or before a sprite has been set (Sprite.setSprite()).
    final Image tempImage = graphics().createImage(1, 1);
    final Sprite sprite = new Sprite(graphics().createImageLayer(tempImage));

    // load and parse json
    assets().getText(jsonPath, new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        try {
          parseJson(images, sprite, json);
        } catch (Throwable err) {
          printd("[pet json spriteloader parsed]: " + jsonPath);
          sprite.error(err);
          return;
        }
        sprite.doneLoadingData();
      }

      @Override
      public void onFailure(Throwable err) {
        sprite.error(err);
      }
    });

    // set callback for image
    image.addCallback(new Callback<Image>() {
      @Override
      public void onSuccess(Image resource) {
        sprite.doneLoadingImages();
      }

      @Override
      public void onFailure(Throwable err) {
        sprite.error(err);
      }
    });

    return sprite;
  }

  /**
   * Return a {@link Sprite}, given a path to the json sprite description.
   * <p>
   * json data should be in the following format:
   *
   * <pre>
   * {
   *   "urls": ["images/peasprites2.png", "images/peasprites3.png"],
   *   "sprites": [
   *     {"id": "sprite_0", "url": 0, "x": 30, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_1", "url": 0, "x": 67, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_2", "url": 1, "x": 104, "y": 30, "w": 37, "h": 37},
   *     {"id": "sprite_3", "url": 1, "x": 141, "y": 30, "w": 37, "h": 37}
   * ]}
   * </pre>
   */
  public static Sprite getSprite(String jsonPath) {
    // temp image to prevent NPE if using the Sprite's Layer (Sprite.getLayer()) before the image
    // has loaded or before a sprite has been set (Sprite.setSprite()).
    final Image tempImage = graphics().createImage(1, 1);
    final Sprite sprite = new Sprite(graphics().createImageLayer(tempImage));

    // create asset watcher for the image assets
    final AssetWatcher watcher = new AssetWatcher(new AssetWatcher.Listener() {
      @Override
      public void done() {
        sprite.doneLoadingImages();
      }

      @Override
      public void error(Throwable e) {
        sprite.error(e);
      }
    });

    // load and parse json, then add each image parsed from the json to the asset watcher to load
    assets().getText(jsonPath, new Callback<String>() {
      @Override
      public void onSuccess(String json) {
        try {
          parseJson(null, sprite, json);
          for (SpriteImage spriteImage : sprite.spriteImages()) {
            watcher.add(spriteImage.image());
          }
          watcher.start();
        } catch (Throwable err) {
          sprite.error(err);
          return;
        }
        sprite.doneLoadingData();
      }

      @Override
      public void onFailure(Throwable err) {
        sprite.error(err);
      }
    });

    return sprite;
  }

  /**
   * Parse a json sprite sheet and add the sprite images to the sheet.
   * <p>
   * If images is null, the images urls are parsed from the json.
   *
   * @param images Image to associate with each {@link SpriteImage}, or null to parse from the json
   * @param sprite Sprite to store the {@link SpriteImage}s
   * @param json json to parse
   */
  private static void parseJson(Image[] images, Sprite sprite, String json) {
    Json.Object document = json().parse(json);

    // parse image urls, if necessary
    if (images == null || images.length == 0) {
      Json.Array urls = document.getArray("urls");
      Asserts.checkNotNull(urls, "No urls provided for sprite images");
      images = new Image[urls.length()];
      for (int i = 0; i < urls.length(); i++) {
        images[i] = assets().getImage(urls.getString(i));
      }
    }

    // parse the sprite images
    Json.Array spriteImages = document.getArray("sprites");
    for (int i = 0; i < spriteImages.length(); i++) {
      Json.Object jsonSpriteImage = spriteImages.getObject(i);
      String id = jsonSpriteImage.getString("id");
      int imageId = jsonSpriteImage.getInt("url"); // will return 0 if not specified
      Asserts.checkElementIndex(imageId, images.length, "URL must be an index into the URLs array");
      int x = jsonSpriteImage.getInt("x");
//      System.out.println("Valor de X:"+x);
      int y = jsonSpriteImage.getInt("y");
      int width = jsonSpriteImage.getInt("w");
      int height = jsonSpriteImage.getInt("h");
      SpriteImage spriteImage = new SpriteImage(images[imageId], x, y, width, height);
      sprite.addSpriteImage(id, spriteImage);
    }
  }
}
