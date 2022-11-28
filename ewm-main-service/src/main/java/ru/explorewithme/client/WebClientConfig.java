package ru.explorewithme.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfig {
    private static final String API_PREFIX = "/stats";

    @Value("${stat-server.url}")
    String serverUrl;

    @Bean
    public StatGetClient createStatGetClient(RestTemplateBuilder builder) {
        return new StatGetClient(serverUrl + API_PREFIX, builder);
    }
}
