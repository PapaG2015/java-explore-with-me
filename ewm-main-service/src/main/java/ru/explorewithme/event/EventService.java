package ru.explorewithme.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.category.CategoryRepository;
import ru.explorewithme.client.StatGetClient;
import ru.explorewithme.client.ViewStats;
import ru.explorewithme.controllers.admin.events.dto.AdminUpdateEventRequest;
import ru.explorewithme.controllers.admin.events.dto.GetEventAdminRequest;
import ru.explorewithme.category.model.Category;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.event.dto.EventShortDto;
import ru.explorewithme.event.dto.NewEventDto;
import ru.explorewithme.event.model.EventState;

import ru.explorewithme.event.model.QEvent;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.request.dto.ParticipationRequestDto;
import ru.explorewithme.event.dto.UpdateEventRequest;
import ru.explorewithme.request.model.RequestState;
import ru.explorewithme.user.model.User;
import ru.explorewithme.user.UserRepository;
import ru.explorewithme.controllers.events.dto.GetEventPublicRequest;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.model.Request;
import ru.explorewithme.request.RequestMapper;
import ru.explorewithme.request.RequestRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EventService {
    private EventRepository eventRepository;
    private UserRepository userRepository;
    private CategoryRepository categoryRepository;
    private RequestRepository requestRepository;
    private IdService idService;
    private StatGetClient statGetClient;

    public EventService(EventRepository eventRepository,
                        UserRepository userRepository,
                        CategoryRepository categoryRepository,
                        IdService idService,
                        RequestRepository requestRepository,
                        StatGetClient statGetClient) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.idService = idService;
        this.requestRepository = requestRepository;
        this.statGetClient = statGetClient;
    }

    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User user = idService.getUserById(userId);
        Category category = idService.getCategoryById(newEventDto.getCategory());

        Event event = EventMapper.toEvent(user, newEventDto, category);
        eventRepository.save(event);

        //Получение статистики
        List<String> uris = List.of("");
        ViewStats response = (ViewStats)(statGetClient.getEndPoint(LocalDateTime.now().minusYears(1),
                LocalDateTime.now().plusYears(1), uris).getBody());
        Long hits = response.getHits();
        //
        Long confirmRequest = requestRepository.countConfirmedRequests(event.getId());
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, hits);
        log.info("Added event: {}", eventFullDto);
        return eventFullDto;
    }

    public List<EventShortDto> getEvents(Long userId, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<EventShortDto> events = eventRepository.findByInitiator_Id(userId, pageable)
                .stream().map(event -> {
                    Long confirmedRequests = requestRepository.countConfirmedRequests(event.getId());
                    return EventMapper.toEventShortDto(event, confirmedRequests);
                }).collect(Collectors.toList());
        log.info("Getted events: {}", events);
        return events;
    }

    public EventFullDto changeEvent(Long userId, UpdateEventRequest updateEventRequest) {
        Long eventId = updateEventRequest.getEventId();
        Event event = idService.getEventById(eventId);

        if (event.getState() == EventState.PUBLISHED)
            throw new IdException("Event with id=" + eventId + " has been published");

        if (event.getEventDate() != null) {
            if (updateEventRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2)))
                throw new IdException("It's too late to apologize");
        }

        if (event.getState() == EventState.CANCELED) event.setState(EventState.PENDING);

        if (updateEventRequest.getAnnotation() != null) event.setAnnotation(updateEventRequest.getAnnotation());

        if (updateEventRequest.getCategory() != null)
            event.setCategory(idService.getCategoryById(updateEventRequest.getCategory()));

        if (updateEventRequest.getDescription() != null)
            event.setDescription(updateEventRequest.getDescription());

        if (event.getEventDate() != null)
            event.setEventDate(updateEventRequest.getEventDate());

        if (event.getPaid() != null)
            event.setPaid(updateEventRequest.getPaid());

        if (event.getParticipantLimit() != null)
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());

        if (event.getTitle() != null)
            event.setTitle(updateEventRequest.getTitle());

        eventRepository.save(event);
        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);
        return EventMapper.toEventFullDto(event, confirmRequest, 9L);
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findByInitiator_IdAndId(userId, eventId);
        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);

        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, 9L);
        log.info("Getted event: {}", eventFullDto);
        return eventFullDto;
    }

    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = idService.getEventById(eventId);
        if (event.getState() != EventState.PENDING)
            throw new IdException("Event with id=" + event + " can't be canceled");

        event.setState(EventState.CANCELED);
        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, 9L);
        log.info("Canceled event: {}", eventFullDto);
        return eventFullDto;
    }

    public EventFullDto publishEvent(Long eventId) {
        Event event = idService.getEventById(eventId);
        if (event.getState() != EventState.PENDING)
            throw new IdException("Event with id=" + eventId + "must have status PENGING");

        if (event.getEventDate().isAfter(LocalDateTime.now().plusHours(1)))
            throw new IdException("It's too late");

        event.setState(EventState.PUBLISHED);
        eventRepository.save(event);
        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, 9L);
        log.info("Published event: {}", eventFullDto);
        return eventFullDto;
    }

    public EventFullDto rejectEvent(Long eventId) {
        Event event = idService.getEventById(eventId);
        if (event.getState() == EventState.PUBLISHED)
            throw new IdException("Event with id=" + eventId + " can't be reject, because it's published");

        event.setState(EventState.CANCELED);
        eventRepository.save(event);
        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, 9L);
        log.info("Rejected event: {}", eventFullDto);
        return eventFullDto;
    }

    public List<EventShortDto> getPublicEvents(GetEventPublicRequest req,
                                               Integer from,
                                               Integer size) {

        //QEvent event = QEvent.event;
        QEvent event = QEvent.event;

        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(event.description.containsIgnoreCase(req.getText())
                .or(event.annotation.containsIgnoreCase(req.getText())));
        conditions.add(event.category.id.in(req.getCategories()));
        conditions.add(event.paid.eq(req.getPaid()));
        if (req.getRangeStart() == null || req.getRangeEnd() == null) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        } else conditions.add(event.eventDate.between(req.getRangeStart(), req.getRangeEnd()));
        conditions.add(event.state.eq(EventState.PUBLISHED));

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Iterable<Event> events;
        if (req.getSort() == "EVENT_DATE") {
            events = eventRepository.findAll(finalCondition, Sort.by(Sort.Direction.ASC, "eventDate"));
        } else {
            events = null;
        }

        List<Event> eventList = EventMapper.toList(events);

        if (req.getOnlyAvailable() == true) {
            eventList = eventList.stream().filter(e -> {
                Long confirmedReq = e.getRequests().stream().filter(request -> request.getStatus() == RequestState.CONFIRMED)
                        .count();
                return confirmedReq < e.getParticipantLimit();
            }).collect(Collectors.toList());
        }

        eventList.stream().skip(from).limit(size);

        List<EventShortDto> eventShortDtos = EventMapper.mapToEventShortDto(events);

        return eventShortDtos;
    }

    public List<EventFullDto> getAdminEvents(GetEventAdminRequest req, Integer from, Integer size) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(event.initiator.id.in(req.getUsers()));
        conditions.add(event.state.in(req.getStates()));
        conditions.add(event.category.id.in(req.getCategories()));
        conditions.add(event.eventDate.between(req.getRangeStart(), req.getRangeEnd()));

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        Pageable pageable = PageRequest.of(from / size, size);
        Iterable<Event> events = eventRepository.findAll(finalCondition, pageable);
        List<EventFullDto> dtos = new ArrayList<>();
        for (Event e : events) {
            Long confirmedRequests = requestRepository.countConfirmedRequests(e.getId());
            dtos.add(EventMapper.toEventFullDto(e, confirmedRequests, 9L));
        }
        log.info("Getted events by admin: {}", dtos);
        return dtos;

    }

    public EventFullDto putAdminEvent(Long eventId, AdminUpdateEventRequest updateEvent) {
        Event event = idService.getEventById(eventId);
        Category category = idService.getCategoryById(updateEvent.getCategory());

        if (updateEvent.getAnnotation() != null) event.setAnnotation(updateEvent.getAnnotation());
        if (updateEvent.getCategory() != null) event.setCategory(category);
        if (updateEvent.getDescription() != null) event.setDescription(updateEvent.getDescription());
        if (updateEvent.getEventDate() != null) event.setEventDate(updateEvent.getEventDate());
        if (updateEvent.getLocation() != null) {
            event.setLocationLat(updateEvent.getLocation().getLat());
            event.setLocationLon(updateEvent.getLocation().getLon());
        }
        if (updateEvent.getPaid() != null) event.setPaid(updateEvent.getPaid());
        if (updateEvent.getParticipantLimit() != null) event.setParticipantLimit(updateEvent.getParticipantLimit());
        if (updateEvent.getRequestModeration() != null) event.setRequestModeration(updateEvent.getRequestModeration());
        if (updateEvent.getTitle() != null) event.setTitle(updateEvent.getTitle());

        eventRepository.save(event);
        Long confirmedRequests = requestRepository.countConfirmedRequests(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmedRequests, 9L);
        log.info("Putted event by admin: {}", eventFullDto);
        return eventFullDto;
    }

    public EventFullDto getPublicEvent(Long eventId) {
        Event event = idService.getEventById(eventId);
        if (event.getState() != EventState.PUBLISHED)
            throw new IdException("State of event with id=" + eventId + " isn't PUBLISHED");

        Long confirmRequest = requestRepository.countConfirmedRequests(eventId);
        EventFullDto eventFullDto = EventMapper.toEventFullDto(event, confirmRequest, 9L);
        log.info("Getted public event={}", eventFullDto);
        return eventFullDto;
    }

    public List<ParticipationRequestDto> getRequests(Long userId, Long eventId) {
        List<Request> requests = requestRepository.findByEvent_Id(eventId);
        List<ParticipationRequestDto> participationRequestDtos = RequestMapper.toParticipationRequestDto(requests);
        return participationRequestDtos;
    }

    public ParticipationRequestDto confirmRequest(Long reqId, Long eventId, Long userId) {
        if (idService.getRequestById(reqId).getStatus() == RequestState.CONFIRMED)
            throw new IdException("Confirmation of the request with id=" + reqId + " is't required");

        if (requestRepository.countConfirmedRequests(eventId) == idService.getEventById(eventId).getParticipantLimit())
            throw new IdException("Participant limit reached");

        Request request = idService.getRequestById(reqId);
        request.setStatus(RequestState.CONFIRMED);
        requestRepository.save(request);

        if (idService.getRequestById(reqId).getStatus() == RequestState.CONFIRMED) {
            List<Request> requests = requestRepository.countPendingRequests(eventId);

            requests.stream().forEach(req -> {
                req.setStatus(RequestState.REJECTED);
                requestRepository.save(req);
            });
        }
        ParticipationRequestDto participationRequestDto = RequestMapper.toParticipationRequestDto(request);
        log.info("Confirmed request: {}", participationRequestDto);
        return participationRequestDto;
    }

    public ParticipationRequestDto rejectRequest(Long reqId, Long eventId, Long userId) {
        Request request = idService.getRequestById(reqId);
        request.setStatus(RequestState.REJECTED);
        requestRepository.save(request);
        ParticipationRequestDto participationRequestDto = RequestMapper.toParticipationRequestDto(request);
        return participationRequestDto;
    }
}
