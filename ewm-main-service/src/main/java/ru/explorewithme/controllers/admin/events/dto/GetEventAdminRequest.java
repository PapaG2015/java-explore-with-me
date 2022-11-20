package ru.explorewithme.controllers.admin.events.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.explorewithme.event.model.EventState;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class GetEventAdminRequest {
    private Set<Long> users;
    private Set<EventState> states;
    private Set<Long> Categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;

    public static GetEventAdminRequest of(Set<Long> users,
                                          Set<EventState> states,
                                          Set<Long> Categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd) {
        GetEventAdminRequest request = new GetEventAdminRequest();
        request.setUsers(users);
        request.setStates(states);
        request.setCategories(Categories);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);

        return request;
    }
}
