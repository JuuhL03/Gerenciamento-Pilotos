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
        System.out.println("🟢 [SERVICE] Iniciando cadastro de pagamento");
        System.out.println("🟢 [SERVICE] TesteId: " + testeId);

        try {
            // 1. Buscar teste
            System.out.println("🔍 [SERVICE] Buscando teste...");
            Teste teste = testeService.buscarPorId(testeId);
            System.out.println("✅ [SERVICE] Teste encontrado: " + teste.getId());

            // 2. Verificar duplicação
            System.out.println("🔍 [SERVICE] Verificando se já existe pagamento...");
            boolean jaExiste = pagamentoRepository.existsByTesteId(testeId);
            System.out.println("🔍 [SERVICE] Já existe? " + jaExiste);

            if (jaExiste) {
                System.err.println("❌ [SERVICE] Teste já possui pagamento!");
                throw new BusinessException("Teste já possui pagamento cadastrado");
            }

            // 3. Decodificar base64
            System.out.println("🔄 [SERVICE] Decodificando base64...");
            byte[] comprovanteBytes = decodificarBase64(comprovanteBase64);
            System.out.println("✅ [SERVICE] Base64 decodificado: " + comprovanteBytes.length + " bytes");

            // 4. Criar entidade
            System.out.println("💾 [SERVICE] Criando entidade Pagamento...");
            Pagamento pagamento = new Pagamento();
            pagamento.setTeste(teste);
            pagamento.setPago(true);
            pagamento.setComprovanteNome(comprovanteNome);
            pagamento.setComprovanteTipo(comprovanteTipo);
            pagamento.setComprovanteTamanho((long) comprovanteBytes.length);
            pagamento.setComprovanteDados(comprovanteBytes);

            // 5. Salvar
            System.out.println("💾 [SERVICE] Salvando no banco...");
            Pagamento saved = pagamentoRepository.save(pagamento);
            System.out.println("✅ [SERVICE] Pagamento salvo! ID: " + saved.getId());

            return saved;

        } catch (Exception e) {
            System.err.println("❌ [SERVICE] ERRO: " + e.getClass().getName());
            System.err.println("❌ [SERVICE] Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
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