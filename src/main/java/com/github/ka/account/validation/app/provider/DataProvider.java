package com.github.ka.account.validation.app.provider;

import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import reactor.core.publisher.Mono;

public interface DataProvider {
    String getName();
    Mono<Responses.Response> validate(Request request);
}
