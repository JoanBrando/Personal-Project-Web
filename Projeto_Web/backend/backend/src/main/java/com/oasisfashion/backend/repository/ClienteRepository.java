package com.oasisfashion.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasisfashion.backend.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
