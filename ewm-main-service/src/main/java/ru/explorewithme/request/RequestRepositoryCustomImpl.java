package ru.explorewithme.request;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Lazy;
import ru.explorewithme.request.model.QRequest;
import ru.explorewithme.request.model.RequestState;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestRepositoryCustomImpl implements RequestRepositoryCustom {

    private RequestRepository requestRepository;
    private EntityManager entityManager;

    public RequestRepositoryCustomImpl(@Lazy RequestRepository requestRepository,
                                       EntityManager entityManager) {
        this.requestRepository = requestRepository;
        this.entityManager = entityManager;
    }

    @Override
    public Map<Long, Long> countConfirmedRequests(List<Long> ids) {
        QRequest request = QRequest.request;
        List<BooleanExpression> conditions = new ArrayList<>();
        conditions.add(request.event.id.in(ids));
        conditions.add(request.status.eq(RequestState.CONFIRMED));

        BooleanExpression finalCondition = conditions.stream()
                .reduce(BooleanExpression::and)
                .get();

        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        List<Tuple> tuples =
                queryFactory.select(
                                request.event.id, request.event.state.count())
                        .from(request)
                        .where(finalCondition)
                        .groupBy(request.event.id)
                        .fetch();

        return tuples
                .stream()
                .collect(
                        Collectors.toMap(
                                tuple -> tuple.get(request.event.id),
                                tuple -> tuple.get(request.event.state.count())
                        ));
    }
}
