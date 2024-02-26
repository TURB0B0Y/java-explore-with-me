package ru.practicum.controller.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDTO;
import ru.practicum.service.RequestService;

import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class UserRequestController {

    private final RequestService requestService;

    @GetMapping
    public List<RequestDTO> getUserRequests(@PathVariable int userId) {
        log.info("getUserRequests {}", userId);
        return requestService.getUserRequests(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestDTO createRequest(@PathVariable int userId, @RequestParam int eventId) {
        log.info("createRequest {} {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDTO cancelRequest(@PathVariable int userId, @PathVariable int requestId) {
        log.info("cancelRequest {} {}", userId, requestId);
        return requestService.cancelRequest(userId, requestId);
    }

}
