package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizarPagamentoRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarPagamentoRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.PagamentoDTO;
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
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, true));
    }

    @GetMapping("/teste/{testeId}")
    public ResponseEntity<PagamentoDTO> buscarPorTeste(@PathVariable Long testeId) {
        Pagamento pagamento = pagamentoService.buscarPorTesteId(testeId);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, false));
    }

    @GetMapping("/teste/{testeId}/comprovante")
    public ResponseEntity<PagamentoDTO> buscarPorTesteComComprovante(@PathVariable Long testeId) {
        Pagamento pagamento = pagamentoService.buscarPorTesteId(testeId);
        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, true));
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastrar(@Valid @RequestBody CadastrarPagamentoRequest request) {
        // ✅ CORRIGIDO: Apenas 3 parâmetros
        Pagamento pagamento = pagamentoService.cadastrar(
                request.getTesteId(),
                request.getValor(),
                request.getComprovanteBase64() // ✅ Só o base64 (será convertido em URL)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(PagamentoDTO.fromEntity(pagamento, false));
    }

    @PutMapping("/teste/{testeId}")
    public ResponseEntity<PagamentoDTO> atualizar(
            @PathVariable Long testeId,
            @Valid @RequestBody AtualizarPagamentoRequest request) {

        // ✅ CORRIGIDO: Apenas 3 parâmetros
        Pagamento pagamento = pagamentoService.atualizar(
                testeId,
                request.getValor(),
                request.getComprovanteBase64() // ✅ Só o base64 (será convertido em URL)
        );

        return ResponseEntity.ok(PagamentoDTO.fromEntity(pagamento, false));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        pagamentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}