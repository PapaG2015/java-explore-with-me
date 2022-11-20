package ru.explorewithme.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.explorewithme.controllers.events.model.EndPoint;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class StatGetClient extends BaseClient {
    private static final String API_PREFIX = "/stats";

    @Autowired
    //shareit-server.url=http://localhost:9090
    public StatGetClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getEndPoint(LocalDateTime start, LocalDateTime end, List<String> uris) {
        String stringStart = encodeValue(toStringFromDate(start));
        String endStart = encodeValue(toStringFromDate(end));

        Map<String, Object> parameters = Map.of(
                "start", stringStart,
                "end", endStart,
                "uris", uris,
                "unique", false
        );
        return get("?start={start}&end={end}&uris={uris}&unique={unique}", 0L, parameters);
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
}
