package com.github.ka.account.validation.app.provider;

import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class DataProvider implements Function<Request, Mono<Responses.Response>> {

    private final WebClient http;

    public DataProvider(String url) {
        this.http = WebClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public Mono<Responses.Response> apply(Request request) {
        return http.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), Request.class)
                .retrieve()
                .bodyToMono(Responses.Response.class);
    }
}
