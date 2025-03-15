package com.hospital.service;


import com.hospital.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    public User assignRolesToUser(Long userId, Set<Long> roleIds);

    public UserDetails loadUserByUsername(String username);


    public User updateUser(Long id, User userDetails);

    public void deleteUser(Long id);

    public List<User> getAllUsers();

    public Optional<User> getUserById(Long id);

    public Optional<User> findByUsername(String username);

    public Optional<User> findByEmail(String email);

    public boolean existsByUsername(String username);


    public boolean existsByEmail(String email);
}