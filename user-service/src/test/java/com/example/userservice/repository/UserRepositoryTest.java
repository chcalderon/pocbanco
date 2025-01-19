package com.example.userservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.userservice.model.Phone;
import com.example.userservice.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import org.assertj.core.groups.Tuple;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("Test para evaluar guardado de usuario")
    @Test
    void testGuardarUser(){
        //BDD Desarrollo dirigido por comportamiento
        //given when y then

        // Given: Un usuario con teléfonos configurados
        Phone phone1 = new Phone(123456789L, 1, "57");
        Phone phone2 = new Phone(987654321L, 1, "57");

        User user = new User();
        user.setName("John Doe");
        user.setEmail("johndoe@example.com");
        user.setPassword("encryptedPassword");
        user.setRawPassword("rawPassword123");
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setPhones(List.of(phone1, phone2));

        // When: Se guarda el usuario en el repositorio
        User savedUser = userRepository.save(user);

        // Then: El usuario debe haberse guardado correctamente
        Optional<User> retrievedUser = userRepository.findById(savedUser.getId());
        assertThat(retrievedUser).isPresent();
        assertThat(retrievedUser.get().getName()).isEqualTo("John Doe");
        assertThat(retrievedUser.get().getEmail()).isEqualTo("johndoe@example.com");
        assertThat(retrievedUser.get().getPhones()).hasSize(2);

        // Verificar los datos de los teléfonos
        assertThat(retrievedUser.get().getPhones())
            .extracting(Phone::getNumber, Phone::getCitycode, Phone::getCountrycode)
            .containsExactlyInAnyOrder(
                    Tuple.tuple(123456789L, 1, "57"),
                    Tuple.tuple(987654321L, 1, "57")
            );
    }
}
