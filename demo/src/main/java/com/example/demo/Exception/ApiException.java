package com.example.demo.Exception;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor

public class ApiException {
    private final String message;



    private final HttpStatus httpStatus;

    private final ZonedDateTime timestamp;


}
