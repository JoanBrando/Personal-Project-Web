package com.oasisfashion.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oasisfashion.backend.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {}