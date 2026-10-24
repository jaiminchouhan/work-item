package com.elemental.tech.workitems.api.dto;

import com.elemental.tech.workitems.domain.WorkItem.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateWorkItemStatusRequest(

        @NotNull
        Status status,

        @NotNull
        @Positive
        Long version
) {}