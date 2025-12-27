package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.RefreshToken;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.exception.UnauthorizedException;
import com.aviacao.gerenciamento_pilotos.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.refresh.expiration}")
    private Long refreshExpiration;

    @Transactional
    public RefreshToken criarRefreshToken(Usuario usuario) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUsuario(usuario);
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setDataExpiracao(LocalDateTime.now().plusSeconds(refreshExpiration / 1000));
        refreshToken.setRevogado(false);

        return refreshTokenRepository.save(refreshToken);
    }

    @Transactional(readOnly = true)
    public RefreshToken validarRefreshToken(String token) {
        return refreshTokenRepository.findByTokenAndDataExpiracaoAfterAndRevogadoFalse(token, LocalDateTime.now())
                .orElseThrow(() -> new UnauthorizedException("Refresh token inválido ou expirado"));
    }

    @Transactional
    public void revogarRefreshToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(rt -> {
            rt.setRevogado(true);
            refreshTokenRepository.save(rt);
        });
    }

    @Transactional
    public void revogarTodosDoUsuario(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);
    }

    @Transactional
    public void limparTokensExpirados() {
        refreshTokenRepository.deleteByDataExpiracaoBefore(LocalDateTime.now());
    }
}