package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizarLocalPousoRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarLocalPousoRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.LocalPousoDTO;
import com.aviacao.gerenciamento_pilotos.service.LocalPousoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/locais-pouso")
@RequiredArgsConstructor
public class LocalPousoController {

    private final LocalPousoService localPousoService;

    @GetMapping
    public ResponseEntity<List<LocalPousoDTO>> listarTodos(
            @RequestParam(defaultValue = "false") boolean incluirImagem) {
        List<LocalPouso> locais = localPousoService.listarTodos();
        List<LocalPousoDTO> response = locais.stream()
                .map(l -> LocalPousoDTO.fromEntity(l, incluirImagem))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalPousoDTO> buscarPorId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "true") boolean incluirImagem) {
        LocalPouso localPouso = localPousoService.buscarPorId(id);
        return ResponseEntity.ok(LocalPousoDTO.fromEntity(localPouso, incluirImagem));
    }

    @PostMapping
    public ResponseEntity<LocalPousoDTO> cadastrar(@Valid @RequestBody CadastrarLocalPousoRequest request) {
        LocalPouso localPouso = new LocalPouso();
        localPouso.setNome(request.getNome());
        localPouso.setImagem(request.getImagem());

        LocalPouso localCriado = localPousoService.cadastrar(localPouso);
        return ResponseEntity.status(HttpStatus.CREATED).body(LocalPousoDTO.fromEntity(localCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocalPousoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarLocalPousoRequest request) {

        LocalPouso localPouso = new LocalPouso();
        localPouso.setNome(request.getNome());
        localPouso.setImagem(request.getImagem());

        LocalPouso localAtualizado = localPousoService.atualizar(id, localPouso);
        return ResponseEntity.ok(LocalPousoDTO.fromEntity(localAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        localPousoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<LocalPousoDTO> ativar(@PathVariable Long id) {
        LocalPouso localPouso = localPousoService.ativar(id);
        return ResponseEntity.ok(LocalPousoDTO.fromEntity(localPouso));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<LocalPousoDTO> desativar(@PathVariable Long id) {
        LocalPouso localPouso = localPousoService.desativar(id);
        return ResponseEntity.ok(LocalPousoDTO.fromEntity(localPouso));
    }
}