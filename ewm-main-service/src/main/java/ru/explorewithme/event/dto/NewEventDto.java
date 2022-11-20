package ru.explorewithme.event.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.explorewithme.event.model.Location;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class NewEventDto {
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @Length(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    @NotNull
    private Location location;
    @Builder.Default
    private Boolean paid = false;
    @Builder.Default
    private Long participantLimit = 0L;
    @Builder.Default
    private Boolean requestModeration = true;
    @Length(min = 3, max = 120)
    private String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation='" + annotation + '\'' + "\n" +
                ", category=" + category + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", eventDate='" + eventDate + '\'' + "\n" +
                ", location=" + location + "\n" +
                ", paid=" + paid + "\n" +
                ", participantLimit=" + participantLimit + "\n" +
                ", requestModeration=" + requestModeration + "\n" +
                ", title='" + title + '\'' +
                '}';
    }
}
