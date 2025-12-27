package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AeronaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AeronaveService {

    private final AeronaveRepository aeronaveRepository;

    @Transactional(readOnly = true)
    public Page<Aeronave> listarTodas(Pageable pageable) {
        return aeronaveRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Aeronave> listarAtivas() {
        return aeronaveRepository.findByAtivaTrue();
    }

    @Transactional(readOnly = true)
    public Aeronave buscarPorId(Long id) {
        return aeronaveRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aeronave não encontrada com ID: " + id));
    }

    @Transactional
    public Aeronave cadastrar(Aeronave aeronave) {
        validarNomeUnico(aeronave.getNome());
        aeronave.setAtiva(true);
        return aeronaveRepository.save(aeronave);
    }

    @Transactional
    public Aeronave atualizar(Long id, Aeronave aeronaveAtualizada) {
        Aeronave aeronave = buscarPorId(id);

        if (aeronaveAtualizada.getNome() != null && !aeronaveAtualizada.getNome().equals(aeronave.getNome())) {
            validarNomeUnico(aeronaveAtualizada.getNome());
            aeronave.setNome(aeronaveAtualizada.getNome());
        }

        if (aeronaveAtualizada.getCategoria() != null) {
            aeronave.setCategoria(aeronaveAtualizada.getCategoria());
        }

        return aeronaveRepository.save(aeronave);
    }

    @Transactional
    public void inativar(Long id) {
        Aeronave aeronave = buscarPorId(id);
        aeronave.setAtiva(false);
        aeronaveRepository.save(aeronave);
    }

    @Transactional
    public void deletar(Long id) {
        Aeronave aeronave = buscarPorId(id);
        aeronaveRepository.delete(aeronave);
    }

    private void validarNomeUnico(String nome) {
        if (aeronaveRepository.existsByNome(nome)) {
            throw new BusinessException("Nome de aeronave já cadastrado");
        }
    }
}