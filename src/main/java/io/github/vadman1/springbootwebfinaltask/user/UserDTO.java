package io.github.vadman1.springbootwebfinaltask.user;

import io.github.vadman1.springbootwebfinaltask.pet.PetDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

public class UserDTO {

    @Null
    private Long id;

    @NotBlank
    private String name;

    @Email
    private String email;

    @Min(0)
    @Max(120)
    @NotNull
    private Integer age;

    @Valid
    private List<PetDTO> pets;

    public UserDTO() {
    }

    public UserDTO(Long id, String name, String email, Integer age) {
        this(id, name, email, age, null);
    }

    public UserDTO(Long id, String name, String email, Integer age, List<PetDTO> pets) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.pets = pets;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<PetDTO> getPets() {
        return pets;
    }

    public void setPets(List<PetDTO> pets) {
        this.pets = pets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id) && Objects.equals(name, userDTO.name) && Objects.equals(email, userDTO.email) && Objects.equals(age, userDTO.age) && Objects.equals(pets, userDTO.pets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age, pets);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", pets=" + pets +
                '}';
    }
}