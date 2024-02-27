package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.dto.comment.CreateCommentDTO;
import ru.practicum.filter.PageFilter;
import ru.practicum.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class UserCommentController {

    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDTO addComment(
            @RequestBody @Valid CreateCommentDTO dto,
            @PathVariable Integer userId,
            @PathVariable Integer eventId
    ) {
        log.info("addComment {} {} {}", eventId, userId, dto);
        return commentService.addComment(dto, userId, eventId);
    }

    @PatchMapping("/{commentId}")
    public CommentDTO renewalComment(
            @RequestBody @Valid CreateCommentDTO dto,
            @PathVariable Integer userId,
            @PathVariable Integer commentId
    ) {
        log.info("renewalComment {} {}", commentId, userId);
        return commentService.editComment(dto, userId, commentId);
    }

    @GetMapping("/{commentId}")
    public CommentDTO getCommentById(@PathVariable Integer userId, @PathVariable Integer commentId) {
        log.info("getCommentById {} {}", commentId, userId);
        return commentService.getById(userId, commentId);
    }

    @GetMapping
    public List<CommentDTO> getAllUserComments(@PathVariable Integer userId, @Valid PageFilter pageFilter) {
        log.info("getAllUserComments {}", userId);
        return commentService.getAllUserComments(userId, pageFilter);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Integer userId, @PathVariable Integer commentId) {
        log.info("deleteCommentById {} {}", commentId, userId);
        commentService.deleteCommentById(userId, commentId);
    }

}
