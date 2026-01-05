package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AeronaveDTO;
import com.aviacao.gerenciamento_pilotos.service.AeronaveService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/aeronaves")
@RequiredArgsConstructor
public class AeronaveController {

    private final AeronaveService aeronaveService;
    private final ObjectMapper objectMapper;

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
    public ResponseEntity<List<AeronaveDTO>> salvar(@RequestBody JsonNode body) {
        List<CadastrarAeronaveRequest> requests = new ArrayList<>();

        if (body.isArray()) {
            for (JsonNode node : body) {
                CadastrarAeronaveRequest request = objectMapper.convertValue(node, CadastrarAeronaveRequest.class);
                requests.add(request);
            }
        } else {
            CadastrarAeronaveRequest request = objectMapper.convertValue(body, CadastrarAeronaveRequest.class);
            requests.add(request);
        }

        List<Aeronave> aeronaves = aeronaveService.salvarEmLote(requests);
        List<AeronaveDTO> response = aeronaves.stream()
                .map(AeronaveDTO::fromEntity)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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