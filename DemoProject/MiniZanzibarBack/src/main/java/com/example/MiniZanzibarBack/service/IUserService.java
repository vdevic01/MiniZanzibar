package com.example.MiniZanzibarBack.service;

import com.example.MiniZanzibarBack.dto.UserLoginDTO;
import com.example.MiniZanzibarBack.dto.UserRegistrationDTO;
import com.example.MiniZanzibarBack.exception.UserAlreadyExistsException;
import com.example.MiniZanzibarBack.model.User;

public interface IUserService {
    User loginUser(UserLoginDTO userLoginDTO);

    User registerUser(UserRegistrationDTO userDTO) throws UserAlreadyExistsException;
}
