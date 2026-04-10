package com.javanauta.fake_api_us.infrastructure.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String message;
    private LocalDateTime data;
    private int status;
    private String path;
}