package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.iav.frontend.model.TimeSlot;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class TimeSlotService {
    private static TimeSlotService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BACKEND_AUTH_URL = System.getenv("BACKEND_TOASTMASTER_URI") + "/timeslots";
    public TimeSlotService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized TimeSlotService getInstance() {
        if (instance == null) {
            instance = new TimeSlotService();
        }
        return instance;
    }

    public List<TimeSlot> getAllTimeSlots() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_AUTH_URL))
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToTimeSlotList) // .thenApply(responseBody -> mapToStudent(responseBody))
                .join();
    }

    private List<TimeSlot> mapToTimeSlotList(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map stocks List", e);
        }

    }


}
