package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusPagamento;
import com.aviacao.gerenciamento_pilotos.domain.enums.TipoPagamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long>, JpaSpecificationExecutor<Pagamento> {

    List<Pagamento> findByAlunoOrderByDataGeracaoDesc(Aluno aluno);

    Page<Pagamento> findByStatus(StatusPagamento status, Pageable pageable);

    Page<Pagamento> findByTipo(TipoPagamento tipo, Pageable pageable);

    List<Pagamento> findByAlunoAndStatus(Aluno aluno, StatusPagamento status);

    List<Pagamento> findByAlunoAndStatusIn(Aluno aluno, List<StatusPagamento> status);

    long countByAlunoAndStatus(Aluno aluno, StatusPagamento status);
}