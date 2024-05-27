package com.guilhermerodrigues.picpaychallenge.services;

import com.guilhermerodrigues.picpaychallenge.dtos.UserRequestDTO;
import com.guilhermerodrigues.picpaychallenge.entities.User;
import com.guilhermerodrigues.picpaychallenge.entities.UserType;
import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeAll
    static void createUser(@Autowired UserRepository userRepository) {
        User user = new User(1L, "Guilherme", "Rodrigues", "99988899989", "gui@hotmail.com", "1234", BigDecimal.ZERO, UserType.COMMON);

        userRepository.save(user);
    }

    @Test
    @DisplayName("Should create a User when everything is ok.")
    void create() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("Kaue", "Palota", "99900099909", "kaue@hotmail.com", "1234", UserType.COMMON);

        userService.create(userRequestDTO);

        User user = userRepository.findByEmail(userRequestDTO.email());

        Assertions.assertNotNull(user);
    }

    @Test
    @DisplayName("Should return a bad request exception when email or document is already created in any user in db.")
    void createCase2() {
        UserRequestDTO userRequestDTO = new UserRequestDTO("Kaue", "Palota", "99988899989", "gui@hotmail.com", "1234", UserType.COMMON);

        Assertions.assertThrows(BadRequestException.class, () -> {
            userService.create(userRequestDTO);
        });
    }
}