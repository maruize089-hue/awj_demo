package com.example.awj.common.result;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Result<T> {
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    private Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Result<T> success() {
        return new Result<>(200, "成功", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "成功", data);
    }

    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<>(code, message, null);
    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }

    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未授权", null);
    }

    public static <T> Result<T> forbidden() {
        return new Result<>(403, "禁止访问", null);
    }

    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }
}
