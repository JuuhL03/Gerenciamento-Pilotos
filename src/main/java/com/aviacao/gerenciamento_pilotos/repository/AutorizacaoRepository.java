package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutorizacaoRepository extends JpaRepository<Autorizacao, Long>, JpaSpecificationExecutor<Autorizacao> {

    Page<Autorizacao> findByAtivaTrue(Pageable pageable);

    boolean existsByAlunoAndAtivaTrue(Aluno aluno);

    List<Autorizacao> findByAlunoIdOrderByDataAutorizacaoDesc(Long alunoId);
}