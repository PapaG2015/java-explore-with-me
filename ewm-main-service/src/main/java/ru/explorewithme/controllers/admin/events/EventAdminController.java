package ru.explorewithme.controllers.admin.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.controllers.admin.events.dto.AdminUpdateEventRequest;
import ru.explorewithme.controllers.admin.events.dto.GetEventAdminRequest;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.event.EventService;
import ru.explorewithme.event.model.EventState;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping(path = "/admin/events")
public class EventAdminController {
    private EventService eventService;

    public EventAdminController(EventService eventService) {
        this.eventService = eventService;
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        log.info("Publushing event with id={}", eventId);
        return eventService.publishEvent(eventId);
    }

    @GetMapping
    public List<EventFullDto> getAdminEvents(@RequestParam(required = false) Set<Long> users,
                                             @RequestParam(required = false) Set<String> states,
                                             @RequestParam(required = false) Set<Long> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                             @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Getting events by admin with users={}, states={}, categories={}, rangeStart={}, rangeEnd={}",
                users, states, categories, rangeStart, rangeEnd);
        Set<EventState> eventStates = EventState.from(states);

        return eventService.getAdminEvents(GetEventAdminRequest.of(users, eventStates, categories, rangeStart, rangeEnd), from, size);
    }

    @PutMapping("/{eventId}")
    public EventFullDto putAdminEvent(@PathVariable Long eventId, @RequestBody AdminUpdateEventRequest event) {
        log.info("Putting new event with id={} by admin: {}", eventId, event);
        return eventService.putAdminEvent(eventId, event);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        log.info("Rejecting event with id={}", eventId);
        return eventService.rejectEvent(eventId);
    }
}
