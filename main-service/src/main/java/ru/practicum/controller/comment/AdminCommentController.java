package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.dto.comment.CreateCommentDTO;
import ru.practicum.service.CommentService;

import javax.validation.Valid;

@Slf4j
@SuppressWarnings("unused")
@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
public class AdminCommentController {

    private final CommentService commentService;

    @PatchMapping
    public CommentDTO renewalCommentAdmin(@RequestBody @Valid CreateCommentDTO dto, @PathVariable Integer commentId) {
        log.info("renewalCommentAdmin {} {}", commentId, dto);
        return commentService.editCommentAdmin(commentId, dto);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Integer commentId) {
        log.info("deleteCommentAdmin {}", commentId);
        commentService.deleteCommentAdmin(commentId);
    }

}
