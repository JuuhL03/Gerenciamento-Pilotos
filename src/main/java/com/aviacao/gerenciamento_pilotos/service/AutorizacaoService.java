package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.AutorizacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AutorizacaoService {

    private final AutorizacaoRepository autorizacaoRepository;
    private final AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public Page<Autorizacao> listarTodas(Pageable pageable) {
        return autorizacaoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Autorizacao> listarAtivas(Pageable pageable) {
        return autorizacaoRepository.findByAtivaTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<Autorizacao> listarPorAluno(Long alunoId) {
        return autorizacaoRepository.findByAlunoIdOrderByDataAutorizacaoDesc(alunoId);
    }

    @Transactional(readOnly = true)
    public Autorizacao buscarPorId(Long id) {
        return autorizacaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Autorização não encontrada com ID: " + id));
    }

    @Transactional
    public Autorizacao autorizarAluno(Long alunoId, Autorizacao autorizacao) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + alunoId));

        // Pegar o teste mais recente usando o método helper
        Teste testeAtual = aluno.getTesteAtual();

        // Validar se o aluno tem teste e se está aprovado
        if (testeAtual == null || testeAtual.getStatus() != StatusTeste.APROVADO) {
            throw new BusinessException("Aluno precisa estar aprovado para ser autorizado");
        }

        // Validar se já não tem autorização ativa
        if (autorizacaoRepository.existsByAlunoAndAtivaTrue(aluno)) {
            throw new BusinessException("Aluno já possui autorização ativa");
        }

        autorizacao.setAluno(aluno);
        autorizacao.setAtiva(true);

        return autorizacaoRepository.save(autorizacao);
    }

    @Transactional
    public void revogarAutorizacao(Long id) {
        Autorizacao autorizacao = buscarPorId(id);
        autorizacao.setAtiva(false);
        autorizacaoRepository.save(autorizacao);
    }

    @Transactional
    public void deletar(Long id) {
        Autorizacao autorizacao = buscarPorId(id);
        autorizacaoRepository.delete(autorizacao);
    }
}