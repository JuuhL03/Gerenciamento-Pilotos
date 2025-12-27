package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.AuditoriaLogin;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuditoriaLoginRepository extends JpaRepository<AuditoriaLogin, Long>, JpaSpecificationExecutor<AuditoriaLogin> {

    Page<AuditoriaLogin> findByUsuarioOrderByDataTentativaDesc(Usuario usuario, Pageable pageable);

    Page<AuditoriaLogin> findBySucessoOrderByDataTentativaDesc(Boolean sucesso, Pageable pageable);

    List<AuditoriaLogin> findByLoginTentadoAndSucessoFalseAndDataTentativaAfterOrderByDataTentativaDesc(
            String login, LocalDateTime apartirDe);

    long countByLoginTentadoAndSucessoFalseAndDataTentativaAfter(
            String login, LocalDateTime apartirDe);
}