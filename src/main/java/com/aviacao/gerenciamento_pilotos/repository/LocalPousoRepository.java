package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalPousoRepository extends JpaRepository<LocalPouso, Long> {

    @Query("SELECT lp FROM LocalPouso lp JOIN FETCH lp.aluno WHERE lp.aluno.id = :alunoId AND lp.ativo = true ORDER BY lp.nome ASC")
    List<LocalPouso> findByAlunoIdWithAlunoFetched(@Param("alunoId") Long alunoId);

    List<LocalPouso> findByAlunoIdOrderByNomeAsc(Long alunoId);

    boolean existsByNomeAndAlunoId(String nome, Long alunoId);

    boolean existsByNomeAndAlunoIdAndIdNot(String nome, Long alunoId, Long id);
}