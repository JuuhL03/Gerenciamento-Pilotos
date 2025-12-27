package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TesteRepository extends JpaRepository<Teste, Long>, JpaSpecificationExecutor<Teste> {

    List<Teste> findByAlunoIdOrderByIdDesc(Long alunoId);

    @Query("SELECT t FROM Teste t WHERE t.aluno.id = :alunoId ORDER BY t.id DESC LIMIT 1")
    Optional<Teste> findTesteAtualByAlunoId(Long alunoId);

    List<Teste> findByStatus(StatusTeste status);

    boolean existsByAlunoId(Long alunoId);
}