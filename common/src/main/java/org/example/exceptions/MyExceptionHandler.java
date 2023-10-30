package org.example.exceptions;

import org.example.response.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MyExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.internalError());
    }

    @ExceptionHandler(MyException.class)
    public ResponseEntity<Object> handleMyException(MyException e) {
        e.printStackTrace();
        return ResponseEntity.status(e.getHttpStatus()).body(e.toCommonResponse());
    }

    @ExceptionHandler(UnsupportedEnumValueException.class)
    public ResponseEntity<Object> handleUnsupportedEnumValueException(UnsupportedEnumValueException e) {
        e.printStackTrace();
        return new ResponseEntity<>(e.toCommonResponse(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handleBindException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder errorMessage = new StringBuilder();

        for (ObjectError objectError : bindingResult.getAllErrors()) {
            if (objectError instanceof FieldError fieldError) {
                errorMessage.append(fieldError.getField())
                        .append(": ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
            } else {
                errorMessage.append(objectError.getDefaultMessage())
                        .append("; ");
            }
        }
        return new ResponseEntity<>(CommonResponse.badRequest(errorMessage.toString()), HttpStatus.BAD_REQUEST);
    }
}
