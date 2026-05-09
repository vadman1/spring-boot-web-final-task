package io.github.vadman1.springbootwebfinaltask.pet;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PetController {

    private static final Logger log = LoggerFactory.getLogger(PetController.class);

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/pets")
    public List<PetDTO> getAllPets() {
        log.info("Get request for getAllPets");
        return petService.getAllPets();
    }

    @PostMapping("/pets")
    public ResponseEntity<PetDTO> createPet(
            @RequestBody @Valid PetDTO petToCreate
    ) {
        log.info("Get request for create pet: pet={}", petToCreate);
        PetDTO createdPet = petService.createPet(petToCreate);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPet);
    }

    @GetMapping("/pets/{id}")
    public PetDTO findById(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for find pet by id: id={}", id);
        return petService.findById(id);
    }

    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable(name = "id") Long id
    ) {
        log.info("Get request for delete pet by id: id={}", id);
        petService.deletePet(id);

        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }

    @PutMapping("/pets/{id}")
    public PetDTO updatePet(
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid PetDTO petToUpdate
    ) {
        log.info("Get request for update pet: id={}, petToUpdate={}", id, petToUpdate);
        return petService.updatePet(id, petToUpdate);
    }
}
