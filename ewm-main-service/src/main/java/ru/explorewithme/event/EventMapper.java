package ru.explorewithme.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.explorewithme.category.CategoryMapper;
import ru.explorewithme.category.model.Category;
import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.client.ViewStats;
import ru.explorewithme.request.RequestRepository;
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
    //@Autowired
    //private static RequestRepository requestRepository;
    //@Autowired
    //private static StatGetClient statGetClient;
    //private static final String API_PREFIX = "/events/";
    //@Value("${main-server.url}")
    //private static String url;

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
        EventFullDto eventFullDto = EventFullDto.builder()
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
                .requestModeration(event.getRequestModeration())
                .state(event.getState().toString())
                .title(event.getTitle())
                .views(hits)
                .build();
        if (event.getPublishedOn() != null) eventFullDto.setPublishedOn(fromDateToString(event.getPublishedOn()));
        return eventFullDto;
    }

    public static EventShortDto toEventShortDto(Event event, Long confirmedRequests, Long hits) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .eventDate(fromDateToString(event.getEventDate()))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(hits)
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

    public static List<EventShortDto> mapToEventShortDto(Iterable<Event> events,
                                                         RequestRepository requestRepository,
                                                         StatGetClient statGetClient,
                                                         String url) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            Long confirmedRequests = requestRepository.countConfirmedRequests(event.getId());
            //Получение статистики
            LocalDateTime start = LocalDateTime.now().minusYears(1);
            LocalDateTime end = LocalDateTime.now().plusYears(1);
            List<ViewStats> viewStats = statGetClient.getEndPoint(start, end, List.of(url + event.getId()));
            Long hits = viewStats.get(0).getHits();
            dtos.add(toEventShortDto(event, confirmedRequests, hits));
        }
        return dtos;
    }

    public static List<EventShortDto> mapToEventShortDto(List<Event> events,
                                                         RequestRepository requestRepository,
                                                         StatGetClient statGetClient,
                                                         String url) {
        List<EventShortDto> dtos = new ArrayList<>();
        for (Event event : events) {
            Long confirmedRequests = requestRepository.countConfirmedRequests(event.getId());
            //Получение статистики
            LocalDateTime start = LocalDateTime.now().minusYears(1);
            LocalDateTime end = LocalDateTime.now().plusYears(1);
            List<ViewStats> viewStats = statGetClient.getEndPoint(start, end, List.of(url + event.getId()));
            Long hits = viewStats.get(0).getHits();
            dtos.add(toEventShortDto(event, confirmedRequests, hits));
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
