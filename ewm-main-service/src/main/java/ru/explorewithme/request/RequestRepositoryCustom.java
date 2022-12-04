package ru.explorewithme.request;

import java.util.List;
import java.util.Map;

public interface RequestRepositoryCustom {
    Map<Long, Long> countConfirmedRequests(List<Long> ids);
}
