package com.example.MiniZanzibarBack.service.implementation;

import com.example.MiniZanzibarBack.dto.UserLoginDTO;
import com.example.MiniZanzibarBack.dto.UserRegistrationDTO;
import com.example.MiniZanzibarBack.exception.UserAlreadyExistsException;
import com.example.MiniZanzibarBack.mapper.UserMapper;
import com.example.MiniZanzibarBack.model.User;
import com.example.MiniZanzibarBack.repository.BaseJPARepository;
import com.example.MiniZanzibarBack.repository.UserRepository;
import com.example.MiniZanzibarBack.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends BaseService<User> implements IUserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected BaseJPARepository<User> getRepository() {
        return this.userRepository;
    }

    @Override
    public User loginUser(UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword()));
        return userRepository.findByEmail(userLoginDTO.getEmail()).orElseThrow();
    }

    @Override
    public User registerUser(UserRegistrationDTO userDTO) throws UserAlreadyExistsException {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User already exists with username: " + userDTO.getEmail());
        }
        User user = UserMapper.INSTANCE.userRegistrationDTOtoUser(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return userRepository.save(user);
    }
}
