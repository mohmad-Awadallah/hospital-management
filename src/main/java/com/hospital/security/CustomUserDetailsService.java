package com.hospital.security;

import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // استخدام Lombok لإنشاء constructor
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("محاولة تسجيل دخول فاشلة للمستخدم الذي لا يوجد: {}", username);
                    return new UsernameNotFoundException("المستخدم غير موجود: " + username);
                });

        logger.debug("تم تحميل المستخدم بالاسم: {} والأدوار: {}", user.getUsername(), user.getRoles());

        return CustomUserDetails.build(user); // استخدام CustomUserDetails
    }

    private Set<GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> {
                    String roleName = role.getName().toString();
                    // إذا كانت البادئة تحتوي على ROLE_ROLE_، نقوم بإزالتها
                    if (roleName.startsWith("ROLE_ROLE_")) {
                        roleName = roleName.replace("ROLE_ROLE_", "ROLE_");
                    }
                    // إذا كانت البادئة لا تحتوي على ROLE_، نقوم بإضافتها
                    if (!roleName.startsWith("ROLE_")) {
                        roleName = "ROLE_" + roleName;
                    }
                    return new SimpleGrantedAuthority(roleName); // تحويلها إلى GrantedAuthority
                })
                .collect(Collectors.toSet());
    }

}
