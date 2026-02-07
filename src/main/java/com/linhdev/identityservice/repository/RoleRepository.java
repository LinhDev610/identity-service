package com.linhdev.identityservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.linhdev.identityservice.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
