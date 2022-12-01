package ru.explorewithme.comment;

import ru.explorewithme.comment.dto.CommentDto;
import ru.explorewithme.comment.dto.NewCommentDto;
import ru.explorewithme.comment.model.Comment;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.user.model.User;

import java.time.LocalDateTime;

import static ru.explorewithme.functions.Function.fromDateToString;

public class CommentMapper {
    public static Comment toComment(User user, Event event, NewCommentDto commentDto) {
        return Comment
                .builder()
                .id(null)
                .user(user)
                .event(event)
                .comment(commentDto.getComment())
                .dateOfPublic(LocalDateTime.now())
                .build();
    }

    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto
                .builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .dateOfPublic(fromDateToString(comment.getDateOfPublic()))
                .event(comment.getEvent())
                .user(comment.getUser())
                .build();
    }
}
