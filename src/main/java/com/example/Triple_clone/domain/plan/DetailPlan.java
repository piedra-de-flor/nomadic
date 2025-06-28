package com.example.Triple_clone.domain.plan;

import jakarta.persistence.*;
import lombok.*;

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

    @Builder
    public DetailPlan(Plan plan, Location location, Date date, String time) {
        this.plan = plan;
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public void update(Location location, Date date, String time) {
        this.location = location;
        this.date = date;
        this.time = time;
    }

    public DetailPlanDto toDto() {
        return new DetailPlanDto(
                this.plan.getId(),
                this.location,
                this.date,
                this.time
        );
    }
}
