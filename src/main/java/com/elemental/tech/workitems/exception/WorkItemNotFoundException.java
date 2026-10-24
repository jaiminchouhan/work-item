package com.elemental.tech.workitems.exception;

public class WorkItemNotFoundException extends RuntimeException {
    public WorkItemNotFoundException(long id) {
        super("Work item not found: " + id);
    }
}