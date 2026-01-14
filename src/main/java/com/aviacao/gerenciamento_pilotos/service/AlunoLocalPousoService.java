package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoComLocaisEInstrutorDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.LocalPousoSimplesDTO;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.LocalPousoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoLocalPousoService {

    private final AlunoRepository alunoRepository;
    private final LocalPousoRepository localPousoRepository;

    @Transactional(readOnly = true)
    public Page<AlunoComLocaisEInstrutorDTO> listarTodosAlunosComLocaisPouso(Pageable pageable) {
        // Buscar alunos paginados
        Page<Aluno> alunosPage = alunoRepository.findAll(pageable);

        // Converter para DTO com locais de pouso
        List<AlunoComLocaisEInstrutorDTO> dtos = alunosPage.getContent().stream()
                .map(aluno -> {
                    // Buscar locais de pouso do aluno
                    List<LocalPouso> locaisPouso = localPousoRepository.findByAlunoIdWithAlunoFetched(aluno.getId());

                    // Converter locais para DTO simples
                    List<LocalPousoSimplesDTO> locaisDTOs = locaisPouso.stream()
                            .map(LocalPousoSimplesDTO::fromEntity)
                            .collect(Collectors.toList());

                    // Obter instrutor (se houver teste)
                    Long instrutorId = null;
                    String instrutorNome = null;

                    if (!aluno.getTestes().isEmpty()) {
                        var primeiroTeste = aluno.getTestes().get(0);
                        if (primeiroTeste.getAvaliador() != null) {
                            instrutorId = primeiroTeste.getAvaliador().getId();
                            instrutorNome = primeiroTeste.getAvaliador().getNome();
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

        // Retornar Page com os DTOs
        return new PageImpl<>(dtos, pageable, alunosPage.getTotalElements());
    }
}