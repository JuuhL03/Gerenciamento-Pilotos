package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.AuditoriaLogin;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.repository.AuditoriaLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditoriaLoginService {

    private final AuditoriaLoginRepository auditoriaLoginRepository;

    @Transactional
    public void registrarTentativa(String login, boolean sucesso, String ip, String userAgent, Usuario usuario) {
        AuditoriaLogin auditoria = new AuditoriaLogin();
        auditoria.setLoginTentado(login);
        auditoria.setSucesso(sucesso);
        auditoria.setIpAddress(ip);
        auditoria.setUserAgent(userAgent);
        auditoria.setUsuario(usuario);

        auditoriaLoginRepository.save(auditoria);
    }
}