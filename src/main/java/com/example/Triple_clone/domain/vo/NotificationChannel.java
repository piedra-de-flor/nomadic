package com.example.Triple_clone.domain.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Embeddable
public class NotificationChannel {

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<NotificationChannelType> types = new HashSet<>();

    protected NotificationChannel() {
    }

    public NotificationChannel(Set<NotificationChannelType> types) {
        this.types = types;
    }

    public boolean includes(NotificationChannelType type) {
        return types.contains(type);
    }
}
