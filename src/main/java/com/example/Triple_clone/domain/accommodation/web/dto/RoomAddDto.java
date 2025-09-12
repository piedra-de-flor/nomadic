package com.example.Triple_clone.domain.accommodation.web.dto;

import com.example.Triple_clone.domain.accommodation.domain.Accommodation;
import com.example.Triple_clone.domain.accommodation.domain.Room;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomAddDto {

    @NotBlank(message = "객실명은 필수입니다")
    @Size(max = 255, message = "객실명은 255자를 초과할 수 없습니다")
    private String name;

    @Min(value = 0, message = "대실 가격은 0 이상이어야 합니다")
    private Integer dayusePrice;

    @Min(value = 0, message = "대실 할인 가격은 0 이상이어야 합니다")
    private Integer dayuseSalePrice;

    private Boolean hasDayuseDiscount;
    private Boolean dayuseSoldout;

    @Size(max = 50, message = "대실 시간은 50자를 초과할 수 없습니다")
    private String dayuseTime;

    @Min(value = 0, message = "숙박 가격은 0 이상이어야 합니다")
    private Integer stayPrice;

    @Min(value = 0, message = "숙박 할인 가격은 0 이상이어야 합니다")
    private Integer staySalePrice;

    private Boolean hasStayDiscount;
    private Boolean staySoldout;

    @Size(max = 50, message = "체크인 시간은 50자를 초과할 수 없습니다")
    private String stayCheckinTime;

    @Size(max = 50, message = "체크아웃 시간은 50자를 초과할 수 없습니다")
    private String stayCheckoutTime;

    @Min(value = 1, message = "기본 수용 인원은 1명 이상이어야 합니다")
    private Integer capacity;

    @Min(value = 1, message = "최대 수용 인원은 1명 이상이어야 합니다")
    private Integer maxCapacity;

    public Room toEntity(Accommodation accommodation) {
        return Room.builder()
                .accommodation(accommodation)
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

    @AssertTrue(message = "기본 수용 인원은 최대 수용 인원보다 클 수 없습니다")
    public boolean isCapacityValid() {
        if (capacity == null || maxCapacity == null) {
            return true;
        }
        return capacity <= maxCapacity;
    }
}