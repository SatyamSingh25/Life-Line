package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.Role;
import com.hu22.bloodBankBackendPrivate.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
    User findByPhone(String phone);
    List<User> findByRolesRoleName(String role);
    List<User> findByCity(String city);
    List<User> findByRolesRoleNameAndCity(String role, String city);
}
