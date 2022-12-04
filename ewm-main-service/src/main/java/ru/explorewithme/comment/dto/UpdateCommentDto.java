package ru.explorewithme.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class UpdateCommentDto {
    @NotNull
    private Long id;
    @NotBlank
    private String comment;

    @Override
    public String toString() {
        return "UpdateCommentDto{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                '}';
    }
}
