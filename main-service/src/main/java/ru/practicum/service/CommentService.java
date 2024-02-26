package ru.practicum.service;

import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.dto.comment.CreateCommentDTO;
import ru.practicum.filter.PageFilter;

import java.util.List;

public interface CommentService {

    CommentDTO editCommentAdmin(Integer commentId, CreateCommentDTO dto);

    void deleteCommentAdmin(Integer commentId);

    List<CommentDTO> getAllCommentsByEvent(Integer eventId, PageFilter pageFilter);

    CommentDTO addComment(CreateCommentDTO dto, Integer userId, Integer eventId);

    CommentDTO editComment(CreateCommentDTO dto, Integer userId, Integer commentId);

    CommentDTO getById(Integer userId, Integer commentId);

    List<CommentDTO> getAllUserComments(Integer userId, PageFilter pageFilter);

    void deleteCommentById(Integer userId, Integer commentId);

}
