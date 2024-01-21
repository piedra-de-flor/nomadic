package com.example.Triple_clone.web.controller.planning;

import com.example.Triple_clone.service.planning.DetailPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DetailPlanController {
    private final DetailPlanService service;
}
