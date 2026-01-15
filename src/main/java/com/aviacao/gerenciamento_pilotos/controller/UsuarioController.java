package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizacaoUsuarioRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastroUsuarioRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.UsuarioDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.UsuarioResumoDTO;
import com.aviacao.gerenciamento_pilotos.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Lista TODOS os usuários (ativos e inativos) - SÓ ADMIN
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioDTO>> listar(
            @RequestParam(required = false) Cargo cargo,
            Pageable pageable) {

        Page<Usuario> usuarios;

        if (cargo != null) {
            usuarios = usuarioService.listarPorCargo(cargo, pageable);
        } else {
            usuarios = usuarioService.listarTodos(pageable);
        }

        Page<UsuarioDTO> response = usuarios.map(UsuarioDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    /**
     * Busca usuário por ID - SÓ ADMIN
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    /**
     * Cadastra novo usuário - SÓ ADMIN
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Atualiza usuário - SÓ ADMIN
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
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

    /**
     * Deleta (inativa) usuário - SÓ ADMIN
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.inativar(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Reativa usuário - SÓ ADMIN
     */
    @PatchMapping("/{id}/reativar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioDTO> reativar(@PathVariable Long id) {
        Usuario usuario = usuarioService.reativar(id);
        return ResponseEntity.ok(UsuarioDTO.fromEntity(usuario));
    }

    /**
     * Lista usuários ativos com apenas ID e NOME
     * Útil para dropdowns e selects
     * Qualquer usuário autenticado pode acessar
     */
    @GetMapping("/resumo")
    public ResponseEntity<List<UsuarioResumoDTO>> listarResumo() {
        List<Usuario> usuarios = usuarioService.listarAtivosResumo();
        List<UsuarioResumoDTO> response = usuarios.stream()
                .map(UsuarioResumoDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(response);
    }
}