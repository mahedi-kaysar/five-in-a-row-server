package com.example.fiveinarowserver.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */
public class GameException extends RuntimeException {

    /**
     *
     */
    private String errorMessage = "An unknown exception";

    /**
     *
     */
    private HttpStatus errorCode = HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     *
     * @param errorMessage
     * @param httpStatus
     */
    public GameException(String errorMessage, HttpStatus httpStatus) {
        this.errorMessage = errorMessage;
        this.errorCode = httpStatus;
    }

    /**
     *
     * @param errorMessage
     */
    public GameException(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
