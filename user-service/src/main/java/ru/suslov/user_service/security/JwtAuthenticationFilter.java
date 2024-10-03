package ru.suslov.user_service.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.suslov.user_service.model.UserApp;
import ru.suslov.user_service.service.UserAppService;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtilities jwtUtilities;
    private UserAppService userAppService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtilities jwtUtilities) {
        this.jwtUtilities = jwtUtilities;
    }

    @Autowired
    public void setUserAppService(UserAppService userAppService) {
        this.userAppService = userAppService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtilities.getToken(request);

        if (token != null && jwtUtilities.validateToken(token)) {
            String username = jwtUtilities.extractUsername(token);

            UserApp userApp = userAppService.findByUsername(username).orElse(null);
            if (userApp != null) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userApp.getUsername(), null, userApp.getAuthorities());
                log.info("Authenticated user with user name: {}", username);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }
        filterChain.doFilter(request, response);
    }

}
