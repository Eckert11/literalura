package com.aluracursos.literalura.service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsumoAPI {

    // 1. Crear el cliente HTTP (con configuración)
    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10)) // Timeout de 10 segundos
            .build();

    // 2. Metodo para obtener datos de la API
    public String obtenerDatos(String url) {
        // Crear la solicitud HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();

        try {
            // Enviar la solicitud y obtener la respuesta
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar el código de respuesta
            if (response.statusCode() == 200) {
                return response.body(); // Devolver el JSON
            } else {
                throw new RuntimeException("Error en la API. Código: " + response.statusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error al consumir la API: " + e.getMessage(), e);
        }
    }
}