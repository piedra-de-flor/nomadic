package com.example.Triple_clone.domain.accommodation.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomUpdateDto {

    @Nullable
    @Size(max = 255, message = "객실명은 255자를 초과할 수 없습니다")
    private String name;

    @Nullable
    @Min(value = 0, message = "대실 가격은 0 이상이어야 합니다")
    private Integer dayusePrice;

    @Nullable
    @Min(value = 0, message = "대실 할인 가격은 0 이상이어야 합니다")
    private Integer dayuseSalePrice;

    @Nullable
    private Boolean hasDayuseDiscount;

    @Nullable
    private Boolean dayuseSoldout;

    @Nullable
    @Size(max = 50, message = "대실 시간은 50자를 초과할 수 없습니다")
    private String dayuseTime;

    @Nullable
    @Min(value = 0, message = "숙박 가격은 0 이상이어야 합니다")
    private Integer stayPrice;

    @Nullable
    @Min(value = 0, message = "숙박 할인 가격은 0 이상이어야 합니다")
    private Integer staySalePrice;

    @Nullable
    private Boolean hasStayDiscount;

    @Nullable
    private Boolean staySoldout;

    @Nullable
    @Size(max = 50, message = "체크인 시간은 50자를 초과할 수 없습니다")
    private String stayCheckinTime;

    @Nullable
    @Size(max = 50, message = "체크아웃 시간은 50자를 초과할 수 없습니다")
    private String stayCheckoutTime;

    @Nullable
    @Min(value = 1, message = "기본 수용 인원은 1명 이상이어야 합니다")
    private Integer capacity;

    @Nullable
    @Min(value = 1, message = "최대 수용 인원은 1명 이상이어야 합니다")
    private Integer maxCapacity;

    @AssertTrue(message = "기본 수용 인원은 최대 수용 인원보다 클 수 없습니다")
    public boolean isCapacityValid() {
        if (capacity == null || maxCapacity == null) {
            return true;
        }
        return capacity <= maxCapacity;
    }
}