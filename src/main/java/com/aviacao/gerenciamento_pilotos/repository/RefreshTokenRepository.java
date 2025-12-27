package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.RefreshToken;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long>, JpaSpecificationExecutor<RefreshToken> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByTokenAndDataExpiracaoAfterAndRevogadoFalse(String token, LocalDateTime agora);

    void deleteByUsuario(Usuario usuario);

    void deleteByDataExpiracaoBefore(LocalDateTime agora);
}