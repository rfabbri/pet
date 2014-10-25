package com.pulapirata.core.sprites;

/**
 * Basic base for any spriter based on a global {@link Atlas}.
 * This takes care of actually performing an animation.
 * It is analogous to the old Sprite class but with global source image / atlas.
 */
public class SpriteBase {

    /** pointer to global atlas */
    Atlas atlas;
    private ImageLayer layer;
    private List<SpriteImage> spriteImages;
    private HashMap<String, Integer> spriteIdMap;
    private SpriteImage current;
    private int currentId = -1;

    /**
     * Do not call directly. Create using {@link SpriteLoader#getSprite(String, String)}
     */
    Sprite(ImageLayer imageLayer, Atlas atlas) {
        this.layer = imageLayer;
        spriteImages = new ArrayList<SpriteImage>(0);
        spriteIdMap = new HashMap<String, Integer>();
    }

    /**
     * Return the sprite {@link ImageLayer}.
     */
    public ImageLayer layer() {
        return layer;
    }

    /**
     * Return the number of sprites.
     */
    public int numSprites() {
        return (spriteImages == null ? 0 : spriteImages.size());
    }

    /**
     * Return the height of the current sprite.
     */
    public float height() {
      if (current != null) {
        return current.height();
      } else {
        return 1;
      }
    }

    /**
     * Set the current sprite via the index.
     * <p>
     * The index is an integer between 0 and the number of sprites (@see {@link #numSprites()})
     */
    public void setSprite(int index) {
      Asserts.checkElementIndex(index, spriteImages.size(), "Invalid sprite index");
      if (index != currentId) {
        current = spriteImages.get(index);
        currentId = index;
        updateLayer();
      }
    }

    /**
     * Set the current sprite via the sprite's id.
     */
    public void setSprite(String id) {
      setSprite(Asserts.checkNotNull(spriteIdMap.get(id), "Invalid sprite id"));
    }

    /**
     * Return the width of the current sprite.
     */
    public float width() {
      if (current != null) {
        return current.width();
      } else {
        return 1;
      }
    }

    /**
     * Add a {@link SpriteImage} to the sprites.
     */
    void addSpriteImage(String key, SpriteImage spriteImage) {
      spriteIdMap.put(key, spriteImages.size());
      spriteImages.add(spriteImage);
    }

    /**
     * Returns the {@link SpriteImage}s associated with this Sprite.
     */
    List<SpriteImage> spriteImages() {
      return spriteImages;
    }

    /**
     * Update the Sprite layer.
     */
    private void updateLayer() {
      if (current != null) {
        layer.setImage(atlas.image().subImage(current.x(), current.y(),
                                                current.width(), current.height()));
      }
    }
}
