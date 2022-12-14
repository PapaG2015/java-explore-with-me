package ru.explorewithme.event.model;

import lombok.*;
import ru.explorewithme.category.model.Category;
import ru.explorewithme.request.model.Request;
import ru.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Entity
@AllArgsConstructor
@Setter
@Table(name = "events", schema = "public")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "location_lat")
    private Float locationLat;
    @Column(name = "location_lon")
    private Float locationLon;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    private String title;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EventState state = EventState.PENDING;
    @OneToMany(mappedBy = "event")
    private List<Request> requests;

    public Event() {
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", annotation='" + annotation + '\'' +
                ", category=" + category +
                ", createdOn=" + createdOn +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", locationLat=" + locationLat +
                ", locationLon=" + locationLon +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                ", initiator=" + initiator +
                ", publishedOn=" + publishedOn +
                ", state=" + state +
                ", requests=" + requests +
                '}';
    }
}
