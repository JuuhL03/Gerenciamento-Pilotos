package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public Pagamento cadastrar(Long testeId, BigDecimal valor, String comprovanteBase64, String comprovanteNome, String comprovanteTipo) {
        Teste teste = testeService.buscarPorId(testeId);

        if (pagamentoRepository.existsByTesteId(testeId)) {
            throw new BusinessException("Teste já possui pagamento cadastrado");
        }

        byte[] comprovanteBytes = decodificarBase64(comprovanteBase64);

        Pagamento pagamento = new Pagamento();
        pagamento.setTeste(teste);
        pagamento.setAluno(teste.getAluno()); // ← ADICIONE! Pega o aluno do teste
        pagamento.setPago(true);
        pagamento.setValor(valor != null ? valor : BigDecimal.ZERO);
        pagamento.setComprovanteNome(comprovanteNome);
        pagamento.setComprovanteTipo(comprovanteTipo);
        pagamento.setComprovanteTamanho((long) comprovanteBytes.length);
        pagamento.setComprovanteDados(comprovanteBytes);

        return pagamentoRepository.save(pagamento);
    }

    @Transactional
    public Pagamento atualizar(Long testeId, String comprovanteBase64, String comprovanteNome, String comprovanteTipo) {
        Pagamento pagamento = buscarPorTesteId(testeId);

        byte[] comprovanteBytes = decodificarBase64(comprovanteBase64);

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

    private byte[] decodificarBase64(String base64String) {
        try {
            String base64Limpo = base64String;

            if (base64String.contains(",")) {
                base64Limpo = base64String.split(",")[1];
            }

            base64Limpo = base64Limpo.replaceAll("\\s+", "");

            return Base64.getDecoder().decode(base64Limpo);

        } catch (IllegalArgumentException e) {
            throw new BusinessException("Comprovante base64 inválido: " + e.getMessage());
        }
    }
}