package ru.suslov.user_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.suslov.user_service.dto.BearerToken;
import ru.suslov.user_service.dto.LoginDto;
import ru.suslov.user_service.dto.RefreshTokenDto;
import ru.suslov.user_service.dto.RegisterUserDto;
import ru.suslov.user_service.exception.BadRegistrationDataException;
import ru.suslov.user_service.model.RefreshToken;
import ru.suslov.user_service.model.Role;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.repository.UserAppRepository;
import ru.suslov.user_service.security.JwtUtilities;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class UserAppService {

    private AuthenticationManager authenticationManager;
    private final JwtUtilities jwtUtilities;
    private PasswordEncoder passwordEncoder;
    private final UserAppRepository userAppRepository;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public UserAppService(UserAppRepository userAppRepository, PasswordEncoder passwordEncoder, JwtUtilities jwtUtilities, RefreshTokenService refreshTokenService) {
        this.userAppRepository = userAppRepository;
        this.refreshTokenService = refreshTokenService;
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

    public BearerToken register(RegisterUserDto registerUserDto) {
        if (userAppRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
            throw new BadRegistrationDataException("email is already taken: " + registerUserDto.getEmail());
//            return new ResponseEntity<>("email is already taken !", HttpStatus.SEE_OTHER);
        } else if (userAppRepository.findByUsername(registerUserDto.getUsername()).isPresent()) {
            throw new BadRegistrationDataException("username is already taken: " + registerUserDto.getUsername());
        }
        UserApp userApp = new UserApp();
        userApp.setEmail(registerUserDto.getEmail());
        userApp.setFirstName(registerUserDto.getFirstName());
        userApp.setUsername(registerUserDto.getUsername());
        userApp.setSecondName(registerUserDto.getSecondName());
        userApp.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));
        userApp.setRoles(Collections.singleton(Role.USER));
        userApp.setCreatedTime(OffsetDateTime.now());
        userApp.setLastModifiedTime(userApp.getCreatedTime());
        userApp.setActive(Boolean.TRUE);
        userApp.setDeleted(Boolean.FALSE);

        userAppRepository.save(userApp);
        String token = jwtUtilities.generateToken(registerUserDto.getUsername(), userApp.getRoles().stream().map(Enum::name).toList());
        RefreshToken refreshToken = refreshTokenService.add(userApp.getId());
        return new BearerToken(token, refreshToken.getValue().toString(), "Bearer");
    }

    public BearerToken authenticate(LoginDto loginDto) {
        UserApp userApp = userAppRepository.findByUsername(loginDto.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(loginDto.getPassword(), userApp.getPassword())) {
            throw new BadCredentialsException("Exception trying to check password for user: " + loginDto.getUsername());
        }
        String token = jwtUtilities.generateToken(userApp.getUsername(), userApp.getRoles().stream().map(Enum::name).toList());
        RefreshToken refreshToken = refreshTokenService.add(userApp.getId());

        return new BearerToken(token, refreshToken.getValue().toString(), "Bearer");
    }

    public BearerToken refreshToken(RefreshTokenDto refreshTokenDto) {
        RefreshToken refreshToken = refreshTokenService.findByValue(UUID.fromString(refreshTokenDto.getValue())).orElseThrow(() -> new UsernameNotFoundException("Token not found"));
        refreshTokenService.verifyExpiration(refreshToken);
        UserApp userApp = userAppRepository.findById(refreshToken.getUserId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtUtilities.generateToken(userApp.getUsername(), userApp.getRoles().stream().map(Enum::name).toList());
        refreshToken = refreshTokenService.add(userApp.getId());
        return new BearerToken(token, refreshToken.getValue().toString(), "Bearer");
    }

}
