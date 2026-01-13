package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    @Query("SELECT p FROM Pagamento p JOIN FETCH p.aluno JOIN FETCH p.teste WHERE p.id = :id")
    Optional<Pagamento> findByIdWithRelations(@Param("id") Long id);

    @Query("SELECT p FROM Pagamento p JOIN FETCH p.aluno JOIN FETCH p.teste WHERE p.teste.id = :testeId")
    Optional<Pagamento> findByTesteIdWithRelations(@Param("testeId") Long testeId);

    Optional<Pagamento> findByTesteId(Long testeId);

    boolean existsByTesteId(Long testeId);
}