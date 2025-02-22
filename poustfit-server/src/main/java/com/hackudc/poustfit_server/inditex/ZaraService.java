package com.hackudc.poustfit_server.inditex;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ZaraService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final AuthenticationService authenticationService; // Inyectamos el servicio de autenticación
    private final String ZARA_API_URL = "https://api.inditex.com/pubvsearch"; // URL real de la API

    public ZaraService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public String getZaraRecommendations(String imageUrl) {
        // 1. Obtener el token llamando a la API intermedia
        String token = authenticationService.getAuthToken();

        // 2. Crear headers con el token
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Agrega el token en el header Authorization: Bearer {token}
        headers.setContentType(MediaType.APPLICATION_JSON); // Asegúrate de que el tipo de contenido es JSON

        // 3. Construir la URL con la imagen
        String urlWithImage = ZARA_API_URL + "?image=" + imageUrl;

        // 4. Hacer la petición GET con el token
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(urlWithImage, HttpMethod.GET, entity, String.class);

        // 5. Retornar la respuesta JSON de Zara
        return response.getBody();
    }

}
