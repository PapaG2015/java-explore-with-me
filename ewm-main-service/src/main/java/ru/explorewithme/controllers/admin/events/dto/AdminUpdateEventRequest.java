package ru.explorewithme.controllers.admin.events.dto;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.explorewithme.event.model.Location;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
public class AdminUpdateEventRequest {
    //@Length(min = 20, max = 2000)
    private String annotation;
    //@NotNull
    private Long category;
    //@Length(min = 20, max = 7000)
    private String description;
    private LocalDateTime eventDate;
    //@NotNull
    private Location location;
    //@Builder.Default
    private Boolean paid;
    //@Builder.Default
    private Long participantLimit;
    //@Builder.Default
    private Boolean requestModeration;
    //@Length(min = 3, max = 120)
    private String title;

    @Override
    public String toString() {
        return "AdminUpdateEventRequest{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}
