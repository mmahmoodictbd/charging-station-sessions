package com.unloadbrain.assignement.evbox.exception;

import org.springframework.validation.BindingResult;

public class ValidationException extends RuntimeException {

    public ValidationException(BindingResult bindingResult) {
        //super(message);
    }
}
