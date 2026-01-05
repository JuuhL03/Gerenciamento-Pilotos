package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.TesteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TesteService {

    private final TesteRepository testeRepository;
    private final AlunoService alunoService;
    private final UsuarioService usuarioService;

    @Transactional(readOnly = true)
    public Page<Teste> listarTodos(Pageable pageable) {
        return testeRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Teste buscarPorId(Long id) {
        return testeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Teste não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Teste> listarPorAluno(Long alunoId) {
        return testeRepository.findByAlunoIdOrderByIdDesc(alunoId);
    }

    @Transactional(readOnly = true)
    public Teste buscarTesteAtual(Long alunoId) {
        List<Teste> testes = testeRepository.findByAlunoIdOrderByIdDesc(alunoId);
        if (testes.isEmpty()) {
            throw new NotFoundException("Nenhum teste encontrado para o aluno ID: " + alunoId);
        }
        return testes.get(0);
    }

    @Transactional(readOnly = true)
    public List<Teste> listarPorStatus(StatusTeste status) {
        return testeRepository.findByStatus(status);
    }

    @Transactional
    public Teste cadastrarTeste(Long alunoId, Long avaliadorId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);

        boolean temTesteEmAndamento = testeRepository.existsByAlunoIdAndStatus(alunoId, StatusTeste.EM_ANDAMENTO);

        if (temTesteEmAndamento) {
            throw new BusinessException("Aluno já possui um teste em andamento. Finalize o teste atual antes de criar um novo.");
        }

        Teste teste = new Teste();
        teste.setAluno(aluno);
        teste.setStatus(StatusTeste.EM_ANDAMENTO);
        teste.setAtivo(true);

        if (avaliadorId != null) {
            Usuario avaliador = usuarioService.buscarPorId(avaliadorId);
            teste.setAvaliador(avaliador);
        }

        return testeRepository.save(teste);
    }

    @Transactional
    public Teste atribuirAvaliador(Long testeId, Long avaliadorId) {
        Teste teste = buscarPorId(testeId);
        Usuario avaliador = usuarioService.buscarPorId(avaliadorId);
        teste.setAvaliador(avaliador);
        return testeRepository.save(teste);
    }

    @Transactional
    public Teste aprovar(Long testeId) {
        Teste teste = buscarPorId(testeId);

        if (teste.getStatus() == StatusTeste.APROVADO) {
            throw new BusinessException("Teste já foi aprovado");
        }

        if (teste.getStatus() == StatusTeste.REPROVADO) {
            throw new BusinessException("Teste já foi reprovado");
        }

        teste.setStatus(StatusTeste.APROVADO);
        teste.setDataFinalizacao(LocalDateTime.now());
        return testeRepository.save(teste);
    }

    @Transactional
    public Teste reprovar(Long testeId) {
        Teste teste = buscarPorId(testeId);

        if (teste.getStatus() == StatusTeste.APROVADO) {
            throw new BusinessException("Teste já foi aprovado");
        }

        if (teste.getStatus() == StatusTeste.REPROVADO) {
            throw new BusinessException("Teste já foi reprovado");
        }

        teste.setStatus(StatusTeste.REPROVADO);
        teste.setDataFinalizacao(LocalDateTime.now());
        return testeRepository.save(teste);
    }

    @Transactional
    public Teste alterarStatus(Long testeId, StatusTeste novoStatus) {
        Teste teste = buscarPorId(testeId);
        Long alunoId = teste.getAluno().getId();

        if (novoStatus == StatusTeste.EM_ANDAMENTO && teste.getStatus() != StatusTeste.EM_ANDAMENTO) {
            long testesEmAndamento = testeRepository.countByAlunoIdAndStatus(alunoId, StatusTeste.EM_ANDAMENTO);

            if (testesEmAndamento > 0) {
                Teste testeEmAndamento = testeRepository.findByAlunoIdAndStatus(alunoId, StatusTeste.EM_ANDAMENTO)
                        .orElse(null);

                if (testeEmAndamento != null && !testeEmAndamento.getId().equals(testeId)) {
                    throw new BusinessException("Aluno já possui outro teste em andamento (Passaporte: " + testeEmAndamento.getAluno().getPassaporte() + "). Finalize-o antes de alterar este teste.");
                }
            }
        }

        teste.setStatus(novoStatus);

        if (novoStatus == StatusTeste.APROVADO || novoStatus == StatusTeste.REPROVADO) {
            teste.setDataFinalizacao(LocalDateTime.now());
        } else {
            teste.setDataFinalizacao(null);
        }

        return testeRepository.save(teste);
    }

    @Transactional
    public void deletar(Long id) {
        Teste teste = buscarPorId(id);
        teste.setAtivo(false);
        testeRepository.save(teste);
    }
}