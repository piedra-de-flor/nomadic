package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.service.planning.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class GoogleMapController {
    private final PlanService service;

    @GetMapping("/addMap")
    public String addMap(@RequestParam(name = "planId") Long planId,
                         @RequestParam(name = "name") String name,
                         @RequestParam(name = "latitude") Double latitude,
                         @RequestParam(name = "longitude") Double longitude,
                         Model model) {
        model.addAttribute("locations", service.addLocation(
                planId,
                name,
                latitude,
                longitude));
        return "map";
    }

    @GetMapping("/showMap")
    public String showMap(@RequestParam(name = "planId") Long planId, Model model) {
        model.addAttribute("locations", service.getLocation(planId));
        return "map";
    }
}
