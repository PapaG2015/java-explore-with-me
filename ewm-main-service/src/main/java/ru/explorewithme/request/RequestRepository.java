package ru.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.explorewithme.request.model.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEvent_Id(Long eventId);

    List<Request> findByRequester_Id(Long requesterId);

    Optional<Request> findByEvent_IdAndRequester_Id(Long eventId, Long requesterId);

    @Query(value = "SELECT COUNT(req) " +
            "FROM Request AS req "+
            "WHERE req.event.id = ?1 "+
            "AND req.status = 'CONFIRMED'")
    Long countConfirmedRequests(Long eventId);

    @Query(value = "SELECT req " +
            "FROM Request AS req "+
            "WHERE req.event.id = ?1 "+
            "AND req.status <> 'PENDING'")
    List<Request> countPendingRequests(Long eventId);
}
