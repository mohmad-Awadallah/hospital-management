package com.hospital;

import com.hospital.security.JwtAuthenticationExceptionHandler;
import com.hospital.security.JwtAuthenticationFilter;
import com.hospital.security.JwtTokenUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;

import static org.mockito.Mockito.*;

@TestPropertySource(properties = "jwt.secret=40EZ5w0fHUfTpNPe8wD0hza96Qt93KMj_adNO_9DWyKMhALpTiPfQvvnR_FKfFmhTTHLlP9IWlmAD2jC_wdRfA==")
class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private JwtTokenUtil jwtTokenUtil;
    private UserDetailsService userDetailsService;
    private JwtAuthenticationExceptionHandler jwtAuthenticationExceptionHandler;

    private String jwtSecret;
    private String validJwt;

    @BeforeEach
    void setUp() {
        // إعداد السر لتوقيع التوكن
        jwtSecret = Base64.getEncoder().encodeToString(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
        jwtTokenUtil = mock(JwtTokenUtil.class);
        userDetailsService = mock(UserDetailsService.class);
        jwtAuthenticationExceptionHandler = mock(JwtAuthenticationExceptionHandler.class);

        // إعداد الفلتر
        jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenUtil, userDetailsService, jwtAuthenticationExceptionHandler);

        // إنشاء JWT صالح
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
        validJwt = Jwts.builder()
                .setSubject("testuser")
                .claim("roles", "USER")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // صالح لمدة ساعة
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void testValidJwt() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // إعداد التوكن الصالح
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validJwt);

        // جعل دالة isTokenValid في JwtTokenUtil ترجع true لأن التوكن صالح
        when(jwtTokenUtil.isTokenValid(anyString())).thenReturn(true);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // التحقق من أن الفلتر يسمح بمرور الطلب
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testMissingJwt() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // محاكاة عدم وجود توكن
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // التحقق من أنه يتم تعيين الحالة إلى Unauthorized
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // التحقق من استدعاء handle في JwtAuthenticationExceptionHandler
        verify(jwtAuthenticationExceptionHandler, times(1)).handle(request, response);
    }


    @Test
    void testInvalidJwt() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // إعداد توكن غير صالح
        String invalidJwt = "Bearer invalid-token";

        // محاكاة حالة التوكن غير الصالح
        when(request.getHeader("Authorization")).thenReturn(invalidJwt);
        when(jwtTokenUtil.isTokenValid(anyString())).thenReturn(false); // التوكن غير صالح

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // التحقق من أنه يتم تعيين الحالة إلى Unauthorized
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // التحقق من أن ExceptionHandler يتم استدعاؤه عند التوكن غير الصالح
        verify(jwtAuthenticationExceptionHandler, times(1)).handle(any(), any());
    }

    @Test
    void testExpiredJwt() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // إنشاء توكن منتهي الصلاحية
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(jwtSecret));
        String expiredJwt = Jwts.builder()
                .setSubject("testuser")
                .claim("roles", "USER")
                .setIssuedAt(new Date(System.currentTimeMillis() - 1000 * 60 * 60)) // تاريخ إصدار قديم
                .setExpiration(new Date(System.currentTimeMillis() - 1000 * 60)) // تاريخ انتهاء قديم
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // إعداد التوكن المنتهي الصلاحية
        when(request.getHeader("Authorization")).thenReturn("Bearer " + expiredJwt);
        when(jwtTokenUtil.isTokenValid(anyString())).thenReturn(false); // التوكن منتهي الصلاحية

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // التحقق من أنه يتم تعيين الحالة إلى Unauthorized
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // التحقق من أن ExceptionHandler يتم استدعاؤه عند التوكن منتهي الصلاحية
        verify(jwtAuthenticationExceptionHandler, times(1)).handle(any(), any());
    }

    @Test
    void testInvalidAuthorizationHeader() throws ServletException, IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        // محاكاة ترويسة غير صالحة
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader");

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        // التحقق من أنه يتم تعيين الحالة إلى Unauthorized
        verify(response, times(1)).setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // التحقق من أن ExceptionHandler يتم استدعاؤه عند وجود ترويسة غير صالحة
        verify(jwtAuthenticationExceptionHandler, times(1)).handle(any(), any());
    }
}
