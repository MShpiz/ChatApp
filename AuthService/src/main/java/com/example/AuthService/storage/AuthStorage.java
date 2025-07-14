package com.example.AuthService.storage;

import com.example.AuthService.dto.AuthorisationRequest;
import com.example.AuthService.storage.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthStorage implements UserDetailsService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private EntityManager manager;

    @Transactional
    public User addUser(AuthorisationRequest dto) {
        if (repository.existsByUsername(dto.username())) {
            throw new IllegalArgumentException("user exists");
        }

        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password()); //хеширование пароля происходит на уровне сервиса (желательно на фронтенде но у нас его нет)

        manager.persist(user);
        return user;
    }

    public User getUser(String username) {
        User user = repository.findByUsername(username).orElseThrow(); // Бросает  NoSuchElementException если пользователей с таким именем нет
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUser(username);
    }
}
