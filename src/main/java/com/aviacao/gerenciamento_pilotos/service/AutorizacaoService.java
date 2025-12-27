package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
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
    private final AlunoService alunoService;
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
        Aluno aluno = alunoService.buscarPorId(alunoId);
        return autorizacaoRepository.findByAlunoOrderByDataAutorizacaoDesc(aluno);
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

        // Validar se o aluno está aprovado
        if (aluno.getTeste() == null || aluno.getTeste().getStatus() != StatusTeste.APROVADO) {
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
    public void revogarAutorizacao(Long autorizacaoId) {
        Autorizacao autorizacao = buscarPorId(autorizacaoId);
        autorizacao.setAtiva(false);
        autorizacaoRepository.save(autorizacao);
    }

    @Transactional
    public void deletar(Long id) {
        Autorizacao autorizacao = buscarPorId(id);
        autorizacaoRepository.delete(autorizacao);
    }
}