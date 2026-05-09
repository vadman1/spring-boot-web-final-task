package io.github.vadman1.springbootwebfinaltask.user;

import io.github.vadman1.springbootwebfinaltask.pet.PetDTO;
import io.github.vadman1.springbootwebfinaltask.pet.PetService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private Long idCounter;
    private final Map<Long, UserDTO> userMap;
    private final PetService petService;

    public UserService(PetService petService) {
        this.idCounter = 0L;
        this.userMap = new HashMap<>();
        this.petService = petService;
    }

    public List<UserDTO> getAllUsers() {
        return userMap.values()
                .stream()
                .toList();
    }

    public UserDTO createUser(UserDTO userToCreate) {
        Long newId = ++idCounter;

        UserDTO newUser = new UserDTO(
                newId,
                userToCreate.getName(),
                userToCreate.getEmail(),
                userToCreate.getAge()
        );

        userMap.put(newId, newUser);
        return newUser;
    }

    public UserDTO findById(Long id) {
        return Optional.ofNullable(userMap.get(id))
                .orElseThrow(() -> new NoSuchElementException("No found user with id=%s".formatted(id)));
    }

    public void deleteUser(Long id) {
        UserDTO user = findById(id);

        if (user.getPets() != null) {
            // создание копии, чтобы можно было одновременно итерироваться и изменять список
            List<PetDTO> petsCopy = new ArrayList<>(user.getPets());
            petsCopy
                    .forEach(pet -> petService.deletePet(pet.getId()));
        }

        userMap.remove(id);
    }

    public UserDTO updateUser(Long id, UserDTO userToUpdate) {
        UserDTO user = findById(id);

        var updatedUser = new UserDTO(
                id,
                userToUpdate.getName(),
                userToUpdate.getEmail(),
                userToUpdate.getAge(),
                user.getPets()
        );

        userMap.put(id, updatedUser);
        return updatedUser;
    }
}
