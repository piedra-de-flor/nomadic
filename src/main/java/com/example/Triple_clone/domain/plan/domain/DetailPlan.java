package com.example.Triple_clone.domain.plan.domain;

import com.example.Triple_clone.domain.plan.web.dto.DetailPlanDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn()
@RequiredArgsConstructor
@Entity
public class DetailPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    private Location location;
    private Date date;
    private String time;

    @Version
    private Long version;
    private Long lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private Long createdBy;
    private LocalDateTime createdAt;

    @Builder
    public DetailPlan(Plan plan, Location location, Date date, String time, Long createdBy) {
        this.plan = plan;
        this.location = location;
        this.date = date;
        this.time = time;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.lastModifiedBy = createdBy;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public void update(Location location, Date date, String time, Long modifiedBy) {
        this.location = location;
        this.date = date;
        this.time = time;
        this.lastModifiedBy = modifiedBy;
        this.lastModifiedAt = LocalDateTime.now();
    }

    public DetailPlanDto toDto() {
        return new DetailPlanDto(
                this.plan.getId(),
                this.location,
                this.date,
                this.time,
                this.version,
                this.lastModifiedBy,
                this.lastModifiedAt
        );
    }

    public boolean isModifiedAfter(LocalDateTime checkTime) {
        return this.lastModifiedAt.isAfter(checkTime);
    }
}
