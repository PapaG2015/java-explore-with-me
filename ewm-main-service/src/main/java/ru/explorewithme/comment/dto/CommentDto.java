package ru.explorewithme.comment.dto;

import lombok.Builder;
import lombok.Getter;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.user.model.User;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String comment;
    private String dateOfPublic;
    private Event event;
    private User user;
}
