package com.zenitho.api.controller;

import com.zenitho.api.entities.User;
import com.zenitho.api.jwt.JwtUtils;
import com.zenitho.api.repositories.UserRepository;
import com.zenitho.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    public UserController(UserService userService) { this.userService = userService; }

    @GetMapping("/me") // 👈 Nuevo endpoint para obtener los datos del usuario actual
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username  = authentication.getName(); // El email del usuario está en el nombre de autenticación

        return userRepository.findByUsername(username )
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + username ));
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/admin")
    public User createAdminUser(@RequestBody User user) {
        return userService.createAdminUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
