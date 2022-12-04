package ru.explorewithme.comment.dto;

import lombok.Builder;
import lombok.Getter;
import ru.explorewithme.event.dto.EventFullDto;
import ru.explorewithme.user.dto.UserDto;;

@Getter
@Builder
public class CommentDto {
    private Long id;
    private String comment;
    private String dateOfPublic;
    private Long eventId;
    private String eventTitle;
    private UserDto user;


    @Override
    public String toString() {
        return "CommentDto{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", dateOfPublic='" + dateOfPublic + '\'' +
                ", eventId=" + eventId +
                ", eventTitle='" + eventTitle + '\'' +
                ", user=" + user +
                '}';
    }
}
