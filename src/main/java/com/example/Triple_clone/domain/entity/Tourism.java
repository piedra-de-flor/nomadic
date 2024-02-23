package com.example.Triple_clone.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("T")
public class Tourism extends Place {

}
