package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastroAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AeronaveDTO;
import com.aviacao.gerenciamento_pilotos.service.AeronaveService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<Page<AeronaveDTO>> listar(Pageable pageable) {
        Page<Aeronave> aeronaves = aeronaveService.listarTodas(pageable);
        Page<AeronaveDTO> response = aeronaves.map(AeronaveDTO::fromEntity);
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
    public ResponseEntity<AeronaveDTO> cadastrar(@Valid @RequestBody CadastroAeronaveRequest request) {
        Aeronave aeronave = new Aeronave();
        aeronave.setNome(request.getNome());
        aeronave.setCategoria(request.getCategoria());

        Aeronave aeronaveCriada = aeronaveService.cadastrar(aeronave);
        return ResponseEntity.status(HttpStatus.CREATED).body(AeronaveDTO.fromEntity(aeronaveCriada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AeronaveDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody CadastroAeronaveRequest request) {

        Aeronave aeronave = new Aeronave();
        aeronave.setNome(request.getNome());
        aeronave.setCategoria(request.getCategoria());

        Aeronave aeronaveAtualizada = aeronaveService.atualizar(id, aeronave);
        return ResponseEntity.ok(AeronaveDTO.fromEntity(aeronaveAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        aeronaveService.inativar(id);
        return ResponseEntity.noContent().build();
    }
}