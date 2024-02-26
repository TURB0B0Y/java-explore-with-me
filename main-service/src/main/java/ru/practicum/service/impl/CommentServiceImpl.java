package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatisticClient;
import ru.practicum.dto.StatisticDTO;
import ru.practicum.dto.comment.CommentDTO;
import ru.practicum.dto.comment.CreateCommentDTO;
import ru.practicum.enums.EventState;
import ru.practicum.environment.Environments;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.filter.PageFilter;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.CommentService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private static final DateTimeFormatter DF = DateTimeFormatter.ofPattern(Environments.DATE_FORMAT);
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final StatisticClient statisticClient;

    @Override
    @Transactional
    public CommentDTO editCommentAdmin(Integer commentId, CreateCommentDTO dto) {
        Comment commentFromDB = getCommentById(commentId);
        commentFromDB.setText(dto.getText());
        Map<Integer, Integer> views = getViews(Collections.singletonList(commentFromDB.getEvent()));
        return CommentMapper.toDto(commentFromDB, views.get(commentFromDB.getEvent().getId()));
    }

    @Override
    @Transactional
    public void deleteCommentAdmin(Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllCommentsByEvent(Integer eventId, PageFilter pageFilter) {
        Event event = getEventById(eventId);
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        List<Comment> comments = commentRepository.findAllByEvent(event, pageable);
        List<Event> events = comments.stream().map(Comment::getEvent).collect(Collectors.toList());
        Map<Integer, Integer> views = getViews(events);
        return comments.stream().map(c -> CommentMapper.toDto(c, views.get(c.getEvent().getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDTO addComment(CreateCommentDTO dto, Integer userId, Integer eventId) {
        Event event = getEventById(eventId);
        User author = getUserById(userId);
        if (EventState.PUBLISHED.equals(event.getState()))
            throw new ConflictException("Only publish event can been commented");
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(dto.getText());
        Map<Integer, Integer> views = getViews(Collections.singletonList(event));
        return CommentMapper.toDto(commentRepository.save(comment), views.get(eventId));
    }

    @Override
    @Transactional
    public CommentDTO editComment(CreateCommentDTO dto, Integer userId, Integer commentId) {
        Comment commentFromDB = getCommentById(commentId);
        User author = getUserById(userId);
        if (!commentFromDB.getAuthor().getId().equals(author.getId()))
            throw new ConflictException("Only comment owner or admin can been edit comment");
        commentFromDB.setText(dto.getText());
        Map<Integer, Integer> views = getViews(Collections.singletonList(commentFromDB.getEvent()));
        return CommentMapper.toDto(commentFromDB, views.get(commentFromDB.getEvent().getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public CommentDTO getById(Integer userId, Integer commentId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NotFoundException("Comment with id=%s was not found", commentId);
        }
        Map<Integer, Integer> views = getViews(Collections.singletonList(comment.getEvent()));
        return CommentMapper.toDto(comment, views.get(comment.getEvent().getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentDTO> getAllUserComments(Integer userId, PageFilter pageFilter) {
        User author = getUserById(userId);
        Pageable pageable = PageRequest.of(pageFilter.getFrom() / pageFilter.getSize(), pageFilter.getSize());
        List<Comment> comments = commentRepository.findAllByAuthor(author, pageable);
        List<Event> events = comments.stream().map(Comment::getEvent).collect(Collectors.toList());
        Map<Integer, Integer> views = getViews(events);
        return comments.stream().map(c -> CommentMapper.toDto(c, views.get(c.getEvent().getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCommentById(Integer userId, Integer commentId) {
        Comment comment = getCommentById(commentId);
        User author = getUserById(userId);
        if (!comment.getAuthor().getId().equals(author.getId()))
            throw new ConflictException("Only comment owner or admin can been delete comment");
        commentRepository.delete(comment);
    }

    private Comment getCommentById(Integer commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=%s was not found", commentId));
    }

    private Map<Integer, Integer> getViews(List<Event> events) {
        String[] uris = events.stream().map(event -> "/events/" + event.getId()).toArray(String[]::new);
        List<StatisticDTO> stats = statisticClient.getStats(
                LocalDateTime.now().minusHours(5).format(DF),
                LocalDateTime.now().plusHours(5).format(DF),
                uris,
                true
        ).getBody();
        if (stats == null || stats.isEmpty())
            return Collections.emptyMap();
        Map<Integer, Integer> views = new HashMap<>();
        for (StatisticDTO state : stats) {
            String eventIdStr = state.getUri().substring(state.getUri().lastIndexOf('/') + 1);
            int eventId = Integer.parseInt(eventIdStr);
            views.put(eventId, state.getHits());
        }
        return views;
    }

    private Event getEventById(int eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EventServiceImpl.EVENT_NOT_FOUND_TEMPLATE, eventId));
    }

    private User getUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=%s was not found", userId));
    }

}
