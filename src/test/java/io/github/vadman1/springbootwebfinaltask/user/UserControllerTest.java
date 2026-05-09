package io.github.vadman1.springbootwebfinaltask.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreateUser() throws Exception {
        var user = new UserDTO(
                null,
                "Vad",
                "vad@mail.ru",
                27,
                null
        );

        String userJson = objectMapper.writeValueAsString(user);

        String createdUserJson = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO userResponse = objectMapper.readValue(createdUserJson, UserDTO.class);

        Assertions.assertNotNull(userResponse.getId());
        Assertions.assertEquals(user.getName(), userResponse.getName());
        Assertions.assertEquals(user.getEmail(), userResponse.getEmail());
        Assertions.assertEquals(user.getAge(), userResponse.getAge());

        Assertions.assertDoesNotThrow(() -> userService.findById(userResponse.getId()));
    }

    @Test
    void shouldNotCreateUserWhenRequestNotValid() throws Exception {
        var user = new UserDTO(
                null,
                "Vad",
                "not_valid_email",
                27,
                null
        );

        String userJson = objectMapper.writeValueAsString(user);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessGetAllUsers() throws Exception {
        var user1 = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                24
        ));

        var user2 = userService.createUser(new UserDTO(
                null,
                "Vad2",
                "v2@mail.ru",
                26
        ));

        String foundUsersString = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        List<UserDTO> users = objectMapper.readValue(foundUsersString, new TypeReference<>() {
        });

        Assertions.assertEquals(2, users.size());

        org.assertj.core.api.Assertions.assertThat(user1)
                .usingRecursiveComparison()
                .isEqualTo(users.get(0));

        org.assertj.core.api.Assertions.assertThat(user2)
                .usingRecursiveComparison()
                .isEqualTo(users.get(1));
    }

    @Test
    void shouldSuccessFindById() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        String foundUserJson = mockMvc.perform(get("/users/{id}", user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserDTO foundUser = objectMapper.readValue(foundUserJson, UserDTO.class);

        org.assertj.core.api.Assertions.assertThat(user)
                .usingRecursiveComparison()
                .isEqualTo(foundUser);
    }

    @Test
    void shouldNotFoundWhenUserNotPresent() throws Exception {
        mockMvc.perform(get("/users/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessDeleteUser() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        mockMvc.perform(delete("/users/{id}", user.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThrows(NoSuchElementException.class, () -> userService.findById(user.getId()));
    }

    @Test
    void shouldSuccessUpdateUser() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var updatedUser = new UserDTO(
                null,
                "Vad-upd",
                "v_upd@mail.ru",
                27
        );

        String updatedUserJson = objectMapper.writeValueAsString(updatedUser);

        String updatedUserJsonResponse = mockMvc.perform(put("/users/{id}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserJson)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var updatedUserResponse = objectMapper.readValue(updatedUserJsonResponse, UserDTO.class);

        Assertions.assertEquals(user.getId(), updatedUserResponse.getId());
        Assertions.assertEquals(updatedUser.getName(), updatedUserResponse.getName());
        Assertions.assertEquals(updatedUser.getEmail(), updatedUserResponse.getEmail());
        Assertions.assertEquals(updatedUser.getAge(), updatedUserResponse.getAge());
    }
}