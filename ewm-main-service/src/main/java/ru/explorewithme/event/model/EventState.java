package ru.explorewithme.event.model;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public enum EventState {
    PENDING, //рассматриваемый
    PUBLISHED,
    CANCELED;

    public static Optional<EventState> from(String stringState) {
        for (EventState state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

    public static Set<EventState> from(Set<String> states) {
        Set<EventState> eventStates = new HashSet<>();
        for (String s : states) {
            EventState state =
                    EventState.from(s).orElseThrow(() -> new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS"));
            eventStates.add(state);
        }
        return eventStates;
    }
}
