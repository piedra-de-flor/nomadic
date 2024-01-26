package com.example.Triple_clone.web.controller;

import com.example.Triple_clone.domain.entity.DetailPlan;
import com.example.Triple_clone.domain.vo.Location;
import com.example.Triple_clone.service.planning.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class GoogleMapController {
    private final PlanService service;
    @GetMapping("/showMap")
    public String index(@RequestParam(name = "planId") Long planId,
                        @RequestParam(name = "name") String name,
                        @RequestParam(name = "latitude") Double latitude,
                        @RequestParam(name = "longitude") Double longitude,
                        Model model) {
        List<DetailPlan> plans = service.getPlans(planId);
        List<Location> locations = new ArrayList<>();

        for (int i = 0; i < plans.size(); i++) {
            locations.add(plans.get(i).getLocation());
        }
        locations.add(new Location(latitude, longitude, name));

        model.addAttribute("locations", locations);
        return "map";
    }
}
