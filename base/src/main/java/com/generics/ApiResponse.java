package com.generics;

import java.util.Objects;

public class ApiResponse <T> {

    private final int statusCode;
    private final T body;

    public ApiResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public T getBody() {
        return body;
    }

    public boolean isOk() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "statusCode=" + statusCode +
                ", body=" + body +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return statusCode == that.statusCode && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCode, body);
    }
}
