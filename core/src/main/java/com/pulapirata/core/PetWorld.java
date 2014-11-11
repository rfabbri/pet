package com.pulapirata.core;

import java.util.Iterator;
import java.util.Random;

import react.Signal;
import react.Slot;

import pythagoras.f.FloatMath;
import pythagoras.f.MathUtil;
import pythagoras.f.Point;
import pythagoras.f.Vector;

import playn.core.*;
import playn.core.util.Clock;
import playn.core.util.Callback;
import static playn.core.PlayN.*;

import tripleplay.entity.Component;
import tripleplay.entity.Entity;
import tripleplay.entity.System;
import tripleplay.entity.World;
import tripleplay.util.Randoms;

import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.utils.PetAttributesLoader;
import com.pulapirata.core.utils.TriggerLoader;
import com.pulapirata.core.Triggers;
import com.pulapirata.core.sprites.Spriter;
import com.pulapirata.core.sprites.PetSpriter;
import static com.pulapirata.core.utils.Puts.*;


/**
 * An entity world designed for character animation and interaction among
 * multiple other objects. Call this the <em>extrinsic world</em>. Pet's extrinsic
 * properties are efficiently updated together with the rest of the objects that
 * share those properties - position, velocity, etc.
 */
class PetWorld extends World {
    /*-------------------------------------------------------------------------------*/
    /** Misc variables */
    public  final   GroupLayer layer_;
    public  final   float width_;
    public  final   float height_;
    public  final   Signal<Key> keyDown_ = Signal.create();
    public  final   Signal<Key> keyUp_ = Signal.create();
    private final   Randoms rando_ = Randoms.with(new Random());
    private boolean attributesLoaded_ = false;
    private boolean triggersLoaded_ = false;
    private boolean isPetWired_ = false;
    private Triggers triggers_;
    public Triggers triggers()  { return triggers_; }

    /*-------------------------------------------------------------------------------*/
    /** Types of entities */
    // TODO: use tripleplay.BitVec to supoprt more than 32 entities.
    public static final int PET       = (1 << 0);
    public static final int DROPPING  = (1 << 1);
    public static final int VOMIT     = (1 << 2);
    public static final int DIARRHEA  = (1 << 3);
    public static final int MOSQUITOS = (1 << 4);
    public static final int STINKY_MOSQUITOS = (1 << 5);

    /*-------------------------------------------------------------------------------*/
    /**
     * Components.
     * Components are bags of types, positions, and other properties shared among
     * playable entities in Pet (like the bunny itself and its droppings)
     */
    public final Component.IMask type_ = new Component.IMask(this);
    public final Component.XY pos_  = new Component.XY(this);
    public final Component.XY opos_ = new Component.XY(this);  // old pos for interpolates
    public final Component.XY vel_  = new Component.XY(this);  // pixels/ms
    public final Component.FScalar radius_ = new Component.FScalar(this); // diameter
    public final Component.IScalar expires_ = new Component.IScalar(this);  // expected lifetime
    public final Component.Generic<Spriter> sprite_ = new Component.Generic<Spriter>(this);
    // public final Component.Generic<Layer> spriteLayer_ = new Component.Generic<Layer>(this);
    public final Component.Generic<PetAttributes> pet_ = new Component.Generic<PetAttributes>(this);
    // public final PetAtlas atlas_;  // shared atlas amongst all sprites
    protected PetAttributes mainPet_;  // direct handle on the attributes of the main pet
    public int mainID_ = -1;

    public PetAttributes mainPet() { return mainPet_; }

    /*-------------------------------------------------------------------------------*/
    /** Time data */

    public int beat_ = 0; // total number of updates so far
    // the following is not static so that we can dynamically speedup the game if desired
    /** beats por 1 coelho dia. multiply by UPDATE_RATE to get ms */
//    public int beatsCoelhoDia_ = 864000 /* 864000 = 24h reais para UPDATE_RATE 100ms */;
    public int beatsCoelhoDia_ = 24*10/*s*/*10 /* 1 coelhoHora = 10 human seconds */;
    public double beatsCoelhoHora_ = (double)beatsCoelhoDia_/24.f;
    public double beatsCoelhoSegundo_ = (double)beatsCoelhoDia_/(24.*60.*60.);
    final public int beatsMaxIdade_ = beatsCoelhoDia_*8;
    // TODO: colocar em pet attributes?
    public int idadeCoelhoHoras() { return (int)((double)beat_ / ((double)beatsCoelhoDia_/24.)); }
    public int idadeCoelhoDias() { return beat_ / beatsCoelhoDia_; }


    //final public double tAverageDuracaoPuloAleatorio_ = beatsCoelhoSegundo_/4;
    final public double tAverageDuracaoPuloAleatorio_ = 1; /* 1s @ 100ms update rate*/
    public double tDuracaoPuloAleatorio_ = tAverageDuracaoPuloAleatorio_; // will be randomized
//    final public double tDuracaoPuloAleatorio_ = beatsCoelhoHora_/20;
    public double tPuloAleatorio_ = 0;
//    public float tAverageSpacingPuloAleatorio_ = (float)tDuracaoPuloAleatorio_*9f;
    public float tAverageSpacingPuloAleatorio_ = (float)tDuracaoPuloAleatorio_*40f;
    public double tProximoPuloAleatorio_ = tAverageSpacingPuloAleatorio_;

    /*-------------------------------------------------------------------------------*/
    /** Physics data */

    /** precomputed unit directions */
    final static float [][] directionLut = new float [][] {
        {1f, 0f},
        {0f, 1f},
        {-1f, 0f},
        {0f, -1f},
        {0.7071f, 0.7071f},
        {-0.7071f, 0.7071f},
        {-0.7071f, -0.7071f},
        {0.7071f, -0.7071f}
    };

    /*-------------------------------------------------------------------------------*/
    /** Misc methods */

    public boolean worldLoaded() {
        return attributesLoaded_ && triggersLoaded_;
    }

    public boolean triggersLoaded() {
        return triggersLoaded_;
    }

    public boolean attributesLoaded() {
        return attributesLoaded_;
    }

    @Override public void update (int delta) {
        beat_++;
        super.update(delta);
    }

    @Override public void paint(Clock clock) {
        super.paint(clock);
    }

    public PetWorld (GroupLayer layer, float width, float height) {
        this.layer_  = layer;
        this.width_  = width;
        this.height_ = height;

        attributesLoaded_ = false;
        triggersLoaded_ = false;
        isPetWired_ = false;

        /** load attributes. Only 1 pet attribute set is supported for now
         * (single global profile, single pet) */
        PetAttributesLoader.CreateAttributes(PetAttributes.JSON_PATH, beatsCoelhoHora_,
            new Callback<PetAttributes>() {
                @Override
                public void onSuccess(PetAttributes resource) {
                    mainPet_ = resource;
                    // if (mainID_ != -1)
                    //     pet_.get(mainID_).didChange();
                    attributesLoaded_ = true;
                }

                @Override
                public void onFailure(Throwable err) {
                    PlayN.log().error("Error loading pet attributes: " + err.getMessage());
                }
            });

        /** Load triggers */
        TriggerLoader.CreateTriggers(Triggers.JSON_PATH, beatsCoelhoHora_,
            new Callback<Triggers>() {
                @Override
                public void onSuccess(Triggers resource) {
                    triggers_ = resource;
                    // if (mainID_ != -1)
                    //     pet_.get(mainID_).didChange();
                    triggersLoaded_ = true;
                }

                @Override
                public void onFailure(Throwable err) {
                    PlayN.log().error("Error loading triggers : " + err.getMessage());
                }
            });

        keyboard().setListener(new Keyboard.Adapter() {
            @Override public void onKeyDown (Keyboard.Event event) {
                keyDown_.emit(event.key());
            }
            @Override public void onKeyUp (Keyboard.Event event) {
                keyUp_.emit(event.key());
            }
        });

        createPet(width_/2.f, height_/2.f);
    }

    // FIXME use enum
    protected String typeName (int id) {
        switch (type_.get(id)) {
        case PET: return "pet";
        case DROPPING: return "dropping";
        case VOMIT: return "vomit";
        case DIARRHEA: return "diarrhea";
        case MOSQUITOS: return "mosquitos";
        case STINKY_MOSQUITOS: return "stinky_mosquitos";
        default: return "unknown:" + type_.get(id);
        }
    }

    /*-------------------------------------------------------------------------------*/
    /** Motion Systems */

    /**
     * Basic motion computation.
     * Handles updating entity position based on entity velocity
     */
    public final System logicalMover = new System(this, 0) {
        @Override protected void update (int delta, Entities entities) {
            Point p = innerPos_;
            Vector v = innerVel_;
            for (int i = 0, ll = entities.size(); i < ll; i++) {
                int eid = entities.get(i);
                pos_.get(eid, p); // get our current pos

                clampxy(p, radius_.get(eid));  // keep entity within screen dimensions
                opos_.set(eid, p);  // copy clamped pos to opos
                vel_.get(eid, v).scaleLocal(delta); // turn velocity into delta pos

                dprint("[mover] velocidade scaled " + v);
                pos_.set(eid, p.x + v.x, p.y + v.y); // add velocity (but don't clamp)
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return entity.has(opos_) && entity.has(pos_) && entity.has(vel_);
        }

        protected final Point  innerPos_ = new Point();
        protected final Vector innerVel_ = new Vector();
    };

    private float clampx(float x) {
        return (x > width_) ? (width_) : ((x < 0) ? (0) : x);
    }
    private float clampy(float y) {
        return (y > height_) ? (height_) : ((y < 0) ? (0) : y);
    }

    private void clampxy(Point p, float r) {
        assert r < height_-4 : "radius must be small enough" ;
        assert r < width_-4  : "radius must be small enough" ;

        p.y = (p.y + r + 2 > height_) ? (height_ - r - 2) : ((p.y < r + 2) ? (r + 2) : p.y);
        p.x = (p.x + r + 2 > width_) ? (width_ - r - 2) : ((p.x < r + 2) ? (r + 2) : p.x);
    }

    /** Updates sprite layers to interpolated position of entities on each paint() call */
    public final System spriteMover = new System(this, 0) {
        @Override protected void paint (Clock clock, Entities entities) {
            float alpha = clock.alpha();
            Point op = innerOldPos_, p = innerPos_;
            for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
                int eid = entities.get(ii);
                // interpolate between opos and pos and use that to update the sprite position
                opos_.get(eid, op);
                pos_.get(eid, p);
                // clamp our interpolated position as we may interpolate off the screen
                // pprint(" translation_xy: " +clampx(MathUtil.lerp(op.x, p.x, alpha)) + clampy(MathUtil.lerp(op.y, p.y, alpha)) );
                sprite_.get(eid).layer().setTranslation(clampx(MathUtil.lerp(op.x, p.x, alpha)),
                                                        clampy(MathUtil.lerp(op.y, p.y, alpha)));
            }
        }

        @Override protected void update (int delta, Entities entities) {

            for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
                int eid = entities.get(ii);
                if (beat_ % 2 != 0)  // sprite update rate
                    return;
                sprite_.get(eid).update(delta);
            }
        }

        @Override protected void wasAdded (Entity entity) {
            super.wasAdded(entity);
            layer_.addAt(sprite_.get(entity.id).layer(), pos_.getX(entity.id), pos_.getX(entity.id));
        }

        @Override protected void wasRemoved (Entity entity, int index) {
            super.wasRemoved(entity, index);
            layer_.remove(sprite_.get(entity.id).layer());
        }

        @Override protected boolean isInterested (Entity entity) {
            return entity.has(opos_) && entity.has(pos_) && entity.has(sprite_);
        }

        protected final Point innerOldPos_ = new Point(), innerPos_ = new Point();
    };

    /**
     * Updates pet sprites to reflect inner state.
     */
    public final System spriteLinker = new System(this, 0) {

        public static final float JUMP_WALK_VELOCITY = 0.5f; // 1f;

        @Override protected void update (int delta, Entities entities) {
            for (int i = 0, ll = entities.size(); i < ll; i++) {
                int eid = entities.get(i);
                //System.out.println("eid: " + eid + " mainID_: " + mainID_ + "pet_.get: " + pet_.get(eid));
                if (attributesLoaded_ ) {
                    if (sprite_.get(mainID_).hasLoaded()) {   // TODO in the future: if all sprites have loaded
                        if (!isPetWired_) {
                            finishCreatingPetAfterLoaded();
                            isPetWired_ = true; // should have a vector of attributesLoaded and sprites Loaded
                        }
                        if (type_.get(eid) != PET)
                            continue;

                        // from time to time pet jumps if it is not jumping
                        if (beat_ > tProximoPuloAleatorio_) {
                            dprint ("[pulo] Testando pulando");

                            if (tPuloAleatorio_ == -1) {
                                // start jumping
                                dprint ("[pulo] Setando pulando");
                                mainPet_.setVisibleCondition(PetAttributes.VisibleCondition.PULANDO);
                                tPuloAleatorio_ = 0;
                                int d = rando_.getInt(8); // chose among these directions
                                Vector v = new Vector();
                                v.x = JUMP_WALK_VELOCITY*directionLut[d][0];
                                v.y = JUMP_WALK_VELOCITY*directionLut[d][1];
                                PetSpriter ps = (PetSpriter) sprite_.get(mainID_);
                                if (v.x > 0)
                                    ps.flipLeft();
                                else
                                    ps.flipRight();
                                vel_.set(eid, v);
                                dprint("[pulo] setando velocidade " + v);

                                tDuracaoPuloAleatorio_ = (double)
                                    rando_.getNormal((float)tAverageDuracaoPuloAleatorio_, (float)(0.4*tAverageDuracaoPuloAleatorio_));
                            }
                        }

                        if (tPuloAleatorio_ >= 0) { // jumping
                           if (tPuloAleatorio_ <= tDuracaoPuloAleatorio_)
                                tPuloAleatorio_++;
                            else {
                                // stop jumping
                                tPuloAleatorio_ = -1;
                                Vector v = new Vector();
                                vel_.set(eid, v);
                                // schedule next jump
                                tProximoPuloAleatorio_ =
                                    beat_ + rando_.getInRange(0.8f*tAverageSpacingPuloAleatorio_, 1.3f*tAverageSpacingPuloAleatorio_);
                                assert tDuracaoPuloAleatorio_ < tAverageSpacingPuloAleatorio_*0.8f;
                                assert tProximoPuloAleatorio_ - beat_ > tDuracaoPuloAleatorio_;
                            }
                        } else {
                            PetAttributes.VisibleCondition newvc = pet_.get(eid).determineVisibleCondition();
                        }

//                        dprint("linker: visibleCondition = " + newvc);
                        dprint("     >>>>>>>>>>>>  Current pet state");
                        // pet_.get(eid).print();
                        dprint("     <<<<<<<<<<<<  END Current pet state");
//                        entity(eid).didChange(); // mover will render it.
                        // sprite_.get(eid).update(delta);
                    }
                }
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return type_.get(entity.id) == PET;
        }
    };

    /**
     * Default updates to pet internal attributes and logic.
     * Two future options for this
     * 1) subclass from this PetWorld to have all internal pet updaters outside
     * 2) create a PetAttribute().update()
     */
    public final System petUpdater = new System(this, 0) {
        @Override protected void update (int delta, Entities entities) {
            for (int i = 0, ll = entities.size(); i < ll; i++) {
                int eid = entities.get(i);
                if (isPetWired_) {
                    pet_.get(eid).passiveUpdate(beat_);
                    // other logic if-spaghetti goes here
                }
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return type_.get(entity.id) == PET;
        }
    };


    /** Use keys to control pet. Like in minigames inside this game. Pet should
     * automatically move and do something fun if no control is pressed. NOOP if
     * touchscreen or gamepad are available.
     */
    public final System keyControls = new System(this, 1) {
        public static final float WALK_VELOCITY = 0.05f; // 1f;
        // actually, use just accel
        public static final float ACCEL = 0.01f;

        /* ctor */ {
            keyDown_.connect(new Slot<Key>() {
                @Override public void onEmit (Key key) {
                    pprint("[key] keydown: " + key);
                    PetSpriter ps = (PetSpriter) sprite_.get(mainID_);
                    switch (key) {
                      // TODO colocar estado walk_velocity_ na classe pet?
                      case LEFT:
                        ps.flipRight();
                        velo_.x =  -WALK_VELOCITY; velo_.y = 0;
                        pprint("[key] LEFT press " + velo_.x + ", " + velo_.y);
                        break;
                      case RIGHT:
                        ps.flipLeft();
                        velo_.x  =  WALK_VELOCITY;  velo_.y = 0;
                        pprint("[key] RIGHT press " + velo_.x + ", " + velo_.y);
                      break;
                      case UP:    velo_.x  =  0;  velo_.y = -WALK_VELOCITY;
                        pprint("[key] UP press " + velo_.x + ", " + velo_.y);
                      break;
                      case DOWN:  velo_.x  =  0;  velo_.y = WALK_VELOCITY;
                        pprint("[key] DOWN press " + velo_.x + ", " + velo_.y);
                      break;
                      case SPACE:
                        java.lang.System.out.println("Key SPACE pressed: u mean jump?");
                        mainPet_.print();
                        break;
                      case C:
                        java.lang.System.out.println("Key C pressed: u mean taka dump?"); break;
                      case R:
                        java.lang.System.out.println("Key R pressed: u mean reload attributes file?"); break;
                      default: break;
                    }
                }
            });
            keyUp_.connect(new Slot<Key>() {
                @Override public void onEmit (Key key) {
                    pprint("[key] keyup: " + key);
                    switch (key) {
                      case LEFT:  velo_.x = 0; velo_.y = 0; break;
                      case RIGHT: velo_.x = 0; velo_.y = 0;  break;
                      case UP:    velo_.x = 0; velo_.y = 0; break;
                      case DOWN:  velo_.x = 0; velo_.y = 0;  break;
                    default: break;
                    }
                }
            });
        }

        @Override protected void update (int delta, Entities entities) {
                Vector v = velo_;
                for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
                    int eid = entities.get(ii);
                    //v.x = MathUtil.clamp(v.x + FloatMath.cos(ang)*_accel, -MAX_VEL, MAX_VEL);
                    v.x = velo_.x();
                    v.y = velo_.y();
                    // XXX vel_.set(eid, v);
                }
        }

        @Override protected void wasAdded (Entity entity) {
            super.wasAdded(entity);
            innerPet_ = entity;
        }

        @Override protected boolean isInterested (Entity entity) {
            return type_.get(entity.id) == PET;
        }

        protected Vector velo_ = new Vector();
        protected Entity innerPet_;
    };

    /** Checks for collisions. Like between PET and DROPPING. Models everything as a sphere. */
    public final System collider = new System(this, 1) {
        @Override protected void update (int delta, Entities entities) {
            // simple O(n^2) collision check; no need for anything fancy here
            for (int ii = 0, ll = entities.size(); ii < ll; ii++) {
                int eid1 = entities.get(ii);
                Entity e1 = world.entity(eid1);
                if (e1.isDestroyed()) continue;
                pos_.get(eid1, p1_);
                float r1 = radius_.get(eid1);
                for (int jj = ii+1; jj < ll; jj++) {
                    int eid2 = entities.get(jj);
                    Entity e2 = world.entity(eid2);
                    if (e2.isDestroyed()) continue;
                    pos_.get(eid2, p2_);
                    float r2 = radius_.get(eid2), dr = r2+r1;
                    float dist2 = p1_.distanceSq(p2_);
                    if (dist2 <= dr*dr) {
                        collide(e1, e2);
                        break; // don't collide e1 with any other entities
                    }
                }
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return entity.has(pos_) && entity.has(radius_);
        }

        private void collide (Entity e1, Entity e2) {
            if (attributesLoaded_) {
                switch (type_.get(e1.id) | type_.get(e2.id)) {
                case PET_DROPPING:
                    if (type_.get(e1.id) == PET) {
                        if (pet_.get(e1.id).sAction().getState() == PetAttributes.ActionState.LIMPANDO) {
                            e2.destroy();
                        }
                    } else {
                        if (pet_.get(e2.id).sAction().getState() == PetAttributes.ActionState.LIMPANDO) {
                            e1.destroy();
                        }
                    }
                    break;
                default: break; // nada
                }
            }
        }

        protected static final int PET_DROPPING = PET|DROPPING;
        protected static final int PET_VOMIT = PET|VOMIT;
        protected static final int PET_DIARRHEA = PET|DIARRHEA;

        protected final Point p1_ = new Point(), p2_ = new Point();
    };

    /*-------------------------------------------------------------------------------*/
    /** Game logic that generalizes among many entities */

    // expires things with limited lifespan
    public final System expirer = new System(this, 0) {
        @Override protected void update (int delta, Entities entities) {
            int now = beat_;
            for (int i = 0, ll = entities.size(); i < ll; i++) {
                int eid = entities.get(i);
                if (expires_.get(eid) <= now) world.entity(eid).destroy();
            }
        }

        @Override protected boolean isInterested (Entity entity) {
            return entity.has(expires_);
        }
    };

    /*-------------------------------------------------------------------------------*/
    /** Entity creation */

    protected Entity createPet (float x, float y) {
        Entity pet = create(true);
        pet.add(type_, pet_, sprite_, opos_, pos_, vel_, radius_, expires_);

        int id = pet.id;
        type_.set(id, PET);
        opos_.set(id, x, y);
        pos_.set(id, x, y);
        vel_.set(id, 0, 0);
        expires_.set(id, beatsMaxIdade_);
        mainID_ = id;

        // read imgLayer / sprite loader
        PetSpriter ps = new PetSpriter();   // the spriteLinker system links it to layer_
                                            // note: if many pets were available,
                                            // we'd alocate them contiguously
        sprite_.set(id, ps);      // also queues sprite to be added by other systems on wasAdded()

        return pet;
    }

    protected void finishCreatingPetAfterLoaded() {
        PetSpriter ps = (PetSpriter) sprite_.get(mainID_);
        // ps.layer().setWidth(-ps.layer().width());
        // XXX mainPet_.vis().connect(ps.slot());    // links sprite to animation
        ps.set(PetAttributes.VisibleCondition.TRISTE);
        pet_.set(mainID_, mainPet_); // only 1 pet for now, but more are easily supported
        radius_.set(mainID_, ps.boundingRadius());
        // spriteLayer_.set(id, layer_);
    }

    public void reset() {
        Iterator<Entity> iter = entities();
        while (iter.hasNext()) iter.next().destroy();
        createPet(width_/2.f, height_/2.f);
    }
}
