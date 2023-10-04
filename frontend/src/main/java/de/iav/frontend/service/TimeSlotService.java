package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.iav.frontend.exception.DeletingRuntimeException;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.TimeSlot;
import javafx.application.Platform;
import javafx.scene.control.ListView;

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
    private static final String APPLICATION_JSON = "application/json";
    private static final String JSESSIONID_IS_EQUAL ="JSESSIONID=";
    private static final String COOKIE = "Cookie";
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

    public TimeSlot createTimeSlot(TimeSlot timeSlot, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(timeSlot);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToTimeSlot)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("--->Failed to save TimeSlot: " + e.getMessage());
        }
    }

    public TimeSlot updateTimeSlot(TimeSlot timeSlot, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(timeSlot);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL + "/" + timeSlot.id()))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL+ sessionId)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
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

    public void deleteTimeSlot(String idToDelete, ListView<TimeSlot> listView, String sessionId ) {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/" + idToDelete))
                .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                .DELETE()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 204) {
                        Platform.runLater(() -> {
                            listView.getItems().removeIf(timeSlot -> timeSlot.id().equals(idToDelete));
                            listView.refresh();
                        });
                    } else {
                        throw new DeletingRuntimeException("Fehler beim Löschen des TimeSlots mit der id " + idToDelete);
                    }
                })
                .join();
    }
}
