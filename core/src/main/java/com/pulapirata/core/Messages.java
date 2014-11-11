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
    /** message displayed when theres no messages.
     * Usually for debugging - should display some other info.
     * TODO ask prestoppc what happens when theres no msgs. */
    private Message emptyMessage_ = new Message("Sem avisos");

    /** pointer to current message */
    private Message c_ = emptyMessage_;
    /** list of messages to be displayed */
    List<Message> messages_ = new LinkedList<Message>();
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
        connectToPrint();

        c_ = emptyMessage_;
        ci_ = messages_.listIterator();

        /** Add messages for the states */
        for each sAttribute s
            messages_.add(new MessageState(ms_, s));

        // at this point this either stays at the default emptymessage,
        // or hits some non-empty message in the initial list
        nextMessage();
    }

    /** add a message to the list to be displayed */
    void add(Message m) {
        messages_.add(m);
        // iterators must be recomputed.
        ci_ = messages_.listIterator(messages_.indexOf(c_));
    }

    /** removes a message to the list to be displayed. checks if equals current */
    void remove(Message m) {
        m.remove();
        if (c_ == m) {
            nextMessage();
            // If really want to remove from list,
            // must recompute iterators
            // messages_.remove(m); // invalidates
        }
    }

    String currentMessage() {
        return c_.message();
    }

    /** sets the current message to the next one in the list */
    String nextMessage() {
        // for each element in the list after current
        // if it is non-null
        //  set current
        // if all are null
        //  current is Empty.


        assert !messages_.isempty() : "messages completely empty should never happen for now";

        if (messages_.isEmpty()) {
            // handle it if it happens
        }

        Message defaultMessage;
        private ListIterator<Message> ci_;

        // circular list not empty.
        while (true) {
            if (!ci_.hasNext()) // circular
                ci_ = messages_.listIterator();
            Message curr = ci_.next();
            assert curr != null;
            if (c_ == curr)  // went around the list and all else is empty;
                break;
            if (!curr.isEmpty()) {
                if (curr.message().priority() > 0) {
                    c_ = aux;
                    break;
                } else {
                    // default messages are with minus 1.
                    defaultMessage = aux;
                    defaultIt = ci_;
                }
            }
        }

        if (c_.isEmpty()) {
            assert defaultMessage != null;
            c_ = defaultMessage;
            ci_ = defaultIt;
        }
    }
}
