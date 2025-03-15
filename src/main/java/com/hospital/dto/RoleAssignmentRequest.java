package com.hospital.dto;

import java.util.Set;

public class RoleAssignmentRequest {
    private Set<Long> roleIds;

    // Getters and Setters
    public Set<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Set<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
