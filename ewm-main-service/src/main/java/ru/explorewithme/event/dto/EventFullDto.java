package ru.explorewithme.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.explorewithme.category.dto.CategoryDto;
import ru.explorewithme.user.dto.UserShortDto;
import ru.explorewithme.event.model.Location;

@Builder
@Getter
@Setter
public class EventFullDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Long views;

    @Override
    public String toString() {
        return "EventFullDto{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", createdOn='" + createdOn + '\'' +
                ", description='" + description + '\'' +
                ", eventDate='" + eventDate + '\'' +
                ", id=" + id +
                ", initiator=" + initiator +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", publishedOn='" + publishedOn + '\'' +
                ", requestModeration=" + requestModeration +
                ", state=" + state +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
