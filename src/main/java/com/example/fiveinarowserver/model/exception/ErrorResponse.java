package com.example.fiveinarowserver.model.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    /**
     * Class level instance of a String error message.
     */
    private final String errorMessage;
}

