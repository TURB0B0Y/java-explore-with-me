package ru.practicum.mapper;

import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.model.Comment;

public class CommentMapper {

    public static CommentDTO toDto(Comment comment, Integer views) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthor(UserMapper.toDto(comment.getAuthor()));
        dto.setEvent(EventMapper.toShortDto(comment.getEvent(), views == null ? 0 : views));
        dto.setCreateDate(comment.getCreated());
        return dto;
    }

}
