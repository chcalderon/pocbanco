package com.example.userservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long number;
    private int citycode;
    private String countrycode;

    // Constructor específico para número, código de ciudad y código de país
    public Phone(long number, int citycode, String contrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = contrycode;
    }
}
