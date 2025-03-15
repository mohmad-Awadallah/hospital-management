package com.hospital.repository;

import com.hospital.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // البحث عن مستخدم بواسطة اسم المستخدم
    @EntityGraph(attributePaths = {"roles"}) // جلب الأدوار مع المستخدم
    Optional<User> findByUsername(String username);


    // البحث عن مستخدم بواسطة البريد الإلكتروني
    Optional<User> findByEmail(String email);

    // التحقق من وجود مستخدم بواسطة اسم المستخدم
    boolean existsByUsername(String username);

    // التحقق من وجود مستخدم بواسطة البريد الإلكتروني
    boolean existsByEmail(String email);


}