package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Room;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {
    private Long id;
    private String name;
    private Integer dayusePrice;
    private Integer dayuseSalePrice;
    private Boolean hasDayuseDiscount;
    private Boolean dayuseSoldout;
    private String dayuseTime;
    private Integer stayPrice;
    private Integer staySalePrice;
    private Boolean hasStayDiscount;
    private Boolean staySoldout;
    private String stayCheckinTime;
    private String stayCheckoutTime;
    private Integer capacity;
    private Integer maxCapacity;

    public static RoomDto from(Room room) {
        return RoomDto.builder()
                .id(room.getId())
                .name(room.getName())
                .dayusePrice(room.getDayusePrice())
                .dayuseSalePrice(room.getDayuseSalePrice())
                .hasDayuseDiscount(room.getHasDayuseDiscount())
                .dayuseSoldout(room.getDayuseSoldout())
                .dayuseTime(room.getDayuseTime())
                .stayPrice(room.getStayPrice())
                .staySalePrice(room.getStaySalePrice())
                .hasStayDiscount(room.getHasStayDiscount())
                .staySoldout(room.getStaySoldout())
                .stayCheckinTime(room.getStayCheckinTime())
                .stayCheckoutTime(room.getStayCheckoutTime())
                .capacity(room.getCapacity())
                .maxCapacity(room.getMaxCapacity())
                .build();
    }

    public Room toEntity() {
        return Room.builder()
                .name(this.name)
                .dayusePrice(this.dayusePrice)
                .dayuseSalePrice(this.dayuseSalePrice)
                .hasDayuseDiscount(this.hasDayuseDiscount)
                .dayuseSoldout(this.dayuseSoldout)
                .dayuseTime(this.dayuseTime)
                .stayPrice(this.stayPrice)
                .staySalePrice(this.staySalePrice)
                .hasStayDiscount(this.hasStayDiscount)
                .staySoldout(this.staySoldout)
                .stayCheckinTime(this.stayCheckinTime)
                .stayCheckoutTime(this.stayCheckoutTime)
                .capacity(this.capacity)
                .maxCapacity(this.maxCapacity)
                .build();
    }
}