package com.unloadbrain.assignement.evbox.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ApplicationExceptionHandler extends BaseExceptionHandler {

    public ApplicationExceptionHandler() {
        super(log);
    }

    @ExceptionHandler(ChargingSessionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleSessionNotFoundException(final ChargingSessionNotFoundException ex) {
        log.error("Session not found thrown ", ex);
        return new ErrorResponse("SESSION_NOT_FOUND", "The session was not found");
    }
}
