package com.github.ka.account.validation.app.model;

import lombok.Data;
import lombok.Value;

import java.util.List;

@Data
public class Responses {

    @Data
    public static class Response {
        String provider;
        boolean isValid;
    }

    Request request;
    List<Response> responses;
}
