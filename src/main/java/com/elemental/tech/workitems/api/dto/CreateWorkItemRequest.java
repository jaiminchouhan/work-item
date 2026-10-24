package com.elemental.tech.workitems.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateWorkItemRequest(

        @NotBlank(message = "Title must not be blank")
        @Size(max = 120, message = "Title must not exceed 120 characters")
        String title,

        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description,

        @NotNull(message = "Priority must be provided")
        Priority priority

) {

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}
