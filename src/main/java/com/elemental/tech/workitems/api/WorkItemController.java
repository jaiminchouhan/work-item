package com.elemental.tech.workitems.api;

import com.elemental.tech.workitems.api.dto.CreateWorkItemRequest;
import com.elemental.tech.workitems.api.dto.UpdateWorkItemStatusRequest;
import com.elemental.tech.workitems.api.dto.WorkItemResponse;
import com.elemental.tech.workitems.service.WorkItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
    TODO: please enhance
 */
@RestController
@RequestMapping("/api/work-items")
public class WorkItemController {

    private final WorkItemService service;

    public WorkItemController(WorkItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkItemResponse create (@Valid @RequestBody CreateWorkItemRequest request) {
        return service.create(request);
    }

    @GetMapping
    public List<WorkItemResponse> list() {
        return service.list();
    }

    @GetMapping("/{id}")
    public WorkItemResponse get(@PathVariable long id) {
        return service.get(id);
    }

    @PatchMapping("/{id}/status")
    public WorkItemResponse updateStatus(
            @PathVariable long id,
            @Valid @RequestBody UpdateWorkItemStatusRequest request
    ) {
        return service.updateStatus(id, request.status());
    }
}
