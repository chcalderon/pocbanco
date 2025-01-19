package com.example.userservice.controller;

import com.example.userservice.dto.LoginResponse;
import com.example.userservice.dto.PhoneDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signUp_shouldReturnCreatedUser() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("Jane Doe");
        userDTO.setEmail("jane.doe@example.com");
        userDTO.setPassword("Password123");
        PhoneDTO phoneDTO = new PhoneDTO();
        phoneDTO.setNumber(123456789);
        phoneDTO.setCitycode(1);
        phoneDTO.setCountrycode("57");
        userDTO.setPhones(List.of(phoneDTO));
        UserResponse mockResponse = new UserResponse();

        mockResponse.setId("12345");
        mockResponse.setToken("mocked_token");

        when(userService.createUser(any(UserDTO.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<UserResponse> response = userController.signUp(userDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getId());
        assertEquals("mocked_token", response.getBody().getToken());
    }

    @Test
    void login_shouldReturnLoginResponse() {
        // Arrange
        String mockToken = "Bearer mocked_token";

        LoginResponse mockResponse = new LoginResponse();
        mockResponse.setId("12345");
        mockResponse.setToken("new_mocked_token");

        when(userService.validateTokenAndUpdate(mockToken)).thenReturn(mockResponse);

        // Act
        ResponseEntity<LoginResponse> response = userController.login(mockToken);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("12345", response.getBody().getId());
        assertEquals("new_mocked_token", response.getBody().getToken());
    }
}
