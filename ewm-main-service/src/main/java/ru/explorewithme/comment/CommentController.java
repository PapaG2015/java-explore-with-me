package ru.explorewithme.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.explorewithme.comment.dto.CommentDto;
import ru.explorewithme.comment.dto.NewCommentDto;
import ru.explorewithme.comment.dto.UpdateCommentDto;
import ru.explorewithme.comment.model.Comment;

import java.util.List;

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

    @PatchMapping("/events/{eventId}/comments")
    public CommentDto addComment(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 @RequestBody UpdateCommentDto updateComment) {
        log.info("Changing comment={} by user with id={} for event with id={}", updateComment, userId, eventId);
        return commentService.updateComment(userId, eventId, updateComment);
    }

    @DeleteMapping("events/{eventId}/comment/{comId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long comId) {
        log.info("Deleting comment with id={} by user with id={} for event with id={}", comId, userId, eventId);
        commentService.deleteComment(userId, eventId, comId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getCommentsOfUser(@PathVariable Long userId) {
        log.info("Getting comments of user with id={}", userId);
        return commentService.getCommentsOfUser(userId);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsOfEvent(@PathVariable Long eventId) {
        log.info("Getting comments of event with id={}", eventId);
        return commentService.getCommentsOfEvent(eventId);
    }
}