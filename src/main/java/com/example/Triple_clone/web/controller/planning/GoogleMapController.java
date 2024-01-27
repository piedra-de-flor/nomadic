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
    @GetMapping("/showMap")
    public String index(@RequestParam(name = "planId") Long planId,
                        @RequestParam(name = "name") String name,
                        @RequestParam(name = "latitude") Double latitude,
                        @RequestParam(name = "longitude") Double longitude,
                        Model model) {

        model.addAttribute("locations", service.getLocations(
                planId,
                name,
                latitude,
                longitude));
        return "map";
    }
}
