package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.RefreshToken;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.dto.request.LoginRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.LoginResponse;
import com.aviacao.gerenciamento_pilotos.dto.response.UsuarioDTO;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.UnauthorizedException;
import com.aviacao.gerenciamento_pilotos.repository.UsuarioRepository;
import com.aviacao.gerenciamento_pilotos.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuditoriaLoginService auditoriaLoginService;

    @Transactional
    public LoginResponse login(LoginRequest request, String ipAddress, String userAgent) {
        Usuario usuario = usuarioRepository.findByLogin(request.getLogin())
                .orElseThrow(() -> {
                    auditoriaLoginService.registrarTentativa(request.getLogin(), false, ipAddress, userAgent, null);
                    return new UnauthorizedException("Credenciais inválidas");
                });

        if (!usuario.getAtivo()) {
            auditoriaLoginService.registrarTentativa(request.getLogin(), false, ipAddress, userAgent, usuario);
            throw new UnauthorizedException("Usuário inativo");
        }

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
            auditoriaLoginService.registrarTentativa(request.getLogin(), false, ipAddress, userAgent, usuario);
            throw new UnauthorizedException("Credenciais inválidas");
        }

        auditoriaLoginService.registrarTentativa(request.getLogin(), true, ipAddress, userAgent, usuario);

        String accessToken = jwtService.gerarToken(usuario);
        RefreshToken refreshToken = refreshTokenService.criarRefreshToken(usuario);

        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tipo("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .usuario(UsuarioDTO.fromEntity(usuario))
                .build();
    }

    @Transactional
    public LoginResponse refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenService.validarRefreshToken(refreshTokenStr);
        Usuario usuario = refreshToken.getUsuario();

        String newAccessToken = jwtService.gerarToken(usuario);
        RefreshToken newRefreshToken = refreshTokenService.criarRefreshToken(usuario);

        refreshTokenService.revogarRefreshToken(refreshTokenStr);

        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .tipo("Bearer")
                .expiresIn(jwtService.getExpirationTime())
                .usuario(UsuarioDTO.fromEntity(usuario))
                .build();
    }

    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()) {
            throw new BusinessException("Refresh token é obrigatório para fazer logout");
        }
        refreshTokenService.revogarRefreshToken(refreshToken);
    }

    @Transactional(readOnly = true)
    public UsuarioDTO obterUsuarioLogado(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new UnauthorizedException("Usuário não encontrado"));
        return UsuarioDTO.fromEntity(usuario);
    }
}