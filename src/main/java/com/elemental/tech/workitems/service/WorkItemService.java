package com.elemental.tech.workitems.service;

import com.elemental.tech.workitems.api.dto.CreateWorkItemRequest;
import com.elemental.tech.workitems.api.dto.WorkItemResponse;
import com.elemental.tech.workitems.domain.WorkItem;
import com.elemental.tech.workitems.exception.WorkItemNotFoundException;
import com.elemental.tech.workitems.persistence.WorkItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WorkItemService {

    private final WorkItemRepository repository;

    public WorkItemService(WorkItemRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public WorkItemResponse create(CreateWorkItemRequest request) {
        WorkItem workItem = new WorkItem(
                request.title(),
                request.description(),
                WorkItem.Priority.valueOf(request.priority().name())
        );

        WorkItem saved = repository.save(workItem);
        return WorkItemMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkItemResponse> list() {
        return repository.findAll()
                .stream()
                .map(WorkItemMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkItemResponse get(long id) {
        WorkItem workItem = repository.findById(id)
                .orElseThrow(() -> new WorkItemNotFoundException(id));
        return WorkItemMapper.toResponse(workItem);
    }

    public WorkItemResponse updateStatus(long id, WorkItem.Status newStatus) {

        WorkItem workItem = repository.findById(id)
                .orElseThrow(() -> new WorkItemNotFoundException(id));

        workItem.transitionTo(newStatus);

        WorkItem saved = repository.save(workItem);

        return WorkItemMapper.toResponse(saved);
    }
}
