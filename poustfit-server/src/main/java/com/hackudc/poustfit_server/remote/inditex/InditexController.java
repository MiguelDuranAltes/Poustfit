package com.hackudc.poustfit_server.remote.inditex;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inditex")
public class InditexController {

    private final InditexService inditexService;

    @Autowired
    public InditexController(InditexService inditexService) {
        this.inditexService = inditexService;
    }

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam String imageUrl) {
        return inditexService.getInditexRecommendations(imageUrl);
    }
}
