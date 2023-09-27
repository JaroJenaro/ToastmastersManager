package de.iav.frontend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.model.User;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;


public class AuthService {

    private String email;
    private String sessionId;
    private String errorMessage;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }


    private static AuthService instance;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();
    //private static final String BACKEND_AUTH_URL = "http://localhost:8080/api/auth";
    //private static final String BACKEND_AUTH_URL = "http://localhost:8080/api/toastMasterManager/users";
    private static final String BACKEND_AUTH_URL = System.getenv("BACKEND_TOASTMASTER_URI") + "/users";
   // private String STUDENTS_URL_BACKEND = System.getenv("BACKEND_TOASTMASTER_URI");

    private AuthService() {
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean registerAppUser(AppUserRequest appUserRequest) {

        System.out.println("1:" + appUserRequest);
        System.out.println("1a: " + BACKEND_AUTH_URL + "/register");
        try {
            String requestBody = objectMapper.writeValueAsString(appUserRequest);
            System.out.println("2a:" + appUserRequest);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            System.out.println("2:" + appUserRequest);
            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("3:" + response);

            int statusCode = response.join().statusCode();

            if (statusCode == 201) {
                return true;
            } else {
                setErrorMessage("Registration failed. Email or Username duplicate?");
                return false;
            }
            //return statusCode == 201;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }


    private User mapToUser(String json) {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to map stock", e);
        }
    }

    public boolean login(String email, String password) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/login"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString((email + ":" + password).getBytes()))
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.join().statusCode();

        if (statusCode == 200) {
            String responseCookie = response.join().headers().firstValue("Set-Cookie").orElseThrow();
            String responseSessionId = responseCookie.substring(11, responseCookie.indexOf(";"));

            setSessionId(responseSessionId);
            setEmail(email);

            return true;
        } else {
            setErrorMessage("Login failed. Wrong username or password?. StatusCode: " + statusCode);
            return false;
        }
    }

    public boolean logout() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/logout"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .header("Cookie", "JSESSIONID=" + sessionId)
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.join().statusCode();

        if (statusCode == 200) {
            return true;
        } else {
            setErrorMessage("Logout failed");
            return false;
        }
    }

    public String me() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BACKEND_AUTH_URL + "/me"))
                .GET()
                .header("Cookie", "JSESSIONID=" + sessionId)
                .build();

        var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.join().statusCode();

        if (statusCode == 200) {
            return response.join().body();
        } else {
            setErrorMessage("Logout failed");
            return "Kein User ist eingeloggt";
        }
    }

}
