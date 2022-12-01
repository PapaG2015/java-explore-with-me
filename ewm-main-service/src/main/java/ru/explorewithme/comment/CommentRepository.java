package ru.explorewithme.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.explorewithme.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
