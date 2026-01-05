package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TesteRepository extends JpaRepository<Teste, Long> {

    List<Teste> findByAlunoIdOrderByIdDesc(Long alunoId);

    List<Teste> findByStatus(StatusTeste status);

    Optional<Teste> findByAlunoIdAndStatus(Long alunoId, StatusTeste status);

    boolean existsByAlunoIdAndStatus(Long alunoId, StatusTeste status);

    @Query("SELECT COUNT(t) FROM Teste t WHERE t.aluno.id = :alunoId AND t.status = :status")
    long countByAlunoIdAndStatus(@Param("alunoId") Long alunoId, @Param("status") StatusTeste status);
}