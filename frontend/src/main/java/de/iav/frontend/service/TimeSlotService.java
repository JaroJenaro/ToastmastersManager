package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.iav.frontend.exception.MappingRuntimeException;
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
            throw new MappingRuntimeException("Failed to map TimeSlotList" + e.getMessage());
        }

    }

    public TimeSlot saveTimeSlot(TimeSlot timeSlot) {
        try {
            String requestBody = objectMapper.writeValueAsString(timeSlot);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToTimeSlot)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to save TimeSlot" + e.getMessage());
        }
    }

    private TimeSlot mapToTimeSlot(String json) {
        try {
            return objectMapper.readValue(json, TimeSlot.class);
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to map TimeSlot" + e.getMessage());
        }
    }

}
