package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoAeronaveRepository extends JpaRepository<AlunoAeronave, Long>, JpaSpecificationExecutor<AlunoAeronave> {

    List<AlunoAeronave> findByAluno(Aluno aluno);

    List<AlunoAeronave> findByAeronave(Aeronave aeronave);

    boolean existsByAlunoAndAeronave(Aluno aluno, Aeronave aeronave);

    Optional<AlunoAeronave> findByAlunoAndAeronave(Aluno aluno, Aeronave aeronave);

    long countByAluno(Aluno aluno);

    void deleteByAlunoAndAeronave(Aluno aluno, Aeronave aeronave);
}