package ru.explorewithme.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.explorewithme.event.model.Event;
import ru.explorewithme.request.model.Request;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface RequestRepository extends JpaRepository<Request, Long>, RequestRepositoryCustom {
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
