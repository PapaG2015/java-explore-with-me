package ru.explorewithme.comment.model;

import lombok.Builder;
import lombok.Getter;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Table(name = "comments", schema = "public")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    private LocalDateTime dateOfPublic;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; //тот, кто комментирует
}
