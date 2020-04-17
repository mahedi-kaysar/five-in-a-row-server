package com.example.fiveinarowserver.config;

import com.example.fiveinarowserver.controller.GameController;
import com.example.fiveinarowserver.exception.GameException;
import com.example.fiveinarowserver.model.GameResponse;
import com.example.fiveinarowserver.model.exception.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

@Log4j2
@ControllerAdvice
public class GameRestBodyAdvice implements ResponseBodyAdvice<Object> {
    private final String errorHeader = "x-five-in-a-row-game-error";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return !ResponseEntity.class.isAssignableFrom(methodParameter.getMethod().getReturnType())
                && GameController.class.isAssignableFrom(methodParameter.getContainingClass());

    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType,
        Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest,
        ServerHttpResponse serverHttpResponse) {

        if (object instanceof GameResponse) {
            serverHttpResponse.setStatusCode(((GameResponse) object).getHttpStatus());
            return object;
        }

        return GameResponse.builder().payload(object).httpStatus(HttpStatus.OK).build();

    }

    @ExceptionHandler(GameException.class)
    public ResponseEntity handleGameException(HttpServletRequest request,
                                                 GameException exception) {

        ErrorResponse errorResponse = ErrorResponse.builder()
                .errorMessage(exception.getErrorMessage()).build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(this.errorHeader, "true");

        return new ResponseEntity<>(errorResponse, headers, exception.getErrorCode());
    }
}
