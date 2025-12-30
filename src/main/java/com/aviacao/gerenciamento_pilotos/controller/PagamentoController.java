package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarPagamentoRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.PagamentoDTO;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> buscarPorId(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.buscarPorId(id);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, false));
    }

    @GetMapping("/{id}/comprovante")
    public ResponseEntity<PagamentoDTO> buscarComComprovante(@PathVariable Long id) {
        Pagamento pagamento = pagamentoService.buscarPorId(id);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, true));  // ← Inclui imagem
    }

    @GetMapping("/teste/{testeId}")
    public ResponseEntity<PagamentoDTO> buscarPorTeste(@PathVariable Long testeId) {
        Pagamento pagamento = pagamentoService.buscarPorTesteId(testeId);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, false));
    }

    @GetMapping("/teste/{testeId}/comprovante")
    public ResponseEntity<PagamentoDTO> buscarPorTesteComComprovante(@PathVariable Long testeId) {
        Pagamento pagamento = pagamentoService.buscarPorTesteId(testeId);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, true));  // ← Inclui imagem
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastrar(@Valid @RequestBody CadastrarPagamentoRequest request) {
        System.out.println("========================================");
        System.out.println("📨 POST /api/pagamentos");
        System.out.println("🔹 TesteId: " + request.getTesteId());
        System.out.println("🔹 Nome: " + request.getComprovanteNome());
        System.out.println("🔹 Tipo: " + request.getComprovanteTipo());
        System.out.println("🔹 Base64 length: " + (request.getComprovanteBase64() != null ? request.getComprovanteBase64().length() : "NULL"));
        System.out.println("========================================");

        try {
            Pagamento pagamento = pagamentoService.cadastrar(
                    request.getTesteId(),
                    request.getComprovanteBase64(),
                    request.getComprovanteNome(),
                    request.getComprovanteTipo()
            );

            System.out.println("✅ Pagamento cadastrado com sucesso! ID: " + pagamento.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoDTO.fromEntity(pagamento, false));

        } catch (NotFoundException e) {
            System.err.println("❌ NOT FOUND: " + e.getMessage());
            throw e;
        } catch (BusinessException e) {
            System.err.println("❌ BUSINESS EXCEPTION: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            System.err.println("❌ ERRO INESPERADO: " + e.getClass().getName());
            System.err.println("❌ Mensagem: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping("/teste/{testeId}")
    public ResponseEntity<PagamentoDTO> atualizar(
            @PathVariable Long testeId,
            @Valid @RequestBody CadastrarPagamentoRequest request) {
        Pagamento pagamento = pagamentoService.atualizar(
                testeId,
                request.getComprovanteBase64(),
                request.getComprovanteNome(),
                request.getComprovanteTipo()
        );
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}