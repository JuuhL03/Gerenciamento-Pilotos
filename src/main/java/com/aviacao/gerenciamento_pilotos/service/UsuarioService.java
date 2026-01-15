package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<Usuario> listarTodos(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Usuario> listarPorCargo(Cargo cargo, Pageable pageable) {
        return usuarioRepository.findByCargo(cargo, pageable);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com ID: " + id));
    }

    @Transactional
    public Usuario cadastrar(Usuario usuario, Long criadoPorId) {
        validarUsuarioUnico(usuario);

        usuario.setSenhaHash(passwordEncoder.encode(usuario.getSenhaHash()));
        usuario.setAtivo(true);

        if (criadoPorId != null) {
            Usuario criador = buscarPorId(criadoPorId);
            usuario.setCriadoPor(criador);
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuario = buscarPorId(id);

        if (usuarioAtualizado.getNome() != null) {
            usuario.setNome(usuarioAtualizado.getNome());
        }

        if (usuarioAtualizado.getPassaporte() != null && !usuarioAtualizado.getPassaporte().equals(usuario.getPassaporte())) {
            if (usuarioRepository.existsByPassaporte(usuarioAtualizado.getPassaporte())) {
                throw new BusinessException("Passaporte já cadastrado");
            }
            usuario.setPassaporte(usuarioAtualizado.getPassaporte());
        }

        if (usuarioAtualizado.getTelefone() != null) {
            usuario.setTelefone(usuarioAtualizado.getTelefone());
        }

        if (usuarioAtualizado.getCargo() != null) {
            usuario.setCargo(usuarioAtualizado.getCargo());
        }

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario alterarSenha(Long id, String novaSenha) {
        Usuario usuario = buscarPorId(id);
        usuario.setSenhaHash(passwordEncoder.encode(novaSenha));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void inativar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuario.setAtivo(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario reativar(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com ID: " + id));
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        Usuario usuario = buscarPorId(id);
        usuarioRepository.delete(usuario);
    }

    private void validarUsuarioUnico(Usuario usuario) {
        if (usuarioRepository.existsByLogin(usuario.getLogin())) {
            throw new BusinessException("Login já cadastrado");
        }

        if (usuarioRepository.existsByPassaporte(usuario.getPassaporte())) {
            throw new BusinessException("Passaporte já cadastrado");
        }
    }

    /**
     * Lista somente usuários ATIVOS ordenados por nome
     * Para uso em dropdowns/selects
     */
    @Transactional(readOnly = true)
    public List<Usuario> listarAtivosResumo() {
        return usuarioRepository.findByAtivoTrueOrderByNomeAsc();
    }
}