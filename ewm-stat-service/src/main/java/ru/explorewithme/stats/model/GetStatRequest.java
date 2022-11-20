package ru.explorewithme.stats.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@Setter
@Getter
public class GetStatRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    private Set<String> uris;
    private Boolean unique;

    public static GetStatRequest of(LocalDateTime start,
                                    LocalDateTime end,
                                    Set<String> uris,
                                    Boolean unique) {
        GetStatRequest request = new GetStatRequest();
        request.setStart(start);
        request.setEnd(end);
        request.setUris(uris);
        request.setUnique(unique);
        return request;
    }
}
