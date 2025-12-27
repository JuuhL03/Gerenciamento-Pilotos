package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizacaoUsuarioRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastroUsuarioRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.UsuarioDTO;
import com.aviacao.gerenciamento_pilotos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioDTO>> listar(
            @RequestParam(required = false) Cargo cargo,
            @RequestParam(required = false) Boolean apenasAtivos,
            Pageable pageable) {

        Page<Usuario> usuarios;

        if (cargo != null) {
            usuarios = usuarioService.listarPorCargo(cargo, pageable);
        } else if (Boolean.TRUE.equals(apenasAtivos)) {
            usuarios = usuarioService.listarAtivos(pageable);
        } else {
            usuarios = usuarioService.listarTodos(pageable);
        }

        Page<UsuarioDTO> response = usuarios.map(UsuarioDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> cadastrar(
            @Valid @RequestBody CadastroUsuarioRequest request) {

        Usuario usuario = new Usuario();
        usuario.setPassaporte(request.getPassaporte());
        usuario.setNome(request.getNome());
        usuario.setTelefone(request.getTelefone());
        usuario.setLogin(request.getLogin());
        usuario.setSenhaHash(request.getSenha());
        usuario.setCargo(request.getCargo());

        Usuario usuarioCriado = usuarioService.cadastrar(usuario, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioDTO.fromEntity(usuarioCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizacaoUsuarioRequest request) {

        Usuario usuario = new Usuario();
        usuario.setPassaporte(request.getPassaporte());
        usuario.setNome(request.getNome());
        usuario.setTelefone(request.getTelefone());
        usuario.setCargo(request.getCargo());

        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);

        if (request.getNovaSenha() != null && !request.getNovaSenha().isBlank()) {
            usuarioAtualizado = usuarioService.alterarSenha(id, request.getNovaSenha());
        }

        if (request.getAtivo() != null) {
            if (request.getAtivo()) {
                usuarioAtualizado = usuarioService.reativar(id);
            } else {
                usuarioService.inativar(id);
                usuarioAtualizado = usuarioService.buscarPorId(id);
            }
        }

        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuarioAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<UsuarioDTO> reativar(@PathVariable Long id) {
        Usuario usuario = usuarioService.reativar(id);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }
}