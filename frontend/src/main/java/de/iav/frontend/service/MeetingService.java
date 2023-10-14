package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.Meeting;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class MeetingService {
    private static MeetingService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final String BACKEND_SC_MEET = System.getenv("BACKEND_TOASTMASTER_URI") + "/meetings";

    public MeetingService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized MeetingService getInstance() {
        if (instance == null) {
            instance = new MeetingService();
        }
        return instance;
    }

    public List<Meeting> getAllMeetings() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_SC_MEET))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToMeetingList)
                .join();
    }

    private List<Meeting> mapToMeetingList(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to map MeetingList" + e.getMessage());
        }
    }
}