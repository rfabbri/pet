package com.pulapirata.core.sprites;


/**
 * Atlas manages a common image used by sprites.
 * Sprites in this game are mere lists of indices to the atlas.
 */
public class Atlas {
    private ImageLayer layer;
    private final Image image;
    private Callback<Sprite> callback_;
    private boolean imageDone = false; // true when atlas image has finished loading


    Atlas(ImageLayer subImageLayer) {
        this.layer = imageLayer;
    }

    /**
     * Set callback that will be called when the atlasimage have been loaded.
     */
    public void addCallback(Callback<Atlas> callback) {
        this.callback = callback;
        if (isReady()) {
            callback.onSuccess(this);
        }
    }

    /**
     * Return true when the atlas image has been loaded.
     * <p>
     * @see #addCallback(Callback)
     */
    public boolean isReady() {
        return imageDone;
    }

    /**
     * Should be called when the atlas sprite image has been loaded. Will handle calling
     * the {@link Callback} of the {@link Sprite}.
     */
    void done() {
      if (callback != null) {
        callback.onSuccess(this);
      }
    }

    /**
     * Should be called when the image is done loading.
     */
    void doneLoadingImage() {
      imageDone = true;
      if (isReady()) {
        done();
      }
    }

    /**
     * Should be called if an error occurs when loading the sprite image or data. Will handle calling
     * the {@link Callback} of the {@link Sprite}.
     */
    void error(Throwable err) {
      if (callback != null) {
        callback.onFailure(err);
      } else {
        // don't let the error fall on deaf ears
        log().error("Error loading sprite", err);
      }
    }
    /**
     * Update the Sprite layer.
     */
    private void updateLayer() {
      if (current != null) {
        layer.setImage(current.image().subImage(current.x(), current.y(),
                                                current.width(), current.height()));
      }
    }
}
