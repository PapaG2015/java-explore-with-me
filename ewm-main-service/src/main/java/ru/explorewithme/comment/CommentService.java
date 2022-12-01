package ru.explorewithme.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.explorewithme.IdService;
import ru.explorewithme.comment.dto.CommentDto;
import ru.explorewithme.comment.dto.NewCommentDto;
import ru.explorewithme.comment.model.Comment;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.event.model.EventState;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.user.UserRepository;
import ru.explorewithme.user.model.User;

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
}
