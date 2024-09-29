package ru.suslov.user_service.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.service.UserAppService;

import java.security.Principal;

@Component
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final UserAppService userAppService;

    public AuthenticationManagerImpl(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        Principal principal = (Principal) authentication.getPrincipal();
        UserApp userApp = userAppService.findByUsername(principal.getName()).filter(UserApp::getActive).orElse(null);

        return new UsernamePasswordAuthenticationToken(
                userApp.getEmail(),
                userApp.getPassword()
        );
    }
}
