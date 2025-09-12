package com.example.Triple_clone.domain.recommend.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TestBlockDto {
    private String type;
    private String text;
    private String orderIndex;
    
    @Override
    public String toString() {
        return "TestBlockDto{" +
                "type='" + type + '\'' +
                ", text='" + text + '\'' +
                ", orderIndex='" + orderIndex + '\'' +
                '}';
    }
}
