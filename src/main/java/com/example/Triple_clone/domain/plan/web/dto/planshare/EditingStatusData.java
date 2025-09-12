package com.example.Triple_clone.domain.plan.web.dto.planshare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditingStatusData {
    private Long detailPlanId;
    private String userId;
    private String userName;
    private boolean isEditing;
}