package ru.explorewithme.comment.model;

import lombok.*;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Entity
@Getter
@Table(name = "comments", schema = "public")
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", dateOfPublic=" + dateOfPublic +
                ", event=" + event +
                ", user=" + user +
                '}';
    }


}
