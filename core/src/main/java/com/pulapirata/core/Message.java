package com.pulapirata.core;
import react.Value;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A Class to store messages to be displayed in the game.
 * It is a reactive string that can wire itself to certain events of the game,
 * such as changing PetAttributeState
 */
public class Message {
    /** reactive string that can send/receive signals */
    public Value<String> text_ = Value.create(null);
    private int priority_;

    public Message() {}
    /** Creates a message with the supplied text */
    public Message(String m) { text_.update(m); }
    public String get() { return text_.get(); }
    public void setMessage(String m) { text_.update(m); }
    public boolean isEmpty() { return text_.get().equals(""); }
    public int priority() { return priority_; }
    public void setPriority(int p) { priority_ = p; }
    public void remove() { setMessage(""); }
    @Override public String toString() { return get(); }
}
