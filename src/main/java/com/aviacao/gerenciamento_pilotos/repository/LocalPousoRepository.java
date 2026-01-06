package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocalPousoRepository extends JpaRepository<LocalPouso, Long> {

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    List<LocalPouso> findAllByOrderByNomeAsc();
}