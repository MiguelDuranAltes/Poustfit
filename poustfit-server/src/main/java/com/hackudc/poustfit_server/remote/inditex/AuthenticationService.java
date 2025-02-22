package com.hackudc.poustfit_server.remote.inditex;

import com.hackudc.poustfit_server.config.MyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthenticationService {

    @Autowired
    private MyProperties properties;

    private final RestTemplate restTemplate = new RestTemplate();
    private final String AUTH_URL = properties.getAuthUrl();  // URL de autenticación

    public String getAuthToken() {
        // Crear el cuerpo de la petición con MultiValueMap para datos de formulario
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "client_credentials");
        requestBody.add("scope", "technology.catalog.read");

        // Configurar los headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(properties.getInditextClient(), properties.getInditexPassword());

        // Crear la solicitud
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

        // Hacer el POST para obtener el token
        ResponseEntity<Map> response = restTemplate.exchange(AUTH_URL, HttpMethod.POST, request, Map.class);
        System.out.println("Response: " + response.getBody()); // Imprimir la respuesta para depuración

        // Verificar la respuesta
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Cambiar a "id_token" para obtener el token correcto
            Object tokenObject = response.getBody().get("id_token");
            if (tokenObject != null) {
                return tokenObject.toString(); // Devolver el token como String
            } else {
                throw new RuntimeException("Token no encontrado en la respuesta");
            }
        }
        throw new RuntimeException("Error obteniendo el token");
    }

}
