package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan {
    @Id
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String place;
    private Partner partner;
    private List<Style> styles;
    private Date startDay;
    private Date endDay;
    @OneToMany(mappedBy = "user")
    private List<DetailPlan> plans;

    @Builder
    public Plan(long userId, String place, Partner partner, Date startDay, Date endDay, List<Style> styles) {
        Plan.builder()
                .place(place)
                .userId(userId)
                .partner(partner)
                .styles(styles)
                .startDay(startDay)
                .endDay(endDay)
                .build();
    }

    public void choosePartner(Partner partner) {
        this.partner = partner;
    }

    public void chooseStyle(List<Style> styles) {
        this.styles = styles;
    }

    public boolean isMine(long userId) {
        return this.user.getId() == userId;
    }
}
