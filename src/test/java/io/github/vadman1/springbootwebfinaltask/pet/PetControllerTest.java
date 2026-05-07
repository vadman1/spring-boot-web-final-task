package io.github.vadman1.springbootwebfinaltask.pet;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.vadman1.springbootwebfinaltask.user.UserDTO;
import io.github.vadman1.springbootwebfinaltask.user.UserService;
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
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PetService petService;

    @Autowired
    private UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldSuccessCreatePet() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var pet = new PetDTO(
                null,
                "Pet",
                user.getId()
        );

        String petJson = objectMapper.writeValueAsString(pet);
        String createdPetJson = mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDTO petResponse = objectMapper.readValue(createdPetJson, PetDTO.class);

        Assertions.assertNotNull(petResponse.getId());
        Assertions.assertEquals(pet.getName(), petResponse.getName());
        Assertions.assertEquals(pet.getUserId(), petResponse.getUserId());

        Assertions.assertDoesNotThrow(() -> petService.findById(petResponse.getId()));

        UserDTO userWithPet = userService.findById(user.getId());
        Assertions.assertEquals(1, userWithPet.getPets().size());
        Assertions.assertEquals(petResponse.getId(), userWithPet.getPets().getFirst().getId());
    }

    @Test
    void shouldNotCreatedPetWhenRequestNotValid() throws Exception {
        var pet = new PetDTO(
                null,
                "",
                1L
        );

        String petJson = objectMapper.writeValueAsString(pet);

        mockMvc.perform(post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(petJson)
                )
                .andExpect(status().is(400));
    }

    @Test
    void shouldSuccessGetAllPets() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var pet1 = petService.createPet(new PetDTO(
                null,
                "Pet1",
                user.getId()
        ));

        var pet2 = petService.createPet(new PetDTO(
                null,
                "Pet2",
                user.getId()
        ));

        String foundPetsString = mockMvc.perform(get("/pets"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse().getContentAsString();

        List<PetDTO> pets = objectMapper.readValue(foundPetsString, new TypeReference<>() {
        });

        Assertions.assertEquals(2, pets.size());

        org.assertj.core.api.Assertions.assertThat(pet1)
                .usingRecursiveComparison()
                .isEqualTo(pets.get(0));

        org.assertj.core.api.Assertions.assertThat(pet2)
                .usingRecursiveComparison()
                .isEqualTo(pets.get(1));
    }

    @Test
    void shouldSuccessFindById() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var pet = petService.createPet(new PetDTO(
                null,
                "Pet",
                user.getId()
        ));

        String foundPetJson = mockMvc.perform(get("/pets/{id}", pet.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PetDTO foundPet = objectMapper.readValue(foundPetJson, PetDTO.class);

        org.assertj.core.api.Assertions.assertThat(pet)
                .usingRecursiveComparison()
                .isEqualTo(foundPet);
    }

    @Test
    void shouldNotFoundWhenPetNotPresent() throws Exception {
        mockMvc.perform(get("/pets/{id}", Integer.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldSuccessDeletePet() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var pet = petService.createPet(new PetDTO(
                null,
                "Pet",
                user.getId()
        ));

        mockMvc.perform(delete("/pets/{id}", pet.getId()))
                .andExpect(status().isNoContent());

        Assertions.assertThrows(NoSuchElementException.class, () -> petService.findById(pet.getId()));
    }

    @Test
    void shouldSuccessUpdatePet() throws Exception {
        var user = userService.createUser(new UserDTO(
                null,
                "Vad",
                "v@mail.ru",
                25
        ));

        var pet = petService.createPet(new PetDTO(
                1L,
                "Pet",
                user.getId()
        ));

        var updatedPet = new PetDTO(
                null,
                "Pet-upd",
                user.getId()
        );

        String updatedPetJson = objectMapper.writeValueAsString(updatedPet);

        String updatedPetJsonResponse = mockMvc.perform(put("/pets/{id}", pet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPetJson)
                )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var updatedPetResponse = objectMapper.readValue(updatedPetJsonResponse, PetDTO.class);

        Assertions.assertEquals(pet.getId(), updatedPetResponse.getId());
        Assertions.assertEquals(updatedPet.getName(), updatedPetResponse.getName());
        Assertions.assertEquals(updatedPet.getUserId(), updatedPetResponse.getUserId());
    }
}