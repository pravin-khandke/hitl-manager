package com.hitl.manager.adapter.web;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.net.URI;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        pd.setType(URI.create("urn:hitl:bad-request"));
        return pd;
    }

    @ExceptionHandler(IllegalStateException.class)
    ProblemDetail handleConflict(IllegalStateException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(CONFLICT, ex.getMessage());
        pd.setType(URI.create("urn:hitl:conflict"));
        return pd;
    }
}
