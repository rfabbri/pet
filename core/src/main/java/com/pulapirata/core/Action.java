/**
 * Manages actions.
 * - runs for a specified duration
 * - actually contains action logic
 *   TODO: this will actually be ActionManager....
 */
class Action {

    int duration_ = 5;   // default duration
    int duration() { return duration_; }
    setDuration(int d) { duration_ = d; }
    ActionState action_;
    int get() { return action_; }

    Action(ActionState a) {
        action_ = a;
    }

    void start() {
        dprint("[action] started " + a + " for duration " + duration + "s");
        wait(5s);
    }
    void start(int duration) {
        setDuration(duration);
        start();
    }

    boolean wasInterrupted() { return false; /* TODO interruption not implemented for now */}
}
