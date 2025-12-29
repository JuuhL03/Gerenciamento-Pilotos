package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final TesteService testeService;

    @Transactional(readOnly = true)
    public Pagamento buscarPorId(Long id) {
        return pagamentoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Pagamento buscarPorTesteId(Long testeId) {
        return pagamentoRepository.findByTesteId(testeId)
                .orElseThrow(() -> new NotFoundException("Pagamento não encontrado para o teste ID: " + testeId));
    }

    @Transactional
    public Pagamento cadastrar(Long testeId, String comprovanteBase64, String comprovanteNome, String comprovanteTipo) {
        Teste teste = testeService.buscarPorId(testeId);

        // Verificar se já existe pagamento para este teste
        if (pagamentoRepository.existsByTesteId(testeId)) {
            throw new BusinessException("Teste já possui pagamento cadastrado");
        }

        // Decodificar base64 para bytes
        byte[] comprovanteBytes;
        try {
            comprovanteBytes = Base64.getDecoder().decode(comprovanteBase64);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Comprovante base64 inválido");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setTeste(teste);
        pagamento.setPago(true);
        pagamento.setComprovanteNome(comprovanteNome);
        pagamento.setComprovanteTipo(comprovanteTipo);
        pagamento.setComprovanteTamanho((long) comprovanteBytes.length);
        pagamento.setComprovanteDados(comprovanteBytes);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento atualizar(Long testeId, String comprovanteBase64, String comprovanteNome, String comprovanteTipo) {
        Pagamento pagamento = buscarPorTesteId(testeId);

        // Decodificar base64 para bytes
        byte[] comprovanteBytes;
        try {
            comprovanteBytes = Base64.getDecoder().decode(comprovanteBase64);
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Comprovante base64 inválido");
        }

        pagamento.setComprovanteNome(comprovanteNome);
        pagamento.setComprovanteTipo(comprovanteTipo);
        pagamento.setComprovanteTamanho((long) comprovanteBytes.length);
        pagamento.setComprovanteDados(comprovanteBytes);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public void deletar(Long id) {
        Pagamento pagamento = buscarPorId(id);
        pagamentoRepository.delete(pagamento);
    }
}