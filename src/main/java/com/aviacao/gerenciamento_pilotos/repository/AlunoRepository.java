package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByIdAndAtivoTrue(Long id);

    Optional<Aluno> findByPassaporteAndAtivoTrue(Integer passaporte);

    Page<Aluno> findByAtivoTrue(Pageable pageable);

    List<Aluno> findByAtivoTrue();

    boolean existsByPassaporte(Integer passaporte);

    boolean existsByPassaporteAndIdNot(Integer passaporte, Long id);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "LEFT JOIN FETCH a.testes t " +
            "WHERE a.ativo = true " +
            "AND (t.ativo = true OR t IS NULL)")
    Page<Aluno> findAllWithTestes(Pageable pageable);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "LEFT JOIN FETCH a.testes t " +
            "WHERE a.ativo = true " +
            "AND (LOWER(a.nome) LIKE LOWER(CONCAT('%', :busca, '%')) " +
            "OR CAST(a.passaporte AS string) LIKE CONCAT('%', :busca, '%')) " +
            "AND (t.ativo = true OR t IS NULL)")
    Page<Aluno> findByBuscaWithTestes(@Param("busca") String busca, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "LEFT JOIN FETCH a.testes t " +
            "WHERE a.ativo = true " +
            "AND t.ativo = true " +
            "AND t.status = :status")
    Page<Aluno> findByStatusWithTestes(@Param("status") StatusTeste status, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "LEFT JOIN FETCH a.testes t " +
            "WHERE a.ativo = true " +
            "AND (LOWER(a.nome) LIKE LOWER(CONCAT('%', :busca, '%')) " +
            "OR CAST(a.passaporte AS string) LIKE CONCAT('%', :busca, '%')) " +
            "AND t.ativo = true " +
            "AND t.status = :status")
    Page<Aluno> findByBuscaAndStatusWithTestes(@Param("busca") String busca, @Param("status") StatusTeste status, Pageable pageable);

    @Query("SELECT DISTINCT a FROM Aluno a " +
            "LEFT JOIN FETCH a.testes t " +
            "LEFT JOIN FETCH t.pagamento " +
            "LEFT JOIN FETCH t.avaliador " +
            "WHERE a.id = :id AND a.ativo = true")
    Optional<Aluno> findByIdWithTestes(@Param("id") Long id);
}