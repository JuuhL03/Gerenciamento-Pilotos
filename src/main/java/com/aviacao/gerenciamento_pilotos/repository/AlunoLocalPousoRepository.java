package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoLocalPouso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoLocalPousoRepository extends JpaRepository<AlunoLocalPouso, Long> {

    List<AlunoLocalPouso> findByAlunoId(Long alunoId);

    Optional<AlunoLocalPouso> findByAlunoIdAndLocalPousoId(Long alunoId, Long localPousoId);

    boolean existsByAlunoIdAndLocalPousoId(Long alunoId, Long localPousoId);

    void deleteByAlunoIdAndLocalPousoId(Long alunoId, Long localPousoId);
}