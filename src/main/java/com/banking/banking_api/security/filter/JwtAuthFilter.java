package com.banking.banking_api.security.filter;

import com.banking.banking_api.repository.AccountRepository;
import com.banking.banking_api.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

// This filter runs on EVERY incoming request, exactly once
// It checks the Authorization header, validates the JWT,
// and sets the current user in the security context
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Step 1: read the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // Step 2: if missing or not a Bearer token, skip — let Spring handle it
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: extract the token (strip "Bearer " prefix)
        final String token = authHeader.substring(7);

        // Step 4: validate the token
        if (!jwtUtil.isTokenValid(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5: get the account number from the token payload
        final String accountNumber = jwtUtil.extractAccountNumber(token);

        // Step 6: only proceed if not already authenticated
        if (accountNumber != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 7: confirm the account still exists in the database
            boolean exists = accountRepository
                    .findByAccountNumber(accountNumber)
                    .isPresent();

            if (exists) {
                // Step 8: create a Spring Security authentication object
                // This is what tells Spring "this request is authenticated"
                // Django equivalent: request.user = user set by middleware
                UserDetails userDetails = new User(
                        accountNumber, "", new ArrayList<>());

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Step 9: store in SecurityContext so controllers can access it
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 10: pass request to the next filter or controller
        filterChain.doFilter(request, response);
    }
}