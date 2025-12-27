package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalPousoRepository extends JpaRepository<LocalPouso, Long>, JpaSpecificationExecutor<LocalPouso> {

    List<LocalPouso> findByAlunoIdOrderByDataCadastroDesc(Long alunoId);

    long countByAlunoId(Long alunoId);

    List<LocalPouso> findByNomeContainingIgnoreCase(String nome);
}