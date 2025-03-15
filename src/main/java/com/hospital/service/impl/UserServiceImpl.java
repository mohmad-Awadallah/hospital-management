package com.hospital.service.impl;

import com.hospital.exception.DuplicateResourceException;
import com.hospital.exception.ResourceNotFoundException;
import com.hospital.model.Role;
import com.hospital.model.User;
import com.hospital.repository.RoleRepository;
import com.hospital.repository.UserRepository;
import com.hospital.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User assignRolesToUser(Long userId, Set<Long> roleIds) {
        log.info("📌 تعيين أدوار للمستخدم ID: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("❌ المستخدم غير موجود!"));

        Set<Role> roles = roleIds.stream()
                .map(roleRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());

        if (roles.isEmpty()) {
            throw new ResourceNotFoundException("❌ لا توجد أدوار صالحة مضافة للمستخدم!");
        }

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        log.info("✅ تم تحديث أدوار المستخدم: {}", userId);

        return updatedUser;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        log.info("📌 تحديث بيانات المستخدم ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("❌ المستخدم غير موجود!"));

        // التحقق من وجود الاسم أو البريد الإلكتروني في النظام
        if (!user.getUsername().equals(userDetails.getUsername()) && userRepository.existsByUsername(userDetails.getUsername())) {
            throw new DuplicateResourceException("❌ اسم المستخدم مستخدم بالفعل!");
        }
        if (!user.getEmail().equals(userDetails.getEmail()) && userRepository.existsByEmail(userDetails.getEmail())) {
            throw new DuplicateResourceException("❌ البريد الإلكتروني مستخدم بالفعل!");
        }

        // نسخ الخصائص الجديدة مع استبعاد الحقول المهمة مثل كلمة المرور والدور
        BeanUtils.copyProperties(userDetails, user, "id", "password", "roles");

        // تحديث كلمة المرور إذا تم تقديمها
        if (userDetails.getPassword() != null && !userDetails.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        log.info("✅ تم تحديث المستخدم بنجاح: {}", id);
        return updatedUser;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // استخدام orElseThrow للحصول على المستخدم أو رمي استثناء إذا لم يكن موجودًا
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("اسم المستخدم غير موجود"));

        return user;
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        log.info("📌 حذف المستخدم ID: {}", id);

        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("❌ المستخدم غير موجود!");
        }

        userRepository.deleteById(id);
        log.info("✅ تم حذف المستخدم بنجاح: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        log.info("📌 استرجاع جميع المستخدمين...");
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> getUserById(Long id) {
        log.info("📌 البحث عن مستخدم بواسطة ID: {}", id);
        return userRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        log.info("📌 البحث عن مستخدم بواسطة اسم المستخدم: {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        log.info("📌 البحث عن مستخدم بواسطة البريد الإلكتروني: {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}

