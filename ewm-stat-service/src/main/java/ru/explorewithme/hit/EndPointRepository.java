package ru.explorewithme.hit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.explorewithme.hit.model.EndPointHit;

public interface EndPointRepository extends JpaRepository<EndPointHit, Long>, QuerydslPredicateExecutor<EndPointHit> {
}
