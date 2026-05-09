package io.github.vadman1.springbootwebfinaltask.pet;

import io.github.vadman1.springbootwebfinaltask.user.UserDTO;
import io.github.vadman1.springbootwebfinaltask.user.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PetService {

    private Long idCounter;
    private final Map<Long, PetDTO> petMap;
    private final UserService userService;

    public PetService(
            @Lazy UserService userService
    ) {
        this.userService = userService;
        this.idCounter = 0L;
        this.petMap = new HashMap<>();
    }

    public List<PetDTO> getAllPets() {
        return petMap.values()
                .stream()
                .toList();
    }

    public PetDTO createPet(PetDTO petToCreate) {
        Long newId = ++idCounter;
        PetDTO newPet = new PetDTO(
                newId,
                petToCreate.getName(),
                petToCreate.getUserId()
        );

        UserDTO user = userService.findById(newPet.getUserId());
        if (user.getPets() == null) {
            user.setPets(new ArrayList<>());
        }

        user.getPets()
                .add(newPet);


        petMap.put(newId, newPet);
        return newPet;
    }

    public PetDTO findById(Long id) {
        return Optional.ofNullable(petMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No found pet by id=%s".formatted(id)));
    }

    public void deletePet(Long id) {
        PetDTO result = petMap.remove(id);
        if (result == null) {
            throw new NoSuchElementException("No found pet by id=%s".formatted(id));
        }

        UserDTO user = userService.findById(result.getUserId());
        user.getPets()
                .remove(result);
    }

    public PetDTO updatePet(Long id, PetDTO petToUpdate) {
        if (petMap.get(id) == null) {
            throw new NoSuchElementException("No found pet by id=%s".formatted(id));
        }

        var updatedPet = new PetDTO(
                id,
                petToUpdate.getName(),
                petToUpdate.getUserId()
        );

        petMap.put(id, updatedPet);

        UserDTO user = userService.findById(updatedPet.getUserId());
        user.getPets().stream()
                .filter(pet -> Objects.equals(pet.getId(), id))
                .findFirst()
                .ifPresent(pet -> {
                    pet.setId(updatedPet.getId());
                    pet.setName(updatedPet.getName());
                    pet.setUserId(updatedPet.getUserId());
                });

        return updatedPet;
    }

}