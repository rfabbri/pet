package com.pulapirata.core;
import java.util.EnumMap;
import java.util.Map;
import react.Value;
import react.Functions;
import react.Function;
import com.pulapirata.core.PetAttributeState;
import com.pulapirata.core.PetAttributes.State;
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
        as.map( forMapInt(stateMessages, "[messageState] error: no message for state") ).connect(text_.slot());
        as.updateForce(as.get());
    }

    /**
     * Returns a function which performs a map lookup with a default value. The function created by
     * this method returns defaultValue for all inputs that do not belong to the map's key set.
     */
    public static Function<Integer, String> forMapInt (final EnumMap<State, String> map, final String defaultValue)
    {
        return new Function<Integer, String>() {
            public String apply (Integer id) {
                String value = map.get(State.values()[id]);
                return (value != null || map.containsKey(State.values()[id])) ? value : defaultValue;
            }
        };
    }
}
