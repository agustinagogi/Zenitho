package com.zenitho.api.service;

import com.zenitho.api.entities.User;
import com.zenitho.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String name, String username, String email, String password){
        User newUser = new User();
        newUser.setName(name);
        newUser.setUsername(username);
        newUser.setEmail(email);

        newUser.setPassword(passwordEncoder.encode(password));

        return userRepository.save(newUser);

    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No se encontr<UNK> el usuario con el id " + userId));
    }
}
