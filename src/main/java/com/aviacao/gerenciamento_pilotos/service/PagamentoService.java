package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.PagamentoRepository;
import com.aviacao.gerenciamento_pilotos.repository.TesteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final TesteRepository testeRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional
    public Pagamento cadastrar(Long testeId, BigDecimal valor, String comprovanteBase64) {
        // Buscar teste
        Teste teste = testeRepository.findById(testeId)
                .orElseThrow(() -> new NotFoundException("Teste não encontrado com ID: " + testeId));

        // Verificar se já existe pagamento para este teste
        if (pagamentoRepository.existsByTesteId(testeId)) {
            throw new BusinessException("Já existe um pagamento cadastrado para este teste");
        }

        Aluno aluno = teste.getAluno();

        Pagamento pagamento = Pagamento.builder()
                .teste(teste)
                .aluno(aluno)
                .valor(valor)
                .pago(true)
                .build();

        // Upload do comprovante no Cloudinary (se fornecido) com pasta do aluno
        if (comprovanteBase64 != null && !comprovanteBase64.trim().isEmpty()) {
            String alunoFolder = cloudinaryService.sanitizeFolderName(aluno.getNome());
            String comprovanteUrl = cloudinaryService.uploadBase64Image(comprovanteBase64, "pagamento", alunoFolder);
            pagamento.setComprovanteUrl(comprovanteUrl);
            log.info("✅ Comprovante enviado para Cloudinary: {}", comprovanteUrl);
        }

        Pagamento saved = pagamentoRepository.save(pagamento);

        // ✅ FORÇA O CARREGAMENTO DAS RELAÇÕES ANTES DE SAIR DA TRANSAÇÃO
        saved.getAluno().getNome();
        saved.getTeste().getId();

        return saved;
    }

    @Transactional
    public Pagamento atualizar(Long testeId, BigDecimal novoValor, String novoComprovanteBase64) {
        Pagamento pagamento = pagamentoRepository.findByTesteId(testeId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado para o teste ID: " + testeId));

        // Atualizar valor se fornecido
        if (novoValor != null) {
            pagamento.setValor(novoValor);
        }

        // Atualizar comprovante se fornecido
        if (novoComprovanteBase64 != null && !novoComprovanteBase64.trim().isEmpty()) {
            // Deletar comprovante antigo se existir
            if (pagamento.getComprovanteUrl() != null) {
                cloudinaryService.deleteImage(pagamento.getComprovanteUrl());
                log.info("✅ Comprovante antigo deletado do Cloudinary");
            }

            // Upload novo comprovante com pasta do aluno
            String alunoFolder = cloudinaryService.sanitizeFolderName(pagamento.getAluno().getNome());
            String novoComprovanteUrl = cloudinaryService.uploadBase64Image(novoComprovanteBase64, "pagamento", alunoFolder);
            pagamento.setComprovanteUrl(novoComprovanteUrl);
            log.info("✅ Novo comprovante enviado para Cloudinary: {}", novoComprovanteUrl);
        }

        Pagamento updated = pagamentoRepository.save(pagamento);

        // ✅ FORÇA O CARREGAMENTO DAS RELAÇÕES
        updated.getAluno().getNome();
        updated.getTeste().getId();

        return updated;
    }

    @Transactional
    public void deletar(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado com ID: " + id));

        // Deletar comprovante do Cloudinary se existir
        if (pagamento.getComprovanteUrl() != null) {
            cloudinaryService.deleteImage(pagamento.getComprovanteUrl());
            log.info("✅ Comprovante deletado do Cloudinary ao deletar pagamento");
        }

        pagamentoRepository.delete(pagamento);
    }

    @Transactional(readOnly = true)
    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findByIdWithRelations(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Pagamento buscarPorTesteId(Long testeId) {
        return pagamentoRepository.findByTesteIdWithRelations(testeId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado para o teste ID: " + testeId));
    }
}