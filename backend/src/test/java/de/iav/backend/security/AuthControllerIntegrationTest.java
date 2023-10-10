package de.iav.backend.security;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WithMockUser(username = "anonymousUser")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String BASE_URL = "/api/toast-master-manager/auth";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testGetMeWithAuthenticatedUser() throws Exception {
        mockMvc.perform(get(BASE_URL +"/me")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().isOk())
                .andExpect(content().string("testUser"));
    }

    @Test
    void testGetMeWithAnonymousUser() throws Exception {
        mockMvc.perform(get(BASE_URL +"/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("anonymousUser"));
    }
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post(BASE_URL +"/login")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser")))
                .andExpect(status().isOk())
                .andExpect(content().string("testUser"));
    }
    @Test
    void register() throws Exception {
        NewAppUser user3 = new NewAppUser("test", "Hans", "David", "1234", "hans.david@rcm.de");

        mockMvc.perform(post(BASE_URL +"/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("firstName").value(user3.firstName()));
    }


    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post(BASE_URL +"/logout"))
                .andExpect(status().isOk())
                .andExpect(content().string("anonymousUser"));
    }
}