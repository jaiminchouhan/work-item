package com.elemental.tech.workitems.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@Entity
@Table(name = "work_item")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(onlyExplicitlyIncluded = true)
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    @Version
    private Long version;

    @Column(nullable = false, length = 120)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Priority priority;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public WorkItem(String title, String description, Priority priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.status = Status.OPEN;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public void setTitle(String title) {
        this.title = title;
        touch();
    }

    public void setDescription(String description) {
        this.description = description;
        touch();
    }

    public void transitionTo(Status newStatus) {
        if (!isTransitionAllowed(this.status, newStatus)) {
            throw new IllegalStateException(
                    "Invalid transition from " + this.status + " to " + newStatus
            );
        }

        this.status = newStatus;
        touch();
    }

    private boolean isTransitionAllowed(Status current, Status target) {
        return switch (current) {
            case OPEN -> target == Status.IN_PROGRESS || target == Status.CANCELLED;
            case IN_PROGRESS -> target == Status.DONE || target == Status.CANCELLED;
            case DONE, CANCELLED -> false;
        };
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    public enum Status {
        OPEN,
        IN_PROGRESS,
        DONE,
        CANCELLED
    }

    public enum Priority {
        LOW,
        MEDIUM,
        HIGH
    }
}