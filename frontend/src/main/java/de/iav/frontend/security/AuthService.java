package de.iav.frontend.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.frontend.exception.MappingRuntimeException;
import de.iav.frontend.model.UserRequestDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;


public class AuthService {

    private String email;

    private String sessionId;
    private String errorMessage;

    private static final Logger LOG = LogManager.getLogger();

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

    public String getEmail() {
        return email;
    }

    private static AuthService instance;
    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String BACKEND_AUTH_URL = System.getenv("BACKEND_TOASTMASTER_URI") + "/auth";


    private AuthService() {
    }

    public static synchronized AuthService getInstance() {
        if (instance == null) {
            instance = new AuthService();
        }
        return instance;
    }

    public boolean registerAppUser(UserRequestDto appUserRequest) {

        LOG.info("1: {}", appUserRequest);
        LOG.info("1a: {}/register", BACKEND_AUTH_URL);
        try {
            String requestBody = objectMapper.writeValueAsString(appUserRequest);
            LOG.info("2a: {}", appUserRequest);


            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(BACKEND_AUTH_URL + "/register"))
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            LOG.info("2: {}", appUserRequest);
            var response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
            LOG.info("3: {}", response);

            int statusCode = response.join().statusCode();

            if (statusCode == 201) {
                return true;
            } else {
                setErrorMessage("Registration failed. Email or Username duplicate?");
                return false;
            }
        }
        catch (JsonProcessingException e) {
            throw new MappingRuntimeException("Failed to register User" + e.getMessage());
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
            return "me :" + response.join().body() + "  auth: " + getEmail();
        } else {
            setErrorMessage("Logout failed");
            return "Kein User ist eingeloggt";
        }
    }

}
