package com.springprojects.banking_application.security;

import com.springprojects.banking_application.entity.User;
import com.springprojects.banking_application.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepo userRepo;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepo userRepo) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String emailExtracted = jwtService.extractEmail(token);

        if(emailExtracted!=null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            User user = userRepo.findByEmail(emailExtracted)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request,response);

        }

    }
}
