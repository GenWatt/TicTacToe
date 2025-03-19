package com.adrian.ddd.domain;

import java.util.Optional;

public class Result<T> {
    private final boolean success;
    private final String error;
    private final T data;

    private Result(boolean success, String error, T data) {
        this.success = success;
        this.error = error;
        this.data = data;
    }

    public static <U> Result<U> success(U data) {
        return new Result<>(true, null, data);
    }

    public static <U> Result<U> success() {
        return new Result<>(true, null, null);
    }

    public static <U> Result<U> failure(String error) {
        return new Result<>(false, error, null);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public Optional<T> getData() {
        return Optional.ofNullable(data);
    }

    public Optional<String> getError() {
        return Optional.ofNullable(error);
    }
}