package com.pulapirata.core;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A class to manage construction and display of multiple messages to the user.
 * This class has the following functionality
 * - store a bunch of {@link MessageState} and {@link Messages}
 * - generate a list of messages from the internal messages
 * - allow addition of new messages other than MessageStates
 * - update()
 *      - regenerates the message list each time
 *      - regenerates the display according to the chosen display mode
 *            - mode 1) circles through all available messages in the list, then
 *            increments the current message pointer from time to time
 *            - mode 2) display all current messages on a rolling list which the user
 *            can go through by swiping or using the mouse.
 */
public class Messages {
    /** pointer to current message */
    private Message c_;
    /** list of messages to be displayed */
    List<Message> messages_ = new ArrayList<Message>();
    /** iterator to current message in list */
    private ListIterator<Message> ci_;

    /** Maps {@link PetAttributes.State} states to message strings.
     * TODO construct from json */
    public EnumMap<State, String> ms_ = new EnumMap<State, String>(State.class());

    connectToLabel(Label l) {
        c_.text_.connect(l.text.slot());
    }

    connectToPrint() {
        c_.text_.connect(new Slot<String>() {
                    @Override public void onEmit (String txt) {
                        pprint("[message] current message: " + txt);
                    }
                });
    }

    /**
     * Consructor
     */
    Messages() {
        /** Add messages for the states */
        for each sAttribute s
            messages_.add(new MessageState(ms_, s));

        updateMessageList();
    }

    /** add a message to the list to be displayed */
    void add(Message m);
    /** removes a message to the list to be displayed. checks if equals current */
    void remove(Message m);

    String currentMessage() {
    }

    String nextMessage() {
    }

    updateMessageList() {
        if (c_ == null) {
            // - go through the list
            // - if string not empty, set current
        } else {

        }
    }
}
