package com.example.fiveinarowserver.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
public class GameResponse<T extends Object> {

    /**
     * Payload of the response.
     */
    private final T payload;

    /**
     * HttpStatus of the response.
     */
    @JsonIgnore
    private final HttpStatus httpStatus;
}
