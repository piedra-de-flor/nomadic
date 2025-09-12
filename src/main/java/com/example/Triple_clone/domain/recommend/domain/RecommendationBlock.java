package com.example.Triple_clone.domain.recommend.domain;

import com.example.Triple_clone.common.file.Image;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "recommendation_block",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_block_order",
                        columnNames = {"recommendation_id", "order_index"})
        },
        indexes = {
                @Index(name = "idx_block_reco", columnList = "recommendation_id"),
                @Index(name = "idx_block_order", columnList = "order_index")
        })
public class RecommendationBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)                
    @JoinColumn(name = "recommendation_id", nullable = false)
    @JsonBackReference
    private Recommendation recommendation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BlockType type;                            

    @Column(name = "order_index", nullable = false)
    private int orderIndex;                            

    // TEXT 전용
    @Lob
    private String text;

    // IMAGE 전용
    @Embedded
    private Image image;

    private String caption;

    public void update(BlockType type, String text, Image image, String caption, int orderIndex) {
        this.type = type;
        this.text = text;
        this.image = image;
        this.caption = caption;
        this.orderIndex = orderIndex;
    }
}
