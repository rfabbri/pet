/**
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

import static playn.core.PlayN.log;

import playn.core.GroupLayer;
import playn.core.util.Callback;

public class PingoComendoSopaBacon{
	public static String IMAGE = "pet/sprites/pingo_bebe_vomitando.png";
	public static String JSON = "pet/sprites/pingo_bebe_vomitando.json";
	private Sprite sprite;
	private int spriteIndex = 0;
	private boolean hasLoaded = false;

	public PingoComendoSopaBacon(final GroupLayer pingoLayer, final float x, final float y){
		sprite = SpriteLoader.getSprite(IMAGE, JSON);	
		sprite.addCallback(new Callback<Sprite>(){
			public void onSuccess(Sprite sprite){
				sprite.setSprite(spriteIndex);
				sprite.layer().setOrigin(sprite.width() / 2f, sprite.height() / 2f);
				sprite.layer().setTranslation(x, y);
				pingoLayer.add(sprite.layer());
				hasLoaded = true;
			}
			public void onFailure(Throwable err){
				log().error("Error loading image!", err);
			}
		});
	}

	public void update(int delta){
		if(hasLoaded){
			spriteIndex = (spriteIndex + 1) % sprite.numSprites();
			sprite.setSprite(spriteIndex);
		}
	}
	
	public void detatch(GroupLayer pingoLayer){
		pingoLayer.remove(sprite.layer());
	}
}
