package com.hackudc.poustfit_server.remote.inditex;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zara")
public class InditexController {

    private final InditexService inditexService;

    public InditexController(InditexService inditexService) {
        this.inditexService = inditexService;
    }

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam String imageUrl) {
        // Llamar al servicio de Zara con la URL de la imagen
        return inditexService.getZaraRecommendations(imageUrl);
    }
}
