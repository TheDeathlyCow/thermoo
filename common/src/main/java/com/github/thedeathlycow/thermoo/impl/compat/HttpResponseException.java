package com.github.thedeathlycow.thermoo.impl.compat;

import java.io.IOException;

public class HttpResponseException extends IOException {
    public HttpResponseException(int code, String body) {
        super("HTTP responses returned %d: %s".formatted(code, body));
    }
}