package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
import com.aviacao.gerenciamento_pilotos.dto.request.AutorizarAlunoRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AutorizacaoDTO;
import com.aviacao.gerenciamento_pilotos.service.AutorizacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/autorizacoes")
@RequiredArgsConstructor
public class AutorizacaoController {

    private final AutorizacaoService autorizacaoService;

    @GetMapping
    public ResponseEntity<Page<AutorizacaoDTO>> listar(
            @RequestParam(required = false) Boolean apenasAtivas,
            Pageable pageable) {

        Page<Autorizacao> autorizacoes;

        // só pra forçar o usuari oadmin ser criado dnv

        if (Boolean.TRUE.equals(apenasAtivas)) {
            autorizacoes = autorizacaoService.listarAtivas(pageable);
        } else {
            autorizacoes = autorizacaoService.listarTodas(pageable);
        }

        Page<AutorizacaoDTO> response = autorizacoes.map(AutorizacaoDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<AutorizacaoDTO>> listarPorAluno(@PathVariable Long alunoId) {
        List<Autorizacao> autorizacoes = autorizacaoService.listarPorAluno(alunoId);
        List<AutorizacaoDTO> response = autorizacoes.stream()
                .map(AutorizacaoDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AutorizacaoDTO> buscarPorId(@PathVariable Long id) {
        Autorizacao autorizacao = autorizacaoService.buscarPorId(id);
        return ResponseEntity.ok(AutorizacaoDTO.fromEntity(autorizacao));
    }

    @PostMapping("/aluno/{alunoId}")
    public ResponseEntity<AutorizacaoDTO> autorizar(
            @PathVariable Long alunoId,
            @RequestBody AutorizarAlunoRequest request) {

        Autorizacao autorizacao = new Autorizacao();
        autorizacao.setDataValidade(request.getDataValidade());
        autorizacao.setObservacoes(request.getObservacoes());

        Autorizacao autorizacaoCriada = autorizacaoService.autorizarAluno(alunoId, autorizacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(AutorizacaoDTO.fromEntity(autorizacaoCriada));
    }

    @PatchMapping("/{id}/revogar")
    public ResponseEntity<Void> revogar(@PathVariable Long id) {
        autorizacaoService.revogarAutorizacao(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        autorizacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}