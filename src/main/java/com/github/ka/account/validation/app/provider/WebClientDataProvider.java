package com.github.ka.account.validation.app.provider;

import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class WebClientDataProvider implements DataProvider {

    private final WebClient http;
    private final String name;

    public WebClientDataProvider(String name, String url) {
        this.name = name;
        this.http = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Mono<Responses.Response> validate(Request request) {
        return http.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), Request.class)
                .retrieve()
                .bodyToMono(Responses.Response.class);
    }
}
