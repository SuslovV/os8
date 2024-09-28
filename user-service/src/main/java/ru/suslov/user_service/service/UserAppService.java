package ru.suslov.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.repository.UserAppRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserAppService {

    private final UserAppRepository userAppRepository;

    @Autowired
    public UserAppService(UserAppRepository userAppRepository) {
        this.userAppRepository = userAppRepository;
    }

    public Optional<UserApp> findById(UUID id) {
        return userAppRepository.findById(id);
    }

    public Optional<UserApp> findByEmail(String username) {
        return userAppRepository.findByEmail(username);
    }

    public Page<UserApp> findAll(Pageable pageable) {
        return userAppRepository.findAll(pageable);
    }

    public UserApp save(UserApp userApp) {
        return userAppRepository.save(userApp);
    }

    public UserApp add(UserApp userApp) {
        userApp.setCreatedTime(OffsetDateTime.now());
        userApp.setLastModifiedTime(userApp.getCreatedTime());
        userApp.setActive(Boolean.TRUE);
        userApp.setDeleted(Boolean.FALSE);

        return userAppRepository.save(userApp);
    }

    public void delete(UserApp userApp) {
        userAppRepository.delete(userApp);
    }

}
