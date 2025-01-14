package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.PhoneDTO;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.model.User;
import com.example.userservice.model.Phone;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UserService userService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void createUser_shouldCreateUserSuccessfully() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");
        userDTO.setEmail("john.doe@example.com");
        userDTO.setPassword("a2asfGfdfdf4");
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(123456789);
        phoneDTO.setCitycode(1);
        phoneDTO.setCountrycode("57");
        userDTO.setPhones(List.of(phoneDTO));

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());
        when(jwtUtil.generateToken(userDTO.getEmail())).thenReturn("mocked_token");

        User mockUser = new User();
        UUID randomUUID = UUID.randomUUID();
        mockUser.setId(randomUUID);
        mockUser.setCreated(LocalDateTime.now());
        mockUser.setLastLogin(LocalDateTime.now());
        mockUser.setToken("mocked_token");
        mockUser.setActive(true);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);
        
        // Act
        UserResponse response = userService.createUser(userDTO);

        // Assert
        assertNotNull(response);
        assertEquals("mocked_token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void createUser_shouldThrowExceptionIfUserAlreadyExists() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("john.doe@example.com");

        when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void validateTokenAndUpdate_shouldReturnLoginResponse() {
        // Arrange
        String token = "Bearer mocked_token";
        String email = "john.doe@example.com";

        when(jwtUtil.extractUsername("mocked_token")).thenReturn(email);

        User mockUser = new User();
        UUID randomUUID = UUID.randomUUID();
        mockUser.setId(randomUUID);
        mockUser.setEmail(email);
        mockUser.setCreated(LocalDateTime.now());
        mockUser.setLastLogin(LocalDateTime.now());
        mockUser.setActive(true);
        mockUser.setName("John Doe");
        mockUser.setRawPassword("password123");
        Phone phone = new Phone();
        phone.setCitycode(1);
        phone.setCountrycode("57");
        phone.setNumber(123456789);
        mockUser.setPhones(List.of(phone));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(email)).thenReturn("new_mocked_token");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        LoginResponse response = userService.validateTokenAndUpdate(token);

        // Assert
        assertNotNull(response);
        assertEquals("new_mocked_token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void validateTokenAndUpdate_shouldThrowExceptionForInvalidToken() {
        // Arrange
        String token = "Bearer invalid_token";

        when(jwtUtil.extractUsername("invalid_token")).thenThrow(new IllegalArgumentException("Token no válido"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.validateTokenAndUpdate(token));
        verify(userRepository, never()).save(any(User.class));
    }

    // @Test
    // public void createUser_shouldThrowExceptionForInvalidEmail() {
    //     // Arrange
    //     UserDTO userDTO = new UserDTO();
    //     userDTO.setName("John Doe");
    //     userDTO.setEmail("invalid_email"); // Missing "@" symbol
    //     userDTO.setPassword("a2asfGfdfdf4");
    //     PhoneDTO phoneDTO = new PhoneDTO();
    //     phoneDTO.setNumber(123456789);
    //     phoneDTO.setCitycode(1);
    //     phoneDTO.setCountrycode("57");
    //     userDTO.setPhones(List.of(phoneDTO));

    //     // Mock userRepository behavior for invalid email
    //     when(userRepository.findByEmail(userDTO.getEmail())).thenReturn(Optional.empty());

    //     // Act & Assert
    //     assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    //     verify(userRepository, never()).save(any(User.class));
    // }

    @Test
    public void validateTokenAndUpdate_shouldThrowExceptionForExpiredToken() {
        // Arrange
        String token = "Bearer expired_token";

        when(jwtUtil.extractUsername(token)).thenThrow(new ExpiredJwtException(null, null, "Token has expired"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.validateTokenAndUpdate(token));
        verify(userRepository, never()).save(any(User.class));
    }
}
