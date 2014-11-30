package com.pulapirata.core;
import com.pulapirata.core.PetAttributes.ActionState;
import com.pulapirata.core.Pet;
import com.pulapirata.core.PetWorld;
import static com.pulapirata.core.utils.Puts.*;
/**
 * Manages actions.
 * - runs for a specified duration
 * - actually contains action logic
 *   TODO: this will actually be ActionManager....
 */
class Action {
    private ActionState action_;
    public ActionState get() { return action_; }
    private PetAttributes pa_;
    public PetAttributes petAttributes() { return pa_; }

    private int duration_ = 5;   // default duration
    public int duration() { return duration_; }
    public void setDuration(int d) { duration_ = d; }
    private double remaining_ = duration_;

    public void update(int delta) {
        pprint("[action] tick... time remaining " + remaining_ );
        pprint("[action] delta, updaterate, beatscoelhoseg " + delta + " " + Pet.UPDATE_RATE  + " " + PetWorld.beatsCoelhoSegundo_);
        remaining_ -= (double)(delta/Pet.UPDATE_RATE)/ (double)PetWorld.beatsCoelhoSegundo_;
        pprint("[action] tac... time remaining " + remaining_ );
        if (remaining_ <= 0) {
            remaining_ = 0;
            pa_.sAction().updateState(ActionState.DEFAULT);
        }
    }

    public Action(ActionState action, PetAttributes pa) {
        action_ = action;
        pa_ = pa;
    }

    public boolean start() {
        pprint("[action] started " + action_ + " for duration " + duration_ + "s");
        remaining_ = duration_;
        pa_.sAction().updateState(action_);
//        try {
//            java.lang.Thread.sleep(1000);                 //1000 milliseconds is one second.
//        } catch(java.lang.InterruptedException ex) {
//            java.lang.Thread.currentThread().interrupt();
//        }
//        pa_.sAction().updateState(ActionState.DEFAULT);
        return true; // success
    }

    public void start(int duration) {
        setDuration(duration);
        start();
    }

    boolean wasInterrupted() { return false; /* TODO interruption not implemented for now */}

    boolean finished() { return remaining_ == 0; }
}