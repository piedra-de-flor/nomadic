package com.example.Triple_clone.domain.entity;

import com.example.Triple_clone.domain.vo.Partner;
import com.example.Triple_clone.domain.vo.Style;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String place;

    @Enumerated(EnumType.STRING)
    private Partner partner;

    @ElementCollection
    @CollectionTable(name = "plan_styles", joinColumns = @JoinColumn(name = "plan_id"))
    @Enumerated(EnumType.STRING)
    private List<Style> styles;

    private Date startDay;

    private Date endDay;

    @OneToMany(mappedBy = "plan")
    private List<DetailPlan> plans;

    @Builder
    public Plan(User user, String place, Partner partner, Date startDay, Date endDay, List<Style> styles) {
        this.user = user;
        this.place = place;
        this.partner = partner;
        this.startDay = startDay;
        this.endDay = endDay;
        this.styles = styles;
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
