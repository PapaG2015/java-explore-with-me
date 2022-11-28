package ru.explorewithme.controllers.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.explorewithme.client.BaseClient;
import ru.explorewithme.controllers.events.model.EndPoint;

@Service
public class StatPostClient extends BaseClient {
    private static final String API_PREFIX = "/hit";

    @Autowired
    //shareit-server.url=http://localhost:9090
    public StatPostClient(@Value("${stat-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addEndPoint(Long userId, EndPoint endPoint) {
        return post("", userId, endPoint);
    }
}
