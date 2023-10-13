package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.SpeechContribution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class SpeechContributionService {
    private static SpeechContributionService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOG = LogManager.getLogger();
    private static final String BACKEND_SC_URL = System.getenv("BACKEND_TOASTMASTER_URI") + "/speech-contributions";
    private static final String APPLICATION_JSON = "application/json";
    private static final String JSESSIONID_IS_EQUAL ="JSESSIONID=";
    private static final String COOKIE = "Cookie";

    public SpeechContributionService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized SpeechContributionService getInstance() {
        if (instance == null) {
            instance = new SpeechContributionService();
        }
        return instance;
    }

    public List<SpeechContribution> getAllSpeechContributions() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_SC_URL))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToSpeechContributionList)
                .join();
    }

    private List<SpeechContribution> mapToSpeechContributionList(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to map SpeechContributionList" + e.getMessage());
        }
    }

    private SpeechContribution mapToSpeechContribution(String json) {
        try {
            return objectMapper.readValue(json, SpeechContribution.class);
        } catch (JsonProcessingException e) {
             throw new MappingRuntimeException("Failed to map SpeechContribution" + e.getMessage());
        }
    }

    public SpeechContribution getSpeechContributionById(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_SC_URL + "/" + id))
                .build();

        SpeechContribution respondedUser = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToSpeechContribution)
                .join();
        LOG.info("respondedUser: {}", respondedUser);
        return respondedUser;
    }
    public SpeechContribution createSpeechContribution(SpeechContribution speechContribution, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(speechContribution);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_SC_URL))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToSpeechContribution)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("--->Failed to save SpeechContribution: " + e.getMessage());
        }
    }
}