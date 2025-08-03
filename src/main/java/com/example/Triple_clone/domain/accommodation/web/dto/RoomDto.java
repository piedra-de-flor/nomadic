package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Room;
import com.example.Triple_clone.domain.accommodation.domain.RoomDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDto {
    private long id;
    private String name;
    private Integer dayusePrice;
    private String dayuseTime;
    private String stayCheckinTime;
    private String stayCheckoutTime;
    private Integer stayPrice;
    private Integer capacity;
    private Integer maxCapacity;

    public static RoomDto fromDocument(RoomDocument doc) {
        if (doc == null) return null;
        return RoomDto.builder()
                .id(doc.getId())
                .name(doc.getName())
                .dayusePrice(doc.getDayusePrice())
                .dayuseTime(doc.getDayuseTime())
                .stayCheckinTime(doc.getStayCheckinTime())
                .stayCheckoutTime(doc.getStayCheckoutTime())
                .stayPrice(doc.getStayPrice())
                .capacity(doc.getCapacity())
                .maxCapacity(doc.getMaxCapacity())
                .build();
    }

    public static RoomDto fromEntity(Room entity) {
        return RoomDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .dayusePrice(entity.getDayusePrice())
                .dayuseTime(entity.getDayuseTime())
                .stayCheckinTime(entity.getStayCheckinTime())
                .stayCheckoutTime(entity.getStayCheckoutTime())
                .stayPrice(entity.getStayPrice())
                .capacity(entity.getCapacity())
                .maxCapacity(entity.getMaxCapacity())
                .build();
    }
}
