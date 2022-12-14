package ru.explorewithme.event.dto;

import lombok.Builder;
import lombok.Getter;
import ru.explorewithme.category.dto.CategoryDto;
import ru.explorewithme.user.dto.UserShortDto;

@Builder
@Getter
public class EventShortDto {
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;

    @Override
    public String toString() {
        return "EventShortDto{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", confirmedRequests=" + confirmedRequests +
                ", eventDate='" + eventDate + '\'' +
                ", id=" + id +
                ", initiator=" + initiator +
                ", paid=" + paid +
                ", title='" + title + '\'' +
                ", views=" + views +
                '}';
    }
}
