package ru.explorewithme.event;

import ru.explorewithme.category.CategoryMapper;
import ru.explorewithme.category.model.Category;
import ru.explorewithme.user.model.User;
import ru.explorewithme.user.UserMapper;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.dto.NewEventDto;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.event.model.Location;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    public static Event toEvent(User user, NewEventDto newEventDto, Category category) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .initiator(user)
                .locationLat(newEventDto.getLocation().getLat())
                .locationLon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long hits) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(fromDateToString(event.getCreatedOn()))
                .description(event.getDescription())
                .eventDate(fromDateToString(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(new Location(event.getLocationLat(), event.getLocationLon()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(fromDateToString(LocalDateTime.now()))
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(hits)
                .build();
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(fromDateToString(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(1000000L)
                .build();
    }

    public static LocalDateTime fromStringToLocalDateTime(String s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(s, formatter);
    }

    public static String fromDateToString(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String s = date.format(formatter);
        return s;
    }

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(toEventShortDto(event, 4L));
        }
        return dtos;
    }
/*
    public static List<EventFullDto> mapToEventFullDto(Iterable<Event> events) {
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event event : events) {
            dtos.add(toEventFullDto(event));
        }
        return dtos;
    }*/

    /*public static Event toEvent(Long eventId, AdminUpdateEventRequest updateEvent, Category category) {
        return Event.builder()
                .id(eventId)
                .annotation(updateEvent.getAnnotation())
                .category(category)
                .description(updateEvent.getDescription())
                .eventDate(updateEvent.getEventDate())
                .locationLat(updateEvent.getLocation().getLat())
                .locationLon(updateEvent.getLocation().getLon())
                .paid(updateEvent.getPaid())
                .participantLimit(updateEvent.getParticipantLimit())
                .requestModeration(updateEvent.getRequestModeration())
                .title(updateEvent.getTitle())
                .build();
    }*/


    public static List<Event> toList(Iterable<Event> ilist) {
        List<Event> events = new ArrayList<>();

        for (Event e : ilist) {
            events.add(e);
        }

        return events;
    }
}
