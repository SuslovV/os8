package ru.suslov.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.dto.LoginDto;
import ru.suslov.user_service.dto.RegisterUserDto;
import ru.suslov.user_service.model.Role;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.model.UserAppPrincipal;
import ru.suslov.user_service.repository.UserAppRepository;
import ru.suslov.user_service.security.JwtUtilities;

import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.*;

@Service
public class UserAppService {

    private AuthenticationManager authenticationManager;
    private final JwtUtilities jwtUtilities;
    private PasswordEncoder passwordEncoder;
    private final UserAppRepository userAppRepository;

    @Autowired
    public UserAppService(UserAppRepository userAppRepository, PasswordEncoder passwordEncoder, JwtUtilities jwtUtilities) {
        this.userAppRepository = userAppRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtilities = jwtUtilities;
    }

    @Autowired
    @Lazy
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public Optional<UserApp> findById(UUID id) {
        return userAppRepository.findById(id);
    }

    public Optional<UserApp> findByUsername(String username) {
        return userAppRepository.findByUsername(username);
    }

    public Optional<UserApp> findByEmail(String email) {
        return userAppRepository.findByEmail(email);
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

    //    public ResponseEntity<?> register(RegisterUserDto registerUserDto) {
    public BearerToken register(RegisterUserDto registerUserDto) throws Exception {
        if (userAppRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new Exception("email is already taken");
//            return new ResponseEntity<>("email is already taken !", HttpStatus.SEE_OTHER);
        } else if (userAppRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new Exception("username is already taken");
        } else {
            UserApp userApp = new UserApp();
            userApp.setEmail(registerUserDto.getEmail());
            userApp.setFirstName(registerUserDto.getFirstName());
            userApp.setUsername(registerUserDto.getUsername());
            userApp.setSecondName(registerUserDto.getSecondName());
            userApp.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
            userApp.setRoles(Collections.singleton(Role.ADMIN));
            userApp.setCreatedTime(OffsetDateTime.now());
            userApp.setLastModifiedTime(userApp.getCreatedTime());
            userApp.setActive(Boolean.TRUE);
            userApp.setDeleted(Boolean.FALSE);

            userAppRepository.save(userApp);
            String token = jwtUtilities.generateToken(registerUserDto.getUsername(), userApp.getRoles().stream().map(Enum::name).toList());
//            return new ResponseEntity<>(new BearerToken(token, "Bearer"), HttpStatus.OK);
            return new BearerToken(token, "Bearer");
        }
    }

    public BearerToken authenticate(LoginDto loginDto) {
        UserApp userApp = userAppRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // todo проверить пар
        String token = jwtUtilities.generateToken(userApp.getUsername(), userApp.getRoles().stream().map(Enum::name).toList());
//        List<String> roles = userApp.getRoles().stream().map(Enum::name).toList();

//        Principal principal = new UserAppPrincipal(userApp.getId(), loginDto.getEmail(), roles);
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        principal,
//                        loginDto.getPassword(),
//                        userApp.getAuthorities()
//                )
//        );
//        SecurityContextHolder.getContext().setAuthentication(authentication);
        return new BearerToken(token, "Bearer");
    }

}
