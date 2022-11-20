package ru.explorewithme.stats;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Service;
import ru.explorewithme.hit.EndPointRepository;

import java.util.ArrayList;
import java.util.List;

import ru.explorewithme.hit.model.QEndPointHit;
import ru.explorewithme.stats.model.GetStatRequest;
import ru.explorewithme.stats.model.ViewStats;

import javax.persistence.EntityManager;

@Service
public class StatService {
    EndPointRepository endPointRepository;

    EntityManager entityManager;

    public StatService(EndPointRepository endPointRepository, EntityManager entityManager) {
        this.endPointRepository = endPointRepository;
        this.entityManager = entityManager;
    }

    public List<ViewStats> getStat(GetStatRequest req) {
        QEndPointHit endPointHit = QEndPointHit.endPointHit;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(endPointHit.timestamp.between(req.getStart(), req.getEnd()));
        conditions.add(endPointHit.uri.in(req.getUris()));

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        //NumberPath<Long> count = Expressions.numberPath(Long.class, "c");

        List<Tuple> tuple = queryFactory.select(
            endPointHit.uri, endPointHit.id.count())
                .from(endPointHit)
                .where(finalCondition)
                .groupBy(endPointHit.uri)
                .fetch();

        List<ViewStats> stats = new ArrayList<>();
        for (Tuple t : tuple) {
            ViewStats viewStats = new ViewStats(
                    "ewm-main-service",
                    t.get(endPointHit.uri),
            t.get(endPointHit.id.count()));
            stats.add(viewStats);

        }
        return stats;
    }
}
