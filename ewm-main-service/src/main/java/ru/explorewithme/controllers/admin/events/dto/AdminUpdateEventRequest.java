package ru.explorewithme.controllers.admin.events.dto;

import lombok.Getter;
import ru.explorewithme.event.model.Location;
import java.time.LocalDateTime;

@Getter
public class AdminUpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
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
