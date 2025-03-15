package com.hospital;

import com.hospital.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private String adminJwtToken;
    private String userJwtToken;

    private static final String ALL_USERS_ENDPOINT = "/api/users";
    private static final String AUTH_ENDPOINT = "/api/auth";

    @BeforeEach
    public void setUp() {
        // إنشاء كائنات UserDetails للمستخدمين
        UserDetails adminDetails = new User("adminUser", "password",
                Collections.singletonList(() -> "ADMIN"));
        UserDetails userDetails = new User("userUser", "password",
                Collections.singletonList(() -> "USER"));

        // إنشاء رموز JWT لكل مستخدم باستخدام JwtTokenUtil
        adminJwtToken = jwtTokenUtil.generateToken(adminDetails);
        userJwtToken = jwtTokenUtil.generateToken(userDetails);
    }

    // اختبار: يجب أن يتمكن المستخدم الذي يمتلك صلاحية ADMIN من الوصول إلى كافة المستخدمين
    @Test
    public void adminUserCanAccessAllUsers() throws Exception {
        mockMvc.perform(get(ALL_USERS_ENDPOINT)
                        .header("Authorization", "Bearer " + adminJwtToken))
                .andExpect(status().isOk());
    }

    // اختبار: يجب ألا يتمكن المستخدم الذي يمتلك صلاحية USER من الوصول إلى كافة المستخدمين
    @Test
    public void userUserCannotAccessAllUsers() throws Exception {
        mockMvc.perform(get(ALL_USERS_ENDPOINT)
                        .header("Authorization", "Bearer " + userJwtToken))
                .andExpect(status().isForbidden())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    // اختبار: يجب أن يتم رفض الوصول في حال عدم توفير JWT (غير مصادق عليه)
    @Test
    public void unauthenticatedUserCannotAccessAllUsers() throws Exception {
        mockMvc.perform(get(ALL_USERS_ENDPOINT))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("JWT token is missing or invalid"));
    }

    // اختبار: يجب أن تكون نقطة النهاية المفتوحة (مثلاً /api/auth) متاحة بدون مصادقة
    @Test
    public void openEndpointIsAccessibleWithoutAuthentication() throws Exception {
        mockMvc.perform(get(AUTH_ENDPOINT))
                .andExpect(status().isOk());
    }
}
