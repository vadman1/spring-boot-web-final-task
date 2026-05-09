package io.github.vadman1.springbootwebfinaltask.user;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public List<UserDTO> getAllUsers() {
        log.info("Get request for getAllUsers");
        return userService.getAllUsers();
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(
            @RequestBody @Valid UserDTO userToCreate
    ) {
        log.info("Get request for create user: user={}", userToCreate);
        UserDTO createdUser = userService.createUser(userToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @GetMapping("/users/{id}")
    public UserDTO findById(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for find user by id: id={}", id);
        return userService.findById(id);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for delete user by id: id={}", id);
        userService.deleteUser(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/users/{id}")
    public UserDTO updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid UserDTO userToUpdate
    ) {
        log.info("Get request for update user by id: id={}, userToUpdate={}", id, userToUpdate);
        return userService.updateUser(id, userToUpdate);
    }
}
