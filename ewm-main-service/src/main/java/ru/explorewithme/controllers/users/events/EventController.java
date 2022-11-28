package ru.explorewithme.controllers.users.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.event.EventService;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.dto.NewEventDto;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.request.dto.ParticipationRequestDto;
import ru.explorewithme.event.dto.UpdateEventRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class EventController {
    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable Long userId, @RequestBody @Valid NewEventDto newEventDto) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
            throw new IdException("It's too late to apologize");
        log.info("User with id={} is creating new event: {}", userId, newEventDto);
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    List<EventShortDto> getEvents(@PathVariable Long userId,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                  @RequestParam(defaultValue = "10") @Positive  Integer size) {
        log.info("Getting events of user with id={}, from={}, size={}", userId, from, size);
        return eventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting event with id={} of user with id={}", eventId, userId);
        return eventService.getEvent(userId, eventId);
    }

    @PatchMapping
    public EventFullDto changeEvent(@PathVariable Long userId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        log.info("Changing existing event to {}", updateEventRequest);
        return eventService.changeEvent(userId, updateEventRequest);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Cancel event with id={} of user with id={}", eventId, userId);
        return eventService.cancelEvent(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId, @PathVariable Long eventId) {
        log.info("Getting requests of event with id={} of user with id={}", eventId, userId);
        return eventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        log.info("Confirming request with id={} of event with id={} of user with id={}", reqId, eventId, userId);
        return eventService.confirmRequest(reqId, eventId, userId);
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        log.info("Rejecting request with id={} of event with id={} of user with id={}", reqId, eventId, userId);
        return eventService.rejectRequest(reqId, eventId, userId);
    }

}
