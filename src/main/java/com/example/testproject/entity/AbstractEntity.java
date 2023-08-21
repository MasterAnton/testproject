package com.example.testproject.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;
@MappedSuperclass
@Getter
@Setter
public class AbstractEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "created_at")
    @CreationTimestamp
    protected Instant createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    protected Instant updatedAt;

    @Column(name = "deleted")
    protected boolean deleted = false;
}
