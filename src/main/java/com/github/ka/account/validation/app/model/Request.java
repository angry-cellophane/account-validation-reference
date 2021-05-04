package com.github.ka.account.validation.app.model;

import lombok.Data;

import java.util.List;

@Data
public class Request {
    String accountNumber;
    String sortCode;
    List<String> providers;

    public boolean matches(String providerName) {
        return providers == null || providers.isEmpty()
                || providers.contains(providerName);
    }
}
