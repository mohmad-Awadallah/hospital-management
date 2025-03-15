package com.hospital.service;

import com.hospital.model.Role;
import com.hospital.model.RoleName;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    // إنشاء دور جديد
    Role createRole(Role role);

    // تحديث دور موجود
    Role updateRole(Long id, Role roleDetails);

    // حذف دور
    void deleteRole(Long id);

    // الحصول على جميع الأدوار
    List<Role> getAllRoles();

    // الحصول على دور بواسطة الـ ID
    Optional<Role> getRoleById(Long id);

    public Optional<Role> findByName(String name);

    public boolean existsByName(RoleName name);
}
