package com.example.userservice.service;

import com.example.userservice.dto.UserResponse;
import com.example.userservice.dto.LoginResponse;
import com.example.userservice.dto.UserDTO;

public interface UserService {
    
    UserResponse createUser(UserDTO userDTO);
    LoginResponse validateTokenAndUpdate(String pretoken);

}
