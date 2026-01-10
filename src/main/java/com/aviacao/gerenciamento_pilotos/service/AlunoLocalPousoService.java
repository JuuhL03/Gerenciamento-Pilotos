package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoLocalPouso;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoComLocaisPousoDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.LocalPousoComAutorizacaoDTO;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoLocalPousoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoLocalPousoService {

    private final AlunoLocalPousoRepository alunoLocalPousoRepository;
    private final AlunoService alunoService;
    private final LocalPousoService localPousoService;

    @Transactional(readOnly = true)
    public AlunoComLocaisPousoDTO listarLocaisPousoDoAluno(Long alunoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        List<AlunoLocalPouso> vinculos = alunoLocalPousoRepository.findByAlunoId(alunoId);
        List<LocalPouso> todosLocais = localPousoService.listarTodos();

        List<LocalPousoComAutorizacaoDTO> locaisComAutorizacao = todosLocais.stream()
                .map(local -> {
                    boolean autorizado = vinculos.stream()
                            .anyMatch(v -> v.getLocalPouso().getId().equals(local.getId()) && v.getAutorizado());

                    return LocalPousoComAutorizacaoDTO.builder()
                            .id(local.getId())
                            .nome(local.getNome())
                            .imagem(local.getImagem())
                            .ativo(local.getAtivo())
                            .autorizado(autorizado)
                            .build();
                })
                .collect(Collectors.toList());

        return AlunoComLocaisPousoDTO.builder()
                .alunoId(aluno.getId())
                .alunoNome(aluno.getNome())
                .alunoPassaporte(aluno.getPassaporte())
                .locaisPouso(locaisComAutorizacao)
                .build();
    }

    @Transactional
    public void autorizarAluno(Long alunoId, Long localPousoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        LocalPouso localPouso = localPousoService.buscarPorId(localPousoId);

        if (alunoLocalPousoRepository.existsByAlunoIdAndLocalPousoId(alunoId, localPousoId)) {
            AlunoLocalPouso vinculo = alunoLocalPousoRepository
                    .findByAlunoIdAndLocalPousoId(alunoId, localPousoId)
                    .orElseThrow();

            vinculo.setAutorizado(true);
            alunoLocalPousoRepository.save(vinculo);
        } else {
            AlunoLocalPouso novoVinculo = new AlunoLocalPouso();
            novoVinculo.setAluno(aluno);
            novoVinculo.setLocalPouso(localPouso);
            novoVinculo.setAutorizado(true);
            alunoLocalPousoRepository.save(novoVinculo);
        }
    }

    @Transactional
    public void desautorizarAluno(Long alunoId, Long localPousoId) {
        AlunoLocalPouso vinculo = alunoLocalPousoRepository
                .findByAlunoIdAndLocalPousoId(alunoId, localPousoId)
                .orElseThrow(() -> new BusinessException("Vínculo não encontrado"));

        alunoLocalPousoRepository.delete(vinculo);
    }
}