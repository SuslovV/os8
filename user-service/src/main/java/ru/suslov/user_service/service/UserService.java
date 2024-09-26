package ru.suslov.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.user_service.model.User;
import ru.suslov.user_service.repository.UserRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User add(String username) {
        var user = new User();
        user.setUsername(username);
        user.setCreatedTime(OffsetDateTime.now());
        user.setLastModifiedTime(user.getCreatedTime());
        user.setActive(Boolean.TRUE);
        user.setDeleted(Boolean.FALSE);

        return userRepository.save(user);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

}
