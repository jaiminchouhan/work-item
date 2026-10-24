package com.elemental.tech.workitems.exception;

import com.elemental.tech.workitems.domain.WorkItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(WorkItemNotFoundException.class)
    public ProblemDetail handleNotFound(WorkItemNotFoundException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Work item not found");
        problem.setDetail(ex.getMessage());

        return problem;
    }

    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleInvalidTransition(IllegalStateException ex) {

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Invalid status transition");
        problem.setDetail(ex.getMessage());

        return problem;
    }
}