package com.github.ka.account.validation.app;

import com.github.ka.account.validation.app.model.Request;
import com.github.ka.account.validation.app.model.Responses;
import com.github.ka.account.validation.app.provider.DataProvider;
import com.github.ka.account.validation.app.provider.WebClientDataProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class AppConfiguration {

    @Bean
    public List<DataProvider> dataProviders(AppProperties properties) {
        var providers = properties.getProviders();
        if (providers == null) return Collections.emptyList();

        return providers.stream()
                .map(p -> new WebClientDataProvider(p.getName(), p.getUrl()))
                .collect(Collectors.toList());
    }
}
