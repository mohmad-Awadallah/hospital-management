package com.hospital.security;

import com.hospital.config.SecurityConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String TOKEN_PREFIX = "Bearer ";
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;

    public JwtAuthenticationFilter(JwtTokenUtil jwtTokenUtil,
                                   UserDetailsService userDetailsService,
                                   JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationExceptionHandler = jwtAuthenticationExceptionHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isOpenEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (jwtSecret == null || jwtSecret.isEmpty()) {
            handleErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT secret key is not configured");
            return;
        }

        Optional<String> jwtOpt = extractJwt(request);
        if (jwtOpt.isEmpty()) {
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token is missing or invalid");
            return;
        }

        String jwt = jwtOpt.get();
        try {
            Claims claims = parseClaims(jwt);
            authenticateUser(claims, response);
        } catch (ExpiredJwtException e) {
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token is expired: " + e.getMessage());
            return;
        } catch (SignatureException e) {
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token signature is invalid: " + e.getMessage());
            return;
        } catch (Exception e) {
            handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token: " + e.getMessage());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isOpenEndpoint(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return requestURI != null && !requestURI.isEmpty() && SecurityConstants.OPEN_ENDPOINTS != null &&
                Arrays.stream(SecurityConstants.OPEN_ENDPOINTS).anyMatch(pattern -> new AntPathRequestMatcher(pattern).matches(request));
    }

    private Optional<String> extractJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(TOKEN_PREFIX))
                .map(h -> h.substring(TOKEN_PREFIX.length()).trim());
    }

    private Claims parseClaims(String jwt) {
        byte[] decodedKey = Base64.getUrlDecoder().decode(jwtSecret);
        SecretKey key = Keys.hmacShaKeyFor(decodedKey);
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    private void authenticateUser(Claims claims, HttpServletResponse response) throws IOException {
        String username = claims.getSubject();
        List<String> roles = claims.get("roles", List.class);

        if (username == null || username.isEmpty() || roles == null || roles.isEmpty()) {
            handleErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT: Missing username or roles");
            return;
        }

        // التأكد من أن الأدوار تحتوي فقط على البادئة "ROLE_"
        for (String role : roles) {
            if (!role.startsWith("ROLE_")) {
                handleErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT: Role " + role + " is not valid");
                return;
            }
        }

        // تحميل تفاصيل المستخدم باستخدام UserDetailsService
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // تحويل الأدوار إلى SimpleGrantedAuthority
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());

        // إنشاء Authentication object
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, authorities);

        // تعيين Authentication في SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private void handleErrorResponse(HttpServletResponse response, int statusCode, String message) {
        logger.error(message);
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try (PrintWriter writer = response.getWriter()) {
            writer.write("{\"error\": \"" + message + "\", \"status\": " + statusCode + "}");
        } catch (IOException e) {
            logger.error("Error writing response", e);
        }
    }
}