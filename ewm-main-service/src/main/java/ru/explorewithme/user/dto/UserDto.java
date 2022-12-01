package ru.explorewithme.user.dto;

import ru.explorewithme.comment.model.Comment;
import ru.explorewithme.user.model.User;

import java.util.List;

public class UserDto extends User {
    public UserDto(Long id, String name, String email) {
        super(id, name, email);
    }
}
