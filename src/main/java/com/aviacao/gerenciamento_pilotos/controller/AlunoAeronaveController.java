package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import com.aviacao.gerenciamento_pilotos.dto.request.VincularAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoAeronaveDTO;
import com.aviacao.gerenciamento_pilotos.service.AlunoAeronaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/alunos/{alunoId}/aeronaves")
@RequiredArgsConstructor
public class AlunoAeronaveController {

    private final AlunoAeronaveService alunoAeronaveService;

    @GetMapping
    public ResponseEntity<List<AlunoAeronaveDTO>> listar(@PathVariable Long alunoId) {
        List<AlunoAeronave> vinculos = alunoAeronaveService.listarPorAluno(alunoId);
        List<AlunoAeronaveDTO> response = vinculos.stream()
                .map(AlunoAeronaveDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AlunoAeronaveDTO> vincular(
            @PathVariable Long alunoId,
            @Valid @RequestBody VincularAeronaveRequest request) {

        AlunoAeronave vinculo = alunoAeronaveService.vincularAeronave(alunoId, request.getAeronaveId());
        return ResponseEntity.status(HttpStatus.CREATED).body(AlunoAeronaveDTO.fromEntity(vinculo));
    }

    @DeleteMapping("/{aeronaveId}")
    public ResponseEntity<Void> desvincular(
            @PathVariable Long alunoId,
            @PathVariable Long aeronaveId) {

        alunoAeronaveService.desvincularAeronave(alunoId, aeronaveId);
        return ResponseEntity.noContent().build();
    }
}