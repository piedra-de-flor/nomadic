package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.service.planning.PlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
@Tag(name = "구글맵 Controller", description = "GOOGLE MAP API")
public class GoogleMapController {
    private final PlanService service;

    @Operation(summary = "Google Map에 마커 추가", description = "구글 지도에 마커를 추가합니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/addMap")
    public String addMap(
            @Parameter(description = "마커를 추가할 계획 ID", required = true)
            @RequestParam(name = "planId") Long planId,
            @Parameter(description = "추가할 마커의 이름", required = true)
            @RequestParam(name = "name") String name,
            @Parameter(description = "추가할 마커의 위도", required = true)
            @RequestParam(name = "latitude") Double latitude,
            @Parameter(description = "추가할 마커의 경도", required = true)
            @RequestParam(name = "longitude") Double longitude,
            Model model) {

        model.addAttribute("locations", service.addLocation(
                planId,
                name,
                latitude,
                longitude));
        return "map";
    }

    @Operation(summary = "Google Map 조회", description = "기존의 구글 지도를 불러옵니다")
    @ApiResponse(responseCode = "200", description = "성공")
    @ApiResponse(responseCode = "400", description = "잘못된 요청 형식입니다")
    @ApiResponse(responseCode = "500", description = "내부 서버 오류 발생")
    @GetMapping("/showMap")
    public String showMap(
            @Parameter(description = "조회할 지도를 포함하고 있는 Plan의 ID", required = true)
            @RequestParam(name = "planId") Long planId,
            Model model) {
        model.addAttribute("locations", service.getLocation(planId));
        return "map";
    }
}
