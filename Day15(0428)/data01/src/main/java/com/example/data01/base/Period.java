package com.example.data01.base;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class Period {
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    @PrePersist
    public void onPrePersist() {
        createdDate = LocalDateTime.now();
        modifiedDate = LocalDateTime.now();
    }

    @PreUpdate
    public void onPreUpdate() {
        modifiedDate = LocalDateTime.now();
    }
}
