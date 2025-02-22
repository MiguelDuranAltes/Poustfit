package com.hackudc.poustfit_server.remote.imgur;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

public class ApiClientImgur {
    private final RestTemplate restTemplate;
    private final String CLIENT_ID = "e02f1c1f47d8352";
    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";

    public ApiClientImgur() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(String imagePath) {
        try {
            // Leer la imagen y convertirla a Base64
            byte[] imageBytes = Files.readAllBytes(Path.of(imagePath));
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Client-ID " + CLIENT_ID);

            // Crear el body en JSON
            String requestBody = "{\"image\": \"" + base64Image + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            // Hacer la petición POST y recibir la respuesta en un objeto ImgurResponse
            ResponseEntity<ImgurResponse> response = restTemplate.exchange(
                    IMGUR_UPLOAD_URL, HttpMethod.POST, requestEntity, ImgurResponse.class
            );

            // Extraer la URL de la imagen
            return response.getBody() != null ? response.getBody().getData().getLink() : "Error: No se recibió respuesta";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error al subir la imagen a Imgur";
        }
    }
}

