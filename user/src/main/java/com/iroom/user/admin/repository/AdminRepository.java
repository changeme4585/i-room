package com.iroom.user.admin.repository;

import com.iroom.user.admin.entity.Admin;
import com.iroom.modulecommon.enums.AdminRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(exported = false)
public interface AdminRepository extends JpaRepository<Admin,Long> {
    boolean existsByEmail(String email);
    boolean existsByRole(AdminRole role);
    Optional<Admin> findByEmail(String email);
    Page<Admin> findByNameContaining(String name, Pageable pageable);
    Page<Admin> findByEmailContaining(String email, Pageable pageable);
    Page<Admin> findByRole(AdminRole role, Pageable pageable);
}
