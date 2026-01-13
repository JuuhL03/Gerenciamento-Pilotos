package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoComLocaisEInstrutorDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.LocalPousoSimplesDTO;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoLocalPousoService {

    private final AlunoRepository alunoRepository;
    private final LocalPousoService localPousoService;

    @Transactional(readOnly = true)
    public List<AlunoComLocaisEInstrutorDTO> listarTodosAlunosComLocaisPouso() {
        List<Aluno> alunos = alunoRepository.findAll();

        return alunos.stream()
                .map(aluno -> {
                    // ✅ Buscar locais de pouso DESTE aluno
                    List<LocalPouso> locaisDoAluno = localPousoService.listarPorAluno(aluno.getId());

                    // ✅ Converter para DTO SIMPLES (sem dados do aluno)
                    List<LocalPousoSimplesDTO> locaisDTO = locaisDoAluno.stream()
                            .map(LocalPousoSimplesDTO::fromEntity) // ✅ MUDOU
                            .collect(Collectors.toList());

                    // ✅ Buscar instrutor do teste atual
                    Teste testeAtual = aluno.getTesteAtual();
                    Long instrutorId = null;
                    String instrutorNome = null;

                    if (testeAtual != null && testeAtual.getAvaliador() != null) {
                        instrutorId = testeAtual.getAvaliador().getId();
                        instrutorNome = testeAtual.getAvaliador().getNome();
                    }

                    return AlunoComLocaisEInstrutorDTO.builder()
                            .alunoId(aluno.getId())
                            .alunoNome(aluno.getNome())
                            .alunoPassaporte(aluno.getPassaporte())
                            .instrutorId(instrutorId)
                            .instrutorNome(instrutorNome)
                            .locaisPouso(locaisDTO)
                            .build();
                })
                .collect(Collectors.toList());
    }
}