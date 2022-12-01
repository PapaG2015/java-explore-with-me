package ru.explorewithme.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.comment.dto.CommentDto;
import ru.explorewithme.comment.dto.NewCommentDto;

@RestController
@Slf4j
@RequestMapping(path = "/users/{userId}")
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/events/{eventId}/comments")
    public CommentDto addComment(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 @RequestBody NewCommentDto newComment) {
        log.info("Adding new comment={} by user with id={} for event with id={}", newComment, userId, eventId);
        return commentService.addComment(userId, eventId, newComment);
    }
}

  /*  Разработана фича "комментарии"
        1) добавление комментария к событию:
        POST /users/{userId}/events/{eventId}/comments
        RequestBody: NewCommentDto
        Re
        2) изменение комментария пользователем
        PATCH /users/{userId}/events/{eventId}/comments
        Изменять комментарий можно, селм с момента опубликования прошло не более 10 мин.
        RequestBody: UpdateCommentDto
        3) удаление комментария пользователя
        DELETE /users/{userId}/events/{eventId}/comment/{comId}
        4) получение всех комментариев пользователя
        GET /users/{userId}/comments
        */