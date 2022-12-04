package ru.explorewithme.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.explorewithme.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUser_Id(Long userId);

    List<Comment> findByEvent_Id(Long eventId);
}
