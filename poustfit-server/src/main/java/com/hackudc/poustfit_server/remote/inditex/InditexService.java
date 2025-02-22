package com.hackudc.poustfit_server.remote.inditex;

import com.hackudc.poustfit_server.config.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class InditexService {
    private final MyProperties properties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService; // Inyectamos el servicio de autenticación

    @Autowired
    public InditexService(AuthenticationService authenticationService, MyProperties properties) {
        this.authenticationService = authenticationService;
        this.properties = properties;
    }

    public String getZaraRecommendations(String imageUrl) {
        // 1. Obtener el token llamando a la API intermedia
        String token = authenticationService.getAuthToken();

        // 2. Crear headers con el token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Agrega el token en el header Authorization: Bearer {token}
        headers.setContentType(MediaType.APPLICATION_JSON); // Asegúrate de que el tipo de contenido es JSON

        // 3. Construir la URL con la imagen
        String urlWithImage = properties.getInditexApiUrl() + "?image=" + imageUrl;

        // 4. Hacer la petición GET con el token
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(urlWithImage, HttpMethod.GET, entity, String.class);

        // 5. Retornar la respuesta JSON de Zara
        return response.getBody();
    }

}
