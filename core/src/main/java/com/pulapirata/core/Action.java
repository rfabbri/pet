package com.pulapirata.core;
import com.pulapirata.core.PetAttributes.ActionState;
import static com.pulapirata.core.utils.Puts.*;
/**
 * Manages actions.
 * - runs for a specified duration
 * - actually contains action logic
 *   TODO: this will actually be ActionManager....
 */
class Action {
    private int duration_ = 5;   // default duration
    public int duration() { return duration_; }
    public void setDuration(int d) { duration_ = d; }
    private ActionState action_;
    public ActionState get() { return action_; }

    public Action(ActionState a) {
        action_ = a;
    }

    public boolean start() {
        dprint("[action] started " + action_ + " for duration " + duration_ + "s");
        try {
            java.lang.Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(java.lang.InterruptedException ex) {
            java.lang.Thread.currentThread().interrupt();
        }
        return true; // success
    }


    public void start(int duration) {
        setDuration(duration);
        start();
    }

    boolean wasInterrupted() { return false; /* TODO interruption not implemented for now */}
}
