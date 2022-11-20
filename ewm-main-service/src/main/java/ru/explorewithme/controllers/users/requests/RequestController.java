package ru.explorewithme.controllers.users.requests;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.request.RequestService;
import ru.explorewithme.request.dto.ParticipationRequestDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@Slf4j
public class RequestController {
    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        log.info("User with id={} is creating new request for event with id={}", userId, eventId);
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<ParticipationRequestDto> getAllRequestOfUser(@PathVariable Long userId) {
        log.info("Getting all requests of user with id={}", userId);
        return requestService.getAllRequestOfUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestOfUser(@PathVariable Long userId, @PathVariable Long requestId) {
        log.info("Cancelling request with id={} of user with id={}", requestId, userId);
        return requestService.cancelRequestOfUser(userId, requestId);
    }
}
