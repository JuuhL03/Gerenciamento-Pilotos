package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AeronaveRepository extends JpaRepository<Aeronave, Long>, JpaSpecificationExecutor<Aeronave> {

    Optional<Aeronave> findByNome(String nome);

    boolean existsByNome(String nome);

    List<Aeronave> findByAtivaTrue();

    Page<Aeronave> findByAtivaTrue(Pageable pageable);

    List<Aeronave> findByCategoria(String categoria);
}