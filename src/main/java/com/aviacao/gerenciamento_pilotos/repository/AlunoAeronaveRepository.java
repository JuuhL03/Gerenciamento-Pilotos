package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoAeronaveRepository extends JpaRepository<AlunoAeronave, Long> {

    Optional<AlunoAeronave> findByAlunoIdAndAeronaveId(Long alunoId, Long aeronaveId);

    List<AlunoAeronave> findByAlunoId(Long alunoId);
}