package com.example.userservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDTO {

    @NotNull(message = "Phone number is required")
    private long number;

    @NotNull(message = "City code is required")
    private int citycode;

    @NotNull(message = "Country code is required")
    private String countrycode;

}