package ru.explorewithme.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.event.model.EventState;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.request.model.RequestState;
import ru.explorewithme.user.model.User;
import ru.explorewithme.user.UserRepository;
import ru.explorewithme.request.dto.ParticipationRequestDto;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.model.Request;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class RequestService {
    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private IdService idService;

    public RequestService(RequestRepository requestRepository,
                          UserRepository userRepository,
                          EventRepository eventRepository,
                          IdService idService) {
        this.requestRepository = requestRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.idService = idService;
    }

    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        if (requestRepository.findByEvent_IdAndRequester_Id(eventId, userId).isPresent())
            throw new IdException("Repeat request (userId=" + userId + ", eventId=" + eventId);

        Event event = idService.getEventById(eventId);
        if (event.getInitiator().getId() == userId) throw new IdException("It's your event");

        if (event.getState() != EventState.PUBLISHED)
            throw new IdException("Event with id=" + eventId + " isn't published");

        if (requestRepository.countConfirmedRequests(eventId) == event.getParticipantLimit())
            throw new IdException("Participant limit reached");

        User requester = idService.getUserById(userId);
        Request request = Request.builder()
                .id(null)
                .created(LocalDateTime.now())
                .status(RequestState.PENDING)
                .event(event)
                .requester(requester)
                .build();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) request.setStatus(RequestState.CONFIRMED);

        ParticipationRequestDto participationRequest = RequestMapper.toParticipationRequestDto(
                requestRepository.save(request));

        log.info("Added request={} of user with id={} for event with id={}", userId, eventId);

        return participationRequest;
    }

    public List<ParticipationRequestDto> getAllRequestOfUser(Long userId) {
        List<Request> requests = requestRepository.findByRequester_Id(userId);
        log.info("For user with id={} getted requests:{}", userId, requests);
        return RequestMapper.toParticipationRequestDto(requests);
    }

    public ParticipationRequestDto cancelRequestOfUser(Long userId, Long requestId) {
        Request request = idService.getRequestById(requestId);
        request.setStatus(RequestState.CANCELED);
        requestRepository.save(request);
        ParticipationRequestDto participationRequest = RequestMapper.toParticipationRequestDto(request);
        log.info("Cancelled request with id={} of user with id={}", requestId, userId);
        return participationRequest;
    }
}
