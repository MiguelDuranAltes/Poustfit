package com.hackudc.poustfit_server.remote.imgur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackudc.poustfit_server.exceptions.ModelException;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Component
public class ApiClientImgur {
    private final RestTemplate restTemplate;
    private final String CLIENT_ID = "e02f1c1f47d8352";
    private final String IMGUR_UPLOAD_URL = "https://api.imgur.com/3/upload";

    public ApiClientImgur() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(MultipartFile file) throws ModelException {
        String uploadUrl = "https://api.imgur.com/3/upload";

        // Creamos el body de la solicitud (con MultipartFile)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        // Agregar headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID " + CLIENT_ID);

        // Crear la solicitud HTTP
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar la solicitud POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        // Procesamos la respuesta de la API para extraer la URL de la imagen
        String responseBody = responseEntity.getBody();

        // Aquí puedes usar un parser JSON (como Jackson) para extraer el link desde la respuesta
        // Supongamos que la respuesta es similar a la que mencionaste anteriormente, extraemos la URL
        // (Este es un ejemplo básico, para un manejo adecuado deberías parsear el JSON)

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Extraer el link de la respuesta (en un caso real se debería hacer parsing del JSON)
            String imageUrl = extractImageUrlFromResponse(responseBody);
            return imageUrl;
        } else {
            // Manejar el error, puedes lanzar una excepción aquí
            throw new RuntimeException("Error al subir la imagen a Imgur");
        }
    }

    private String extractImageUrlFromResponse(String response) throws ModelException {
        // Este método debe hacer parsing del JSON para extraer la URL de la imagen
        // Un ejemplo de cómo podrías hacerlo con Jackson sería:
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("data").path("link").asText();
        } catch (JsonProcessingException e) {
            throw new ModelException("Error al procesar la respuesta de Imgur");
        }


        // Para fines demostrativos, asumiendo que "link" está en una posición específica:
    }
}

