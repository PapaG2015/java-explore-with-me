package ru.explorewithme;

import org.springframework.stereotype.Service;
import ru.explorewithme.category.CategoryRepository;
import ru.explorewithme.comment.CommentRepository;
import ru.explorewithme.comment.model.Comment;
import ru.explorewithme.compilation.CompilationRepository;
import ru.explorewithme.category.model.Category;
import ru.explorewithme.compilation.model.Compilation;
import ru.explorewithme.user.model.User;
import ru.explorewithme.user.UserRepository;
import ru.explorewithme.exception.IdException;
import ru.explorewithme.event.EventRepository;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.model.Request;
import ru.explorewithme.request.RequestRepository;

@Service
public class IdService {
    private UserRepository userRepository;
    private EventRepository eventRepository;

    private CategoryRepository categoryRepository;

    private RequestRepository requestRepository;

    private CompilationRepository compilationRepository;

    private CommentRepository commentRepository;

    public IdService(UserRepository userRepository,
                     EventRepository eventRepository,
                     CategoryRepository categoryRepository,
                     CompilationRepository compilationRepository,
                     RequestRepository requestRepository,
                     CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.categoryRepository = categoryRepository;
        this.compilationRepository = compilationRepository;
        this.requestRepository = requestRepository;
        this.commentRepository = commentRepository;
    }

    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new IdException("no event with such id=" + eventId));
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IdException("no user with such id=" + userId));
    }

    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() -> new IdException("no category with such id=" + catId));
    }

    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() -> new IdException("no compilation with such id=" + compId));
    }

    public Request getRequestById(Long reqId) {
        return requestRepository.findById(reqId).orElseThrow(() -> new IdException("no request with such id=" + reqId));
    }

    public Comment getCommentById(Long comId) {
        return commentRepository.findById(comId).orElseThrow(() -> new IdException("no comment with such id=" + comId));
    }
}
