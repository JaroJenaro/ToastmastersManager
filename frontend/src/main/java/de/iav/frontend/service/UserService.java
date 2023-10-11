package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.DeletingRuntimeException;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.User;
import javafx.application.Platform;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
public class UserService {
    private static UserService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final Logger LOG = LogManager.getLogger();
    private static final String BACKEND_USR_URL = System.getenv("BACKEND_TOASTMASTER_URI") + "/users";
    private static final String APPLICATION_JSON = "application/json";
    private static final String JSESSIONID_IS_EQUAL ="JSESSIONID=";
    private static final String COOKIE = "Cookie";

    public UserService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static synchronized UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> getAllUsers() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_USR_URL))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToUserList)
                .join();
    }

    private List<User> mapToUserList(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to map UserLIst" + e.getMessage());
        }
    }

    private User mapToUser(String json) {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
             throw new MappingRuntimeException("Failed to map User" + e.getMessage());
        }
    }

    public User getUserByEmail(String email) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_USR_URL + "/email/" + email))
                .build();

        User respondedUser = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToUser)
                .join();
        LOG.info("respondedUser: {}", respondedUser);
        return respondedUser;
    }

    public void deleteUser(String idToDelete, ListView<User> lvUsers, String sessionId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_USR_URL + "/" + idToDelete))
                .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                .DELETE()
                .build();

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> {
                    if (response.statusCode() == 204) {
                            Platform.runLater(() -> {
                            lvUsers.getItems().removeIf(timeSlot -> timeSlot.id().equals(idToDelete));
                            lvUsers.refresh();
                        });
                    } else {
                        throw new DeletingRuntimeException("Fehler beim Löschen des Users mit der id " + idToDelete);
                    }
                })
                .join();
    }

    public User updateUser(User user, String sessionId) {
        try {
            String requestBody = objectMapper.writeValueAsString(user);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_USR_URL + "/" + user.id()))
                    .header("Content-Type", APPLICATION_JSON)
                    .header("Accept", APPLICATION_JSON)
                    .header(COOKIE, JSESSIONID_IS_EQUAL + sessionId)
                    .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToUser)
                    .join();
        } catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to save User" + e.getMessage());
        }
    }
}