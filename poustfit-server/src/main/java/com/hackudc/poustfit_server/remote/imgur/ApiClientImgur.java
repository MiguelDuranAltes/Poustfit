package com.hackudc.poustfit_server.remote.imgur;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hackudc.poustfit_server.config.MyProperties;
import com.hackudc.poustfit_server.exceptions.ModelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ApiClientImgur {
    private final RestTemplate restTemplate;
    @Autowired
    private MyProperties properties;

    public ApiClientImgur() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(MultipartFile file) throws ModelException {
        String uploadUrl = properties.getImgurUrl();

        // Creamos el body de la solicitud (con MultipartFile)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());

        // Agregar headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID " + properties.getImgurClientId());

        // Crear la solicitud HTTP
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar la solicitud POST
        System.out.println("Enviando solicitud POST a Imgur...");
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

