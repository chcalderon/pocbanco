package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.dto.PhoneDTO;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.model.User;
import com.example.userservice.model.Phone;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public UserResponse createUser(UserDTO userDTO) {
        Optional<User> existingUser = userRepository.findByEmail(userDTO.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe.");
        }
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRawPassword(userDTO.getPassword()); // Texto plano
        user.setCreated(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setActive(true);
        user.setPhones(userDTO.getPhones().stream()
                .map(dto -> new Phone(dto.getNumber(), dto.getCitycode(), dto.getCountrycode()))
                .collect(Collectors.toList()));

        // Generar token JWT
        String token = jwtUtil.generateToken(userDTO.getEmail());
        user.setToken(token);

        User usersaved = userRepository.save(user);

        UserResponse response = new UserResponse();
        response.setId(usersaved.getId().toString());
        response.setCreated(usersaved.getCreated());
        response.setLastLogin(usersaved.getLastLogin());
        response.setToken(usersaved.getToken());
        response.setActive(usersaved.isActive());

        return response;
    }

    public LoginResponse validateTokenAndUpdate(String pretoken) {
        try {
            // Verifica que el token no sea nulo o vacío
            if (pretoken == null || pretoken.isBlank()) {
                throw new IllegalArgumentException("El token JWT no ha sido proporcionado");
            }
            String token = extractToken(pretoken); // Extraer token del encabezado
            String userEmail;
            try {
                userEmail = jwtUtil.extractUsername(token); // Extraer correo del token
            } catch (ExpiredJwtException e) {
                throw new IllegalArgumentException("El token ha expirado", e);
            } catch (MalformedJwtException e) {
                throw new IllegalArgumentException("El token proporcionado tiene un formato inválido", e);
            } catch (SignatureException e) {
                throw new IllegalArgumentException("La firma del token no es válida", e);
            } catch (Exception e) {
                throw new IllegalArgumentException("El token proporcionado no es válido", e);
            }

            // Buscar usuario por correo electrónico
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new IllegalArgumentException("El token proporcionado no está asociado a un usuario válido"));

            // Actualizar información de último ingreso y generar nuevo token
            user.setLastLogin(LocalDateTime.now());
            String newToken = jwtUtil.generateToken(user.getEmail());
            user.setToken(newToken);

            userRepository.save(user);

            return new LoginResponse(
                    user.getId().toString(),
                    user.getCreated().toString(),
                    user.getLastLogin().toString(),
                    newToken,
                    user.isActive(),
                    user.getName(),
                    user.getEmail(),
                    user.getRawPassword(),
                    user.getPhones().stream()
                            .map(dto -> new PhoneDTO(dto.getNumber(), dto.getCitycode(), dto.getCountrycode()))
                            .collect(Collectors.toList())
            );
        } catch (UserNotFoundException | IllegalArgumentException e) {
        throw e; // Estas excepciones ya tienen manejadores específicos.
        } catch (Exception e) {
            throw new RuntimeException("Ocurrió un error inesperado", e);
        }
    }

    /**
     * Extrae el token JWT del encabezado de autorización.
     */
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("Token JWT no proporcionado o con formato inválido");
    }

}

