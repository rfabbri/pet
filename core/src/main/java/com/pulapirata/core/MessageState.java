package com.pulapirata.core;
import react.Value;
import static com.pulapirata.core.utils.Puts.*;

/**
 * A message that reacts to PetAttributestates.
 * The message strings reflect a specific PetAttributeState.
 */
class MessageState extends Message {
    /**
     * Construct with a text message for each state in {@link PetAttributeState} {@param as}.
     * There must be one state message string per state. When there is no
     * message for a given state, a null string must be provided.
     */
    MessageState(EnumMap<State, String> stateMessages, PetAttributeState as) {
        assert as.isInitialized();
        as.map( Functions.forMap(stateMessages, "") ).connect(text_.slot());
        as.updateForce(as.get());
    }
}
