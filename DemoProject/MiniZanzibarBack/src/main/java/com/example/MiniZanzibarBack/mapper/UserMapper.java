package com.example.MiniZanzibarBack.mapper;

import com.example.MiniZanzibarBack.dto.UserRegistrationDTO;
import com.example.MiniZanzibarBack.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

//    UserDetailsDTO userToUserDetailsDTO(User user);

    User userRegistrationDTOtoUser(UserRegistrationDTO userRegistrationDTO);
}

