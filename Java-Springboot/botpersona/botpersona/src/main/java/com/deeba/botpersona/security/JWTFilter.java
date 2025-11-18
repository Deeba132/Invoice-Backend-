package com.deeba.botpersona.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTFilter.class);

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getServletPath();
        logger.debug("Processing request: {}", path);
        
        // Skip JWT check for /api/auth/**
        if (path.startsWith("/api/auth")) {
            logger.debug("Skipping JWT filter for auth endpoint");
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        logger.debug("Authorization header: {}", authHeader);
        
        String token = null;
        String username = null;

        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7);
                logger.debug("Extracted token: {}...", token.substring(0, Math.min(20, token.length())));
                
                username = jwtUtil.extractUsername(token);
                logger.debug("Extracted username: {}", username);
            } else {
                logger.warn("No valid Authorization header found");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                logger.debug("Loading user details for: {}", username);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.debug("User details loaded: {}", userDetails.getUsername());

                if (jwtUtil.isTokenValid(token, username)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("✅ Authentication set successfully for user: {}", username);
                } else {
                    logger.error("❌ Token validation FAILED for user: {}", username);
                }
            } else if (username == null) {
                logger.warn("Username is null - token extraction failed");
            } else {
                logger.debug("Authentication already set in SecurityContext");
            }
        } catch (JWTVerificationException e) {
            logger.error("❌ JWT verification failed: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("❌ Error processing JWT: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }
}