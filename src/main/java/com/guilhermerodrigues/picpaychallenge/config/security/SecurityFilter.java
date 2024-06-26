package com.guilhermerodrigues.picpaychallenge.config.security;

import com.guilhermerodrigues.picpaychallenge.exceptions.BadRequestException;
import com.guilhermerodrigues.picpaychallenge.repositories.UserRepository;
import com.guilhermerodrigues.picpaychallenge.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = this.recoverToken(request);
        if(token != null) {
            String subject = tokenService.validateToken(token);

            UserDetails user = userRepository.findByEmail(subject);
            if(user == null) {
                throw new BadRequestException("Error while recover this token.");
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if(authorization == null) return null;

        return authorization.replace("Bearer ", "");
    }
}
