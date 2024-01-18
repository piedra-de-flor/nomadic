package com.example.Triple_clone.service.planning;

import com.example.Triple_clone.dto.planning.PlanCreateDto;
import com.example.Triple_clone.dto.planning.PlanDto;
import org.springframework.stereotype.Service;

@Service
public class PlanService {
    public PlanDto createPlan(PlanCreateDto createDto) {
        //todo
        //이걸 트랜잭션 단위로 잡고
        //- 여행지 선택
        //- 여행 스타일
        //- 여행 날짜 선택
        //- 예약
        //을 모두 처리해야 함
        return new PlanDto();
    }
}
