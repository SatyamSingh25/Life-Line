package com.hu22.bloodBankBackendPrivate.repositories;

import com.hu22.bloodBankBackendPrivate.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}
