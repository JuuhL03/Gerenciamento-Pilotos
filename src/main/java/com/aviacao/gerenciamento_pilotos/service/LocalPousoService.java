package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.LocalPousoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalPousoService {

    private final LocalPousoRepository localPousoRepository;

    @Transactional(readOnly = true)
    public List<LocalPouso> listarTodos() {
        return localPousoRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public LocalPouso buscarPorId(Long id) {
        return localPousoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local de pouso não encontrado com ID: " + id));
    }

    @Transactional
    public LocalPouso cadastrar(LocalPouso localPouso) {
        if (localPousoRepository.existsByNome(localPouso.getNome())) {
            throw new BusinessException("Já existe um local de pouso com o nome: " + localPouso.getNome());
        }

        localPouso.setAtivo(true);
        return localPousoRepository.save(localPouso);
    }

    @Transactional
    public LocalPouso atualizar(Long id, LocalPouso localPousoAtualizado) {
        LocalPouso localPouso = buscarPorId(id);

        if (localPousoAtualizado.getNome() != null && !localPousoAtualizado.getNome().equals(localPouso.getNome())) {
            if (localPousoRepository.existsByNomeAndIdNot(localPousoAtualizado.getNome(), id)) {
                throw new BusinessException("Já existe um local de pouso com o nome: " + localPousoAtualizado.getNome());
            }
            localPouso.setNome(localPousoAtualizado.getNome());
        }

        if (localPousoAtualizado.getImagem() != null) {
            localPouso.setImagem(localPousoAtualizado.getImagem());
        }

        return localPousoRepository.save(localPouso);
    }

    @Transactional
    public void deletar(Long id) {
        LocalPouso localPouso = buscarPorId(id);
        localPouso.setAtivo(false);
        localPousoRepository.save(localPouso);
    }

    @Transactional
    public LocalPouso ativar(Long id) {
        LocalPouso localPouso = localPousoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local de pouso não encontrado com ID: " + id));
        localPouso.setAtivo(true);
        return localPousoRepository.save(localPouso);
    }

    @Transactional
    public LocalPouso desativar(Long id) {
        LocalPouso localPouso = buscarPorId(id);
        localPouso.setAtivo(false);
        return localPousoRepository.save(localPouso);
    }
}