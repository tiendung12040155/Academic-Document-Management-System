package com.example.ADMS.exception;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiException extends RuntimeException {
    HttpStatus status;
    CustomError error;

    public static ApiException notFoundException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("404")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException badRequestException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("400")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException internalServerException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("500")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException forBiddenException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("403")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException unAuthorizedException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("401")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException maxSizeException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("417")
                        .message(message)
                        .build())
                .build();
    }

    public static ApiException conflictResourceException(String message) {
        return ApiException.builder().status(HttpStatus.ACCEPTED)
                .error(CustomError.builder()
                        .code("409")
                        .message(message)
                        .build())
                .build();
    }
}