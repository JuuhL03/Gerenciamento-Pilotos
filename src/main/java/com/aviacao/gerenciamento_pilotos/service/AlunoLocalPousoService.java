package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoComLocaisEInstrutorDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.LocalPousoSimplesDTO;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.LocalPousoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlunoLocalPousoService {

    private final AlunoRepository alunoRepository;
    private final LocalPousoRepository localPousoRepository;

    @Transactional(readOnly = true)
    public Page<AlunoComLocaisEInstrutorDTO> listarTodosAlunosComLocaisPouso(Pageable pageable) {
        log.info("🔍 Buscando alunos com teste AUTORIZADO - Paginação: {}", pageable);

        // ✅ Buscar SOMENTE alunos que TÊM teste AUTORIZADO
        Page<Aluno> alunosPage = alunoRepository.findAlunosComTesteAutorizado(pageable);

        log.info("✅ Encontrados {} alunos com teste AUTORIZADO", alunosPage.getTotalElements());

        List<AlunoComLocaisEInstrutorDTO> dtos = alunosPage.getContent().stream()
                .map(aluno -> {
                    log.debug("👤 Processando aluno: {} (tem teste autorizado: {})",
                            aluno.getNome(), aluno.temTesteAutorizado());

                    // Buscar locais de pouso do aluno
                    List<LocalPouso> locaisPouso = localPousoRepository.findByAlunoIdWithAlunoFetched(aluno.getId());

                    // Converter locais para DTO simples
                    List<LocalPousoSimplesDTO> locaisDTOs = locaisPouso.stream()
                            .map(LocalPousoSimplesDTO::fromEntity)
                            .collect(Collectors.toList());

                    // Obter instrutor do primeiro teste AUTORIZADO
                    Long instrutorId = null;
                    String instrutorNome = null;

                    if (aluno.getTestes() != null) {
                        var testeAutorizado = aluno.getTestes().stream()
                                .filter(t -> t.getAtivo() && t.getStatus() == StatusTeste.APROVADO)
                                .findFirst()
                                .orElse(null);

                        if (testeAutorizado != null && testeAutorizado.getAvaliador() != null) {
                            instrutorId = testeAutorizado.getAvaliador().getId();
                            instrutorNome = testeAutorizado.getAvaliador().getNome();
                        }
                    }

                    return AlunoComLocaisEInstrutorDTO.builder()
                            .alunoId(aluno.getId())
                            .alunoNome(aluno.getNome())
                            .alunoPassaporte(aluno.getPassaporte())
                            .instrutorId(instrutorId)
                            .instrutorNome(instrutorNome)
                            .locaisPouso(locaisDTOs)
                            .build();
                })
                .collect(Collectors.toList());

        log.info("✅ Retornando {} alunos com locais de pouso", dtos.size());

        return new PageImpl<>(dtos, pageable, alunosPage.getTotalElements());
    }
}