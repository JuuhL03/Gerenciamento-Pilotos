package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AeronaveRepository extends JpaRepository<Aeronave, Long> {

    Optional<Aeronave> findByIdAndAtivaTrue(Long id);

    List<Aeronave> findByAtivaTrue();

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);
}