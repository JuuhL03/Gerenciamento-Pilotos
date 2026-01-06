package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoLocalPouso;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoLocalPousoDTO;
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
    public List<AlunoLocalPousoDTO> listarLocaisPousoDoAluno(Long alunoId) {
        // ✅ Busca o aluno
        Aluno aluno = alunoService.buscarPorId(alunoId);

        // ✅ Busca os vínculos do aluno com locais de pouso
        List<AlunoLocalPouso> vinculos = alunoLocalPousoRepository.findByAlunoId(alunoId);

        // ✅ Busca todos os locais de pouso ativos
        List<LocalPouso> todosLocais = localPousoService.listarTodos();

        return todosLocais.stream()
                .map(local -> {
                    boolean autorizado = vinculos.stream()
                            .anyMatch(v -> v.getLocalPouso().getId().equals(local.getId()) && v.getAutorizado());

                    return AlunoLocalPousoDTO.builder()
                            // ✅ Dados do Aluno
                            .alunoId(aluno.getId())
                            .alunoNome(aluno.getNome())
                            .alunoPassaporte(aluno.getPassaporte())
                            // ✅ Dados do Local de Pouso
                            .localPousoId(local.getId())
                            .localPousoNome(local.getNome())
                            .localPousoImagem(local.getImagem())
                            // ✅ Status
                            .autorizado(autorizado)
                            .build();
                })
                .collect(Collectors.toList());
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