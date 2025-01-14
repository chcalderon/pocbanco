package com.example.userservice.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.Valid;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    @NotNull(message = "El nombre no puede ser nulo")
    private String name;

    @NotNull(message = "El correo no puede ser nulo")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", 
             message = "El correo electrónico no tiene un formato válido")
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d.*\\d)[A-Za-z\\d]{8,12}$", message = "La contraseña debe tener entre 8 y 12 caracteres, al menos una letra mayúscula y dos números")
    private String password;

    @Valid
    private List<PhoneDTO> phones;

}
