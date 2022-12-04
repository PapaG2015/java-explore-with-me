package ru.explorewithme.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.comment.dto.CommentDto;
import ru.explorewithme.comment.dto.NewCommentDto;
import ru.explorewithme.comment.dto.UpdateCommentDto;
import ru.explorewithme.comment.model.Comment;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.event.model.EventState;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.user.UserRepository;
import ru.explorewithme.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CommentService {
    private CommentRepository commentRepository;
    private UserRepository userRepository;
    private EventRepository eventRepository;
    private IdService idService;

    public CommentService(CommentRepository commentRepository,
                          UserRepository userRepository,
                          EventRepository eventRepository,
                          IdService idService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.idService = idService;
    }

    public CommentDto addComment(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = idService.getUserById(userId);

        Event event = idService.getEventById(eventId);
        if (event.getState() != EventState.PUBLISHED) throw new IdException("You can't commit unpublished events");

        Comment comment = CommentMapper.toComment(user, event, newCommentDto);
        comment = commentRepository.save(comment);

        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        log.info("Added new comment={}", commentDto);
        return commentDto;
    }

    public CommentDto updateComment(Long userId, Long eventId, UpdateCommentDto updateComment) {
        Comment comment = idService.getCommentById(updateComment.getId());

        if (comment.getUser().getId() != userId) throw new IdException("It's not your comment");
        if (LocalDateTime.now().isAfter(comment.getDateOfPublic().plusMinutes(10)))
            throw new IdException("It's to late to change comment");

        if (updateComment.getComment() != null) comment.setComment(updateComment.getComment());

        commentRepository.save(comment);

        CommentDto commentDto = CommentMapper.toCommentDto(comment);
        log.info("Changed comment={}", commentDto);
        return commentDto;
    }

    public void deleteComment(Long userId, Long eventId, Long comId) {
        Comment comment = idService.getCommentById(comId);

        if (comment.getUser().getId() != userId) throw new IdException("It's not your comment");

        log.info("Deleted comment with id={} by user with id={} for event with id={}", comId, userId, eventId);
        commentRepository.deleteById(comId);
    }

    public List<CommentDto> getCommentsOfUser(Long userId) {
        List<Comment> comments = commentRepository.findByUser_Id(userId);

        List<CommentDto> commentDtos = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        log.info("Getted comments={}  of user with id={}", commentDtos, userId);
        return  commentDtos;
    }

    public List<CommentDto> getCommentsOfEvent(Long eventId) {
        List<Comment> comments = commentRepository.findByEvent_Id(eventId);

        List<CommentDto> commentDtos = comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList());
        log.info("Getted comments={}  of event with id={}", commentDtos, eventId);
        return  commentDtos;
    }
}
