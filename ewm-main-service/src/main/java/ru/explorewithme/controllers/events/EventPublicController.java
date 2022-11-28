package ru.explorewithme.controllers.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.controllers.events.dto.GetEventPublicRequest;
import ru.explorewithme.controllers.events.model.EndPoint;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.EventService;
import ru.explorewithme.exception.IdException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/events")
public class EventPublicController {
    private EventService eventService;

    private StatPostClient statPostClient;

    public EventPublicController(EventService eventService, StatPostClient statPostClient) {
        this.eventService = eventService;
        this.statPostClient = statPostClient;
    }

    @GetMapping
    public List<EventShortDto> getPublicEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) HashSet<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(required = false) String sort,
                                               @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size,
                                               HttpServletRequest request) {
        log.info("Getting public events with text={}, categories={}, paid={}, rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        log.info("Getted uri={}", request.getRequestURL() + "?" + request.getQueryString());

        return eventService.getPublicEvents(
                GetEventPublicRequest.of(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort),
                from, size, request);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getPublicEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("Getting public event with id={}", eventId);

        return eventService.getPublicEvent(eventId, request);
    }

    String toStringFromDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String s = date.format(formatter);
        return s;
    }
}
