package com.pulapirata.core;

import tripleplay.entity.Component;
import tripleplay.entity.Entity;
import tripleplay.entity.System;
import tripleplay.entity.World;


/**
 * An entity world designed for character animation and interaction among
 * multiple other objects. Call this the <em>extrinsic world</em>. Pet's extrinsic
 * properties are efficiently updated together with the rest of the objects that
 * share those properties - position, velocity, etc.
 */
class PetWorld extends World {
    /*-------------------------------------------------------------------------------*/
    /** Misc variables */

    public final GroupLayer layer_;
    public final Signal<Key> keyDown_ = Signal.create();
    public final Signal<Key> keyUp_ = Signal.create();

    /*-------------------------------------------------------------------------------*/
    /** Types of entities */
    public static final int PET      = (1 << 0);
    public static final int DROPPING = (1 << 1);
    public static final int VOMIT    = (1 << 2);
    public static final int DIARRHEA = (1 << 3);
    public static final int MOSQUITOS = (1 << 4);
    public static final int STINKY_MOSQUITOS = (1 << 5);

    /*-------------------------------------------------------------------------------*/
    /** Components.
     * Components are bags of types, positions, and other properties shared among
     * playable entities in Pet (like the bunny itself and its droppings)
     */
    public final Component.IMask type_ = new Component.IMask(this);
    public final Component.XY pos_ = new Component.XY(this);
    public final Component.XY vel_ = new Component.XY(this); // pixels/ms
    public final Component.IScalar expires_ = new Component.IScalar(this);
    private final Randoms rando_ = Randoms.with(new Random());

    /*-------------------------------------------------------------------------------*/
    /** Time data */
    private int beat_ = 0; // total number of updates so far

    /*-------------------------------------------------------------------------------*/
    /** Misc methods */

    @Override public void update (int delta) {
        beat_ += delta;
        super.update(delta);
    }

    public PetWorld (GroupLayer stage) {
        this.stage = stage;

        keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown (Keyboard.Event event) {
                keyReloadGameFile.emit(event.key());
            }
        });
    }

    protected String typeName (int id) {
        switch (type.get(id)) {
        case PET: return "pet";
        case DROPPING: return "dropping";
        case VOMIT: return "vomit";
        case DIARRHEA: return "diarrhea";
        case MOSQUITOS: return "mosquitos";
        case STINKY_MOSQUITOS: return "stinky_mosquitos";
        default: return "unknown:" + type.get(id);
      }
    }

    /*-------------------------------------------------------------------------------*/
    /** Systems */

    /** Simple motion. Handles updating entity position based on entity velocity */
    public final System mover = new System(this, 0) {
        @Override protected void update (int delta, Entities entities) {
            Point p = pos_;
            Vector v = vel_;
            for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
                int eid = entities.get(ii);
                pos_.get(eid, p); // get our current pos
                vel.get(eid, v).scaleLocal(delta); // turn velocity into delta pos
                pos.set(eid, p.x + v.x, p.y + v.y); // add velocity
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return entity.has(pos) && entity.has(vel);
        }

        protected final Point  _pos = new Point();
        protected final Vector _vel = new Vector();
    };

    /** Use keys to control pet. Like in minigames. Pet should automatically move
     * and do something fun if no control is pressed. NOOP if touchscreen
     * or gamepad are available.
     */
    public final System walkControls = new System(this, 1) {
        /* ctor */ {
            keyDown.connect(new Slot<Key>() {
                @Override public void onEmit (Key key) {
                    switch (key) {
                      // TODO colocar estado walk_velocity_ na classe pet?
                      case LEFT:  vel_.x = -WALK_VELOCITY;  vel_.y = 0;  break;
                      case RIGHT: vel_.x  =  WALK_VELOCITY;  vel_.y = 0;  break;
                      case UP:    vel_.x  =  0;  vel_.y =  WALK_VELOCITY;  break;
                      case DOWN:  vel_.x  =  0;  vel_.y = -WALK_VELOCITY;  break;
                      case SPACE: System.out.println("Key SPACE pressed: u mean jump?"); break;
                      case C: System.out.println("Key C pressed: u mean taka dump?"); break;
                    default: break;
                    }
                }
            });
            keyUp.connect(new Slot<Key>() {
                @Override public void onEmit (Key key) {
                    switch (key) {
                      case LEFT:  vel_.x = 0; vel_.y = 0; break;
                      case RIGHT: vel_.x = 0; vel_.y = 0;  break;
                      case UP:    vel_.x = 0; vel_.y = 0; break;
                      case DOWN:  vel_.x = 0; vel_.y = 0;  break;
                    default: break;
                    }
                }
            });
        }

        @Override protected void wasAdded (Entity entity) {
            super.wasAdded(entity);
            _ship = entity;
        }

        @Override protected boolean isInterested (Entity entity) {
            return type.get(entity.id) == PET;
        }

        protected Vector vel_ = new Vector();
    };

}
