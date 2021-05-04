package com.github.ka.account.validation.app;

import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import com.github.ka.account.validation.app.provider.DataProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ValidationController {

    @Autowired
    private List<DataProvider> providers;

    @PostMapping("validate")
    public Mono<Responses> validate(@RequestBody Request request) {
        return Flux.fromIterable(providers)
                .filter(p -> request.matches(p.getName()))
                .flatMap(p -> p.validate(request))
                .collectList()
                .map(r -> {
                    log.info("request {}, responses {} ", request, r);
                    var responses = new Responses();
                    responses.setRequest(request);
                    responses.setResponses(r);
                    return responses;
                });
    }
}
