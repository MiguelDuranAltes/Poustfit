package com.hackudc.poustfit_server.remote.imgbb;

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
public class ApiClientImgbb {

    private final RestTemplate restTemplate;
    @Autowired
    private MyProperties properties;

    public ApiClientImgbb() {
        this.restTemplate = new RestTemplate();
    }

    public String uploadImage(MultipartFile file) throws ModelException {
        // Endpoint de ImgBB para cargar imágenes
        String uploadUrl = properties.getImgbbUrl();

        // Creamos el cuerpo de la solicitud (con MultipartFile)
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", file.getResource());
        body.add("key", properties.getImgbbKey());  // Tu API Key de ImgBB

        // Agregar los headers (la autenticación se maneja a través de la API Key en el body)
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Crear la solicitud HTTP
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Enviar la solicitud POST
        System.out.println("Enviando solicitud POST a ImgBB...");
        ResponseEntity<String> responseEntity = restTemplate.exchange(uploadUrl, HttpMethod.POST, requestEntity, String.class);

        // Procesamos la respuesta de la API para extraer la URL de la imagen
        String responseBody = responseEntity.getBody();

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            // Extraer el link de la respuesta
            String imageUrl = extractImageUrlFromResponse(responseBody);
            return imageUrl;
        } else {
            // Manejar el error, puedes lanzar una excepción aquí
            throw new RuntimeException("Error al subir la imagen a ImgBB");
        }
    }

    private String extractImageUrlFromResponse(String response) throws ModelException {
        // Este método debe hacer parsing del JSON para extraer la URL de la imagen
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(response);
            return rootNode.path("data").path("url").asText();  // ImgBB devuelve la URL de la imagen en "data.url"
        } catch (JsonProcessingException e) {
            throw new ModelException("Error al procesar la respuesta de ImgBB");
        }
    }
}
