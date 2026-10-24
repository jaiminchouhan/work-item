package com.elemental.tech.workitems.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.elemental.tech.workitems.domain.WorkItem;

import java.time.Instant;

import static com.elemental.tech.workitems.domain.WorkItem.*;

public interface WorkItemRepository extends JpaRepository<WorkItem, Long> {

    Page<WorkItem> findByStatus(Status status, Pageable pageable);

    Page<WorkItem> findByPriority(Priority priority, Pageable pageable);

    Page<WorkItem> findByStatusAndPriority(
            Status status,
            Priority priority,
            Pageable pageable
    );

    Page<WorkItem> findByTitleContainingIgnoreCase(
            String keyword,
            Pageable pageable
    );

    Page<WorkItem> findByCreatedAtBetween(
            Instant from,
            Instant to,
            Pageable pageable
    );
}