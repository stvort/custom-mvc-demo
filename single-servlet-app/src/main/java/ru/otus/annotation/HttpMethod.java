package ru.otus.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum HttpMethod {
    GET("GET"),
    POST("POST");

    private final String value;
}
