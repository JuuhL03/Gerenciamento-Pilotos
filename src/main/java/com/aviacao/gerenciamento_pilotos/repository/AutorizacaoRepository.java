package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long>, JpaSpecificationExecutor<Autorizacao> {

    List<Autorizacao> findByAlunoOrderByDataAutorizacaoDesc(Aluno aluno);

    Optional<Autorizacao> findByAlunoAndAtivaTrue(Aluno aluno);

    boolean existsByAlunoAndAtivaTrue(Aluno aluno);

    Page<Autorizacao> findByAtivaTrue(Pageable pageable);

    Page<Autorizacao> findByAtivaFalse(Pageable pageable);
}