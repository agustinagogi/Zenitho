package com.zenitho.api.service;

import com.zenitho.api.entities.*;
import com.zenitho.api.repositories.RoleRepository;
import com.zenitho.api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardColumnService boardColumnService;

    @Autowired
    private CardService cardService;

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No se encontr<UNK> el usuario con el id " + userId));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        user.getRoles().add(role);
        User savedUser = userRepository.save(user);

        Board defaultBoard = boardService.createBoard("Mi primer tablero", savedUser.getId());
        BoardColumn defaultColumn = boardColumnService.createColumn("Tareas pendientes", defaultBoard.getId());

        Card defaultCard = new Card();
        defaultCard.setTitle("'Bienvenido a Zenitho!");
        defaultCard.setContent(("{\"type\":\"doc\",\"content\":[{\"type\":\"paragraph\",\"content\":[{\"text\":\"¡Edita o elimina esta tarjeta para empezar!\",\"type\":\"text\"}]}]}"));
        cardService.createCard(defaultCard, defaultColumn.getId());
        return userRepository.save(user);
    }

    public User createAdminUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role role = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User user) {
        User db = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        db.setName(user.getName());
        db.setUsername(user.getUsername());
        db.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            db.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userRepository.save(db);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }
}
