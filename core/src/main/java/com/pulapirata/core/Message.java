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
    public Value<String> text_;
    private int priority_;

    /** Cretes a message with the supplied text */
    public Message(String m) { text_.update(m); }
    public String message() { return text_.get() }
    public void setMessage(String message_) { text_.set(m); }
}
