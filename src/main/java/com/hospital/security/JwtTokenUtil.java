package com.hospital.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    private static final int REQUIRED_KEY_LENGTH = 64;
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
    private static final long RENEWAL_THRESHOLD = 600_000;
    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    private final ConcurrentHashMap<String, Key> keyRing = new ConcurrentHashMap<>();
    private Key currentSigningKey;
    private String currentKeyId;

    @Value("${jwt.expiration:3600}") // القيمة الافتراضية: 3600 ثانية (ساعة واحدة)
    private Long expiration;

    public JwtTokenUtil(@Value("${jwt.secret}") String secret) {
        initializeSigningKey(secret, "default-key-id");
    }

    private void initializeSigningKey(String secret, String keyId) {
        try {
            byte[] keyBytes = Base64.getUrlDecoder().decode(secret);
            if (keyBytes.length != REQUIRED_KEY_LENGTH) {
                throw new IllegalArgumentException("المفتاح يجب أن يكون 512 بت (64 بايت). الطول الحالي: " + keyBytes.length);
            }
            this.currentSigningKey = Keys.hmacShaKeyFor(keyBytes);
            this.currentKeyId = keyId;
            keyRing.put(keyId, currentSigningKey);
            logger.info("تم تهيئة مفتاح التوقيع بنجاح");
        } catch (IllegalArgumentException e) {
            logger.error("خطأ في تهيئة JWT: {}", e.getMessage());
            throw new JwtAuthenticationException("خطأ في تكوين JWT", ErrorCodes.CONFIG_ERROR, e);
        }
    }

    public String generateToken(UserDetails userDetails) {
        Objects.requireNonNull(userDetails, "UserDetails لا يمكن أن يكون null");
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        if (roles.isEmpty()) {
            throw new IllegalArgumentException("UserDetails يجب أن يحتوي على أدوار (Roles)");
        }

        return Jwts.builder()
                .setHeaderParam("kid", currentKeyId)  // تعيين معرف المفتاح
                .setId(UUID.randomUUID().toString())
                .setSubject(userDetails.getUsername())
                .claim("roles", roles)
                .setIssuer("HospitalAuthService")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(expiration)))
                .signWith(currentSigningKey, SIGNATURE_ALGORITHM)
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            String keyId = getKeyIdFromToken(token);  // الحصول على معرف المفتاح من التوكن
            Key validationKey = keyRing.getOrDefault(keyId, currentSigningKey);  // التحقق من المفتاح الصحيح
            return Jwts.parserBuilder()
                    .setSigningKey(validationKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.error("التوكن منتهي الصلاحية: {}", e.getMessage());
            throw new JwtAuthenticationException("التوكن منتهي الصلاحية", ErrorCodes.TOKEN_EXPIRED, e);
        } catch (MalformedJwtException e) {
            logger.error("التوكن تالف: {}", e.getMessage());
            throw new JwtAuthenticationException("التوكن تالف", ErrorCodes.TOKEN_INVALID, e);
        } catch (SignatureException e) {
            logger.error("توقيع التوكن غير صالح: {}", e.getMessage());
            throw new JwtAuthenticationException("توقيع التوكن غير صالح", ErrorCodes.TOKEN_INVALID, e);
        } catch (JwtException e) {
            logger.error("التوكن غير صالح: {}", e.getMessage());
            throw new JwtAuthenticationException("التوكن غير صالح", ErrorCodes.TOKEN_INVALID, e);
        }
    }

    private String getKeyIdFromToken(String token) {
        try {
            Jws<Claims> jws = Jwts.parserBuilder().build().parseClaimsJws(token);
            String keyId = jws.getHeader().getKeyId();
            if (keyId == null || keyId.isEmpty()) {
                logger.warn("معرف المفتاح غير موجود، استخدام المفتاح الافتراضي");
                return currentKeyId;
            }
            return keyId;
        } catch (Exception e) {
            logger.warn("فشل في استخراج معرف المفتاح، استخدام المفتاح الافتراضي: {}", e.getMessage());
            return currentKeyId;
        }
    }

    public static class ErrorCodes {
        public static final String TOKEN_EXPIRED = "JWT_001";
        public static final String TOKEN_INVALID = "JWT_002";
        public static final String CONFIG_ERROR = "JWT_003";
        public static final String KEY_MANAGEMENT_ERROR = "JWT_004";
    }
}