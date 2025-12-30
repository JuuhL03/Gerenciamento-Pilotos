package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizarAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AeronaveDTO;
import com.aviacao.gerenciamento_pilotos.service.AeronaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aeronaves")
@RequiredArgsConstructor
public class AeronaveController {

    private final AeronaveService aeronaveService;

    @GetMapping
    public ResponseEntity<List<AeronaveDTO>> listarTodas() {
        List<Aeronave> aeronaves = aeronaveService.listarTodos();
        List<AeronaveDTO> response = aeronaves.stream()
                .map(AeronaveDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<AeronaveDTO>> listarAtivas() {
        List<Aeronave> aeronaves = aeronaveService.listarAtivas();
        List<AeronaveDTO> response = aeronaves.stream()
                .map(AeronaveDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AeronaveDTO> buscarPorId(@PathVariable Long id) {
        Aeronave aeronave = aeronaveService.buscarPorId(id);
        return ResponseEntity.ok(AeronaveDTO.fromEntity(aeronave));
    }

    @PostMapping
    public ResponseEntity<AeronaveDTO> cadastrar(@Valid @RequestBody CadastrarAeronaveRequest request) {
        Aeronave aeronave = aeronaveService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(AeronaveDTO.fromEntity(aeronave));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AeronaveDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarAeronaveRequest request) {

        Aeronave aeronave = aeronaveService.atualizar(id, request);
        return ResponseEntity.ok(AeronaveDTO.fromEntity(aeronave));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        aeronaveService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<AeronaveDTO> ativar(@PathVariable Long id) {
        Aeronave aeronave = aeronaveService.ativar(id);
        return ResponseEntity.ok(AeronaveDTO.fromEntity(aeronave));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<AeronaveDTO> desativar(@PathVariable Long id) {
        Aeronave aeronave = aeronaveService.desativar(id);
        return ResponseEntity.ok(AeronaveDTO.fromEntity(aeronave));
    }
}