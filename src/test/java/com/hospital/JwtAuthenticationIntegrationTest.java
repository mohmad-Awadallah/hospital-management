package com.hospital;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Test
    void shouldReturnUnauthorizedWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/protected-endpoint")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAuthenticateWithValidJwt() throws Exception {
        System.out.println("JWT Secret: " + jwtSecret);

        String token = createValidJwtToken();

        mockMvc.perform(get("/api/protected-endpoint")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private String createValidJwtToken() {
        byte[] decodedSecret = Base64.getUrlDecoder().decode(jwtSecret);
        System.out.println("Decoded Secret Length: " + decodedSecret.length); // إضافة تسجيل السجلات هنا
        return Jwts.builder()
                .setSubject("admin")
                .claim("roles", "ADMIN")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10000))
                .signWith(SignatureAlgorithm.HS512, decodedSecret)
                .compact();
    }


    // فئة لتوليد المفتاح السري (يجب تشغيلها بشكل منفصل)
    public static class JwtSecretGenerator {
        public static void main(String[] args) {
            byte[] key = new byte[64];
            new SecureRandom().nextBytes(key);
            String encodedKey = Base64.getUrlEncoder().encodeToString(key);
            System.out.println("Generated JWT Secret (URL-safe): " + encodedKey);
        }
    }

}