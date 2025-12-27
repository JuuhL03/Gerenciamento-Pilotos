package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.dto.request.AlterarStatusTesteRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.AtribuirAvaliadorRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarTesteRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.TesteDTO;
import com.aviacao.gerenciamento_pilotos.service.TesteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/testes")
@RequiredArgsConstructor
public class TesteController {

    private final TesteService testeService;

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<List<TesteDTO>> listarPorAluno(@PathVariable Long alunoId) {
        List<Teste> testes = testeService.listarPorAluno(alunoId);
        List<TesteDTO> response = testes.stream()
                .map(TesteDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/aluno/{alunoId}/atual")
    public ResponseEntity<TesteDTO> buscarTesteAtual(@PathVariable Long alunoId) {
        Teste teste = testeService.buscarTesteAtual(alunoId);
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TesteDTO> buscarPorId(@PathVariable Long id) {
        Teste teste = testeService.buscarPorId(id);
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @PostMapping
    public ResponseEntity<TesteDTO> cadastrar(@Valid @RequestBody CadastrarTesteRequest request) {
        Teste teste = testeService.cadastrarTeste(request.getAlunoId(), request.getAvaliadorId());
        return ResponseEntity.status(HttpStatus.CREATED).body(TesteDTO.fromEntity(teste));
    }

    @PatchMapping("/{id}/atribuir-avaliador")
    public ResponseEntity<TesteDTO> atribuirAvaliador(
            @PathVariable Long id,
            @Valid @RequestBody AtribuirAvaliadorRequest request) {
        Teste teste = testeService.atribuirAvaliador(id, request.getAvaliadorId());
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<TesteDTO> aprovar(@PathVariable Long id) {
        Teste teste = testeService.aprovar(id);
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<TesteDTO> reprovar(@PathVariable Long id) {
        Teste teste = testeService.reprovar(id);
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @PatchMapping("/{id}/alterar-status")
    public ResponseEntity<TesteDTO> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlterarStatusTesteRequest request) {
        Teste teste = testeService.alterarStatus(id, request.getStatus());
        return ResponseEntity.ok(TesteDTO.fromEntity(teste));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        testeService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}