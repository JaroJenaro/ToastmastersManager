package de.iav.backend.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.iav.backend.model.UserResponseDTO;
import de.iav.backend.security.NewAppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "users")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
class NewUserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final static String BASE_URL = "/api/toastMasterManager/users";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NewAppUser user1 = new NewAppUser("Putin", "Wladimir", "Putin", "1234", "wladimir.putin@udssr.ru");
    private final NewAppUser user2 = new NewAppUser("Trump", "Donald", "Trump", "1234", "donald.trump@usa.us");

    @BeforeEach
    void insertTestUsers() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user1))
        );
        mockMvc.perform(post(BASE_URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(user2))
        );
    }

    @Test
    void getAllUsers_shouldReturnAllEntries_whenTwoEntriesAreSavedAndNoSearchParamsAreDefined() throws Exception {

        mockMvc.perform(get(BASE_URL))
                .andExpect(jsonPath("[0].id").isNotEmpty())
                .andExpect(jsonPath("[0].firstName").value(user1.firstName()))
                .andExpect(jsonPath("[1].id").isNotEmpty())
                .andExpect(jsonPath("[1].firstName").value(user2.firstName()));
    }

    @Test
    void getAllUsersByFirstName_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {

        mockMvc.perform(get(BASE_URL + "?firstName=" + user1.firstName()))
                .andExpect(jsonPath("[0].firstName").value(user1.firstName()))
                .andExpect(jsonPath("$", hasSize(1)))
        ;
    }

    @Test
    void getAllUsersByLastName_shouldReturnOneEntry_whenOneFittingEntryExists() throws Exception {
        mockMvc.perform(get(BASE_URL + "?lastName=" + user1.lastName()))
                .andExpect(jsonPath("[0].lastName").value(user1.lastName()))
                .andExpect(jsonPath("$", hasSize(1)));
    }



    @Test
    void searchUser_ByFirstNameAndLastName() throws Exception {
        mockMvc.perform(get(BASE_URL + "/search?firstName=" + user1.firstName() + "&lastName=" + user1.lastName()))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("firstName").value(user1.firstName()))
                .andExpect(jsonPath("lastName").value(user1.lastName()));
    }

    @Test
    void searchUser_ByFirstNameAndEmail() throws Exception {
        mockMvc.perform(get(BASE_URL + "/search2?firstName=" + user1.firstName() + "&email=" + user1.email()))
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("firstName").value(user1.firstName()))
                .andExpect(jsonPath("email").value(user1.email()));
    }

    @Test
    void getUserById_shouldReturnRequestedUser_whenMatchingIdIsProvided() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> usersList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        UserResponseDTO firstUser = usersList.get(0);

        mockMvc.perform(get(BASE_URL + "/" + firstUser.getId()))
                .andExpect(jsonPath("id").value(firstUser.getId()));
    }

    @Test
    void addUser_shouldCreateNewUserWithId_whenValidDataIsProvided() throws Exception {
        NewAppUser user3 = new NewAppUser("test", "Hans", "David", "1234", "hans.david@rcm.de");

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("firstName").value(user3.firstName())
                );
    }

    @Test
    void addUser_shouldReturn400_whenInvalidDataIsProvided() throws Exception {
        NewAppUser userFirstName = new NewAppUser("Hansi", null, "David", "1234", "hans.david@rcm.de");
        NewAppUser userLastName = new NewAppUser("Hansi", "Hans", null, "1234", "hans.david@rcm.de");

        NewAppUser userEmail = new NewAppUser("Hansi", "Hans", "David", "1234", null);
        


        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userFirstName)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userLastName)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post(BASE_URL)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userEmail)))
                .andExpect(status().isBadRequest());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void updateUser_shouldUpdateUserAccordingly_whenUserHasAdminRoleAndUserExists() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> usersList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        UserResponseDTO originalUser = usersList.get(0);
        UserResponseDTO updatedFirstUser = new UserResponseDTO(originalUser.getId(),"Rene", "Borbonus", "rene.borbonus@greater.me", "USER");

        mockMvc.perform(put(BASE_URL + "/" + originalUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedFirstUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(originalUser.getId()))
                .andExpect(jsonPath("firstName").value(updatedFirstUser.getFirstName()))
                .andExpect(jsonPath("lastName").value(updatedFirstUser.getLastName())
                );
    }

    @Test
    void updateUser_shouldReturn400_whenValuesInBodyArentValidAndIdIsInvalid() throws Exception {
        String THIS_ID_DOES_NOT_EXIST = "THIS_ID_NUMBER_DOES_NOT_EXIST";
        NewAppUser userThatDoesntExist = new NewAppUser("?", "?", "?", "?", "?");

        mockMvc.perform(put(BASE_URL + "/" + THIS_ID_DOES_NOT_EXIST)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userThatDoesntExist)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturn400_whenValuesInBodyArentValidAndIdIsValid() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> usersList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        UserResponseDTO originalUser = usersList.get(0);

        NewAppUser invalidRequestBody = new NewAppUser("?", "?", "?", "?", "?");

        mockMvc.perform(put(BASE_URL + "/" + originalUser.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidRequestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturn404_whenUserDoesntExist() throws Exception {
        String THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST = "THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST";
        UserResponseDTO userThatDoesntExist = new UserResponseDTO(THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST,"Nobody", "Nobody", "Nobody@Nobody.de", "Nobody");

        mockMvc.perform(put(BASE_URL + "/123" )
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userThatDoesntExist)))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin",  roles = {"ADMIN"})
    @Test
    void deleteUser_shouldDeleteUserById_whenUserToDeleteExistsAndUserHasAdminRole() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> usersList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        UserResponseDTO userToDelete = usersList.get(0);

        mockMvc.perform(delete(BASE_URL + "/" + userToDelete.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(BASE_URL + "/" + userToDelete.getId()))
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteUser_shouldReturn404_whenUserDoesntExistAndUserHasAdminRole() throws Exception {
        String THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST = "THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST";

        mockMvc.perform(delete(BASE_URL + "/" + THIS_MATRICULATION_NUMBER_DOES_NOT_EXIST))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturn403_whenUserHasNoAdminRole() throws Exception {
        String usersListAsString = mockMvc.perform(get(BASE_URL))
                .andReturn().getResponse().getContentAsString();

        List<UserResponseDTO> usersList = objectMapper.readValue(usersListAsString, new TypeReference<>() {
        });
        UserResponseDTO originalUser = usersList.get(0);

        mockMvc.perform(delete(BASE_URL + "/" + originalUser.getId()))
                .andExpect(status().isForbidden());
    }
}
