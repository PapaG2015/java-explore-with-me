package ru.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class StatGetClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    public StatGetClient(String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<ViewStats> getEndPoint(LocalDateTime start, LocalDateTime end, List<String> uris) {
        String stringStart = toStringFromDate(start);
        String stringEnd = toStringFromDate(end);

        Map<String, Object> parameters = Map.of(
                "start", stringStart,
                "end", stringEnd,
                "uris", toStringFromUris(uris),
                "unique", false
        );

        List<ViewStats> viewStats = get("?start={start}&end={end}&uris={uris}&unique={unique}", 0L, parameters).getBody();

        return viewStats;
    }

    private String toStringFromDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String s = date.format(formatter);
        return s;
    }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    private String toStringFromUris(List<String> uris) {
        return uris.stream().collect(Collectors.joining(","));
    }
}
