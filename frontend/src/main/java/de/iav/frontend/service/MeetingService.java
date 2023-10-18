package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.Meeting;

import java.io.IOException;
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

    private static final String APPLICATION_JSON = "application/json";
    private static final String JSESSIONID_IS_EQUAL ="JSESSIONID=";
    private static final String COOKIE = "Cookie";

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

    private Meeting mapToMeeting(String json) {
        try {
            return objectMapper.readValue(json, Meeting.class);
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to map Meeting" + e.getMessage());
        }
    }

    public Meeting createMeeting(Meeting meeting, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(meeting);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_SC_MEET))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToMeeting)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("--->Failed to save Meeting: " + e.getMessage());
        }
    }

    public Meeting getMeetingByDateTimeAndLocation(String dateTime, String location) throws IOException, InterruptedException {
        String url = BACKEND_SC_MEET + "/search?dateTime=" + dateTime + "&location=" + location;
        url = url.replace(" ", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response;
        response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String responseBody = response.body();

        if (statusCode == 200) {
             return mapToMeeting(responseBody);
        } else if (statusCode == 404) {
             return null;
         } else {
            throw new MappingRuntimeException("Fehler beim Abrufen des Meetings: " + responseBody);
        }
    }

    public Meeting updateMeeting(Meeting meeting, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(meeting);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_SC_MEET+ "/" + meeting.id()))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToMeeting)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("--->Failed to update Meeting: " + e.getMessage());
        }
    }
}