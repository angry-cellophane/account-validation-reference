package com.github.ka.account.validation.app;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @Data
    public static class Provider {
        private String name;
        private String url;
    }

    private List<Provider> providers;
}
