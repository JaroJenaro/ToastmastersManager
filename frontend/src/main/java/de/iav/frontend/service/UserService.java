package de.iav.frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.model.User;
import de.iav.frontend.model.UserWithoutIdDto;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
public class UserService {
    private static UserService instance;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private static final String BACKEND_AUTH_URL = "http://localhost:8080/api/toastMasterManager/usersData";
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

    //wird später benötigt zur Darstellung alle User für den Admin
    public List<User> getAllUsers() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_AUTH_URL))
                .build();
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToUserList) // .thenApply(responseBody -> mapToStudent(responseBody))
                .join();
    }

    public User addUser(UserWithoutIdDto userWithoutIdDto) {
        try {
            String requestBody = objectMapper.writeValueAsString(userWithoutIdDto);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();
            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::mapToUser)
                    .join();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<User> mapToUserList(String responseBody) {
        try {
            return objectMapper.readValue(responseBody, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map users List", e);
        }
    }

    private User mapToUser(String json) {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map User", e);
        }
    }


    public User getUserByEmail(String email) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(BACKEND_AUTH_URL + "/email/" + email))
                .build();

        User respondedUser = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(this::mapToUser) // .thenApply(responseBody -> mapToStudent(responseBody))
                .join();
        System.out.println("respondedUser: " + respondedUser);
        return respondedUser;
    }
}