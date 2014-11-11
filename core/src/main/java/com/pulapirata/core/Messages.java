package com.pulapirata.core;
import java.util.List;
import java.util.LinkedList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.ListIterator;
import react.Slot;
import tripleplay.ui.Label;
import com.pulapirata.core.Message;
import com.pulapirata.core.PetAttributes;
import com.pulapirata.core.PetAttributes.State;
import com.pulapirata.core.PetAttributes.AttributeID;
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
    public static String JSON_PATH = "pet/jsons/messages.json";

    /** message displayed when theres no messages.
     * Usually for debugging - should display some other info.
     * TODO ask prestoppc what happens when theres no msgs. */
    private Message emptyMessage_ = new Message("Sem avisos");

    /** pointer to current message */
    private Message c_ = emptyMessage_;
    /** list of messages to be displayed */
    private List<Message> messages_
        = new LinkedList<Message>();
    /** iterator to current message in list */
    private ListIterator<Message> ci_;

    /** Reference to a text label to show the contents. */
    private Label l_;

    /** total number of updates so far */
    public int beat_ = 0;

    /** Maps {@link PetAttributes.State} states to message strings.
     * Constructed from json. */
    public EnumMap<State, String> ms_ = new EnumMap<State, String>(State.class);

    /** Sets a text label to show the contents. */
    public void setLabel(Label l) {
        l_ = l;
    }

    /** is it wired to a label? */
    public boolean isLabelSet() {
        return l_ != null;
    }

    /** Sets the current message to be shown when in round-robin mode. */
    public void setCurrentMessage(Message m) {
        disconnectLabel();
        c_ = m;
        connectLabel();
    }

    /** Connects the UI label to the current text */
    private void connectLabel() {
        connectToPrint();
        if (isLabelSet()) {
            c_.text_.connect(l_.text.slot());
        }
        c_.text_.updateForce(c_.text_.get());   // notify all watchers
    }

    /** Disconnects the UI label from the current text */
    private void disconnectLabel() {
        disconnectToPrint();
        if (isLabelSet())
            c_.text_.disconnect(l_.text.slot());
    }

    /** Slot to print emitted texts from a message.
     *  This watches when a message changes state,
     *  for debugging.
     */
    private Slot<String> printSlot = new Slot<String>() {
                    @Override public void onEmit (String txt) {
                        pprint("[message] current message received at slot: " + txt);
                    }
                };

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
        emptyMessage_.setPriority(-1);
        setCurrentMessage(emptyMessage_);
        ci_ = messages_.listIterator();
    }

    /**
     * To be called once ms_ is filled up by MessageLoader
     */
    public void init(PetAttributes a) {
        messages_.add(emptyMessage_);
        // Add messages for the states
        for (AttributeID id : a.sAtt_.keySet())
            messages_.add(new MessageState(ms_, a.sAtt(id)));
        ci_ = messages_.listIterator();
        setCurrentMessage(ci_.next());

        update();
        // at this point round-robin mode either stays at the default
        // emptymessage, or hits some non-empty message in the initial list
    }

    /**
     * Updates message queue and current message or contents.
     * Alias to nextMessage() for roundrobin mode.
     */
    public void update() {
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
        return c_.get();
    }

    /** sets the current message to the next one in the list */
    public void nextMessage() {
        // for each element in the list after current
        // if it is non-null
        //  set current
        // if all are null
        //  current is Empty.


        assert !messages_.isEmpty() : "messages completely empty should never happen for now";

        if (messages_.isEmpty()) {
            // handle it if it happens
        }

        Message defaultMessage = null;
        ListIterator<Message> defaultIt = null;

        // circular list not empty.
        while (true) {
            pprint("[mod] loop. ");
            if (!ci_.hasNext()) { // circular
                pprint("[mod] it doesnt have next");
                ci_ = messages_.listIterator();
            }
            Message curr = ci_.next();
            assert curr != null;
            if (c_ == curr) {  // went around the list and all else is empty;
                pprint("[mod] went around the list and all else is empty");
                break;
            }
            if (!curr.isEmpty()) {
                pprint("[mod] considering nonempty " + curr);
                if (curr.priority() >= 0) {
                    pprint("[mod] set current to " + curr);
                    setCurrentMessage(curr);
                    break;
                } else {
                    // default messages are with minus 1.
                    defaultMessage = curr;
                    defaultIt = ci_;
                }
            }
        }

        if (c_.isEmpty()) {
            pprint("[mod] set to default");
            assert defaultMessage != null;
            setCurrentMessage(defaultMessage);
            ci_ = defaultIt;
        }
        print();
    }

    /**
     * Prints messages for debugging.
     */
    public void print() {
        pprint("[message] current message: " + c_);
        pprint("[message]         queue num messages: " + messages_.size());
        pprint("[message]         queue contents: " );
        for (Message m: messages_) {
            pprint("[message]           " + m);
        }
        pprint("[message]  -------- End queue" );
        pprint("[message]  map: " + ms_);
    }
}
