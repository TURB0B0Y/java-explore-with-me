package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public List<CommentDTO> getAllCommentsForEvent(@PathVariable Integer eventId, @Valid PageFilter pageFilter) {
        log.info("getAllCommentsForEvent {}", eventId);
        return commentService.getAllCommentsByEvent(eventId, pageFilter);
    }

}
