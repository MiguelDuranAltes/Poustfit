package com.hackudc.poustfit_server.inditex;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/zara")
public class ZaraController {

    private final ZaraService zaraService;

    public ZaraController(ZaraService zaraService) {
        this.zaraService = zaraService;
    }

    @GetMapping("/recommendations")
    public String getRecommendations(@RequestParam String imageUrl) {
        // Llamar al servicio de Zara con la URL de la imagen
        return zaraService.getZaraRecommendations(imageUrl);
    }
}
