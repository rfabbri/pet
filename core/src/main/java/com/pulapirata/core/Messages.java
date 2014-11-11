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
 *            - mode 1) round robin: circles through all available messages in
 *            the list, then increments the current message pointer from time to
 *            time. Messages are displayed in order of priority, with negative
 *            priority being usually reserved for default messages
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

    /** Reference to a text label to show the contents. */
    private Label l_;

    /** Maps {@link PetAttributes.State} states to message strings.
     * TODO construct from json */
    public EnumMap<State, String> ms_ = new EnumMap<State, String>(State.class());

    /** Sets a text label to show the contents. */
    public void setLabel(Label l) {
        l_ = l;
    }

    /** Sets the current message to be shown when in round-robin mode. */
    public void setCurrentMessage(Message m) {
        disconnectLabel();
        c_ = m;
        connectLabel();
    }

    /** Connects the UI label to the current text */
    private void connectLabel() {
        c_.text_.connect(l_.text.slot());
        connectToPrint();
    }

    /** Disconnects the UI label from the current text */
    private void disconnectLabel() {
        c_.text_.disconnect(l_.text.slot());
        disconnectToPrint();
    }

    /** Slot to print emitted texts from a message.
     *  This watches when a message changes state,
     *  for debugging.
     */
    Slot<string> printSlot = new Slot<String>() {
                    @Override public void onEmit (String txt) {
                        pprint("[message] current message received at slot: " + txt);
                    }
                }

    /**
     * Connects the current message to stdout
     */
    private void connectToPrint() {
        c_.text_.connect(printSlot);
    }

    /**
     * Disconnects the current message to stdout
     */
    private void disconnectToPrint() {
        c_.text_.disconnect(printSlot);
    }

    /**
     * Consructor.
     * To set this class up, you typically call
     * - constructor
     * - setLabel(l) if you want to show on a UI label
     * - on your update loop, call updateMessages();
     *
     */
    public Messages() {
        setCurrentMessage(emptyMessage_);
        ci_ = messages_.listIterator();

        /** Add messages for the states */
        for each sAttribute s
            messages_.add(new MessageState(ms_, s));

        // at this point this either stays at the default emptymessage,
        // or hits some non-empty message in the initial list
        nextMessage();
    }

    /**
     * Updates message queue and current message or contents.
     * Alias to nextMessage() for roundrobin mode.
     */
    public void updateMessages() {
        nextMessage();
    }

    /** add a message to the list to be displayed */
    public void add(Message m) {
        messages_.add(m);
        // iterators must be recomputed.
        ci_ = messages_.listIterator(messages_.indexOf(c_));
    }

    /** removes a message to the list to be displayed. checks if equals current */
    public void remove(Message m) {
        m.remove();
        if (c_ == m) {
            nextMessage();
            // If really want to remove from list,
            // must recompute iterators
            // messages_.remove(m); // invalidates
        }
    }

    public String currentMessage() {
        return c_.message();
    }

    /** sets the current message to the next one in the list */
    public String nextMessage() {
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
                    setCurrentMessage(aux);
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
            setCurrentMessage(defaultMessage);
            ci_ = defaultIt;
        }
        print();
    }

    /**
     * Prints messages for debugging.
     */
    void print() {
        pprint("[message] current message: " + c_);
        pprint("[message]         queue num messages: " + messages_.size());
        pprint("[message]         queue contents: " );
        for(Message m: messages_) {
            pprint("[message]           " + m);
        }
        pprint("[message]  -------- End queue" );
    }
}
