package com.example.Triple_clone.domain.entity;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn()
@RequiredArgsConstructor
@MappedSuperclass
public class Place extends DetailPlan {
}

