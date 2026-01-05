package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizarAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastrarAeronaveRequest;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AeronaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AeronaveService {

    private final AeronaveRepository aeronaveRepository;

    @Transactional(readOnly = true)
    public List<Aeronave> listarTodos() {
        return aeronaveRepository.findAll();
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
    public void deletar(Long id) {
        Aeronave aeronave = buscarPorId(id);
        aeronave.setAtiva(false);
        aeronaveRepository.save(aeronave);
    }

    @Transactional
    public Aeronave ativar(Long id) {
        Aeronave aeronave = buscarPorId(id);
        aeronave.setAtiva(true);
        return aeronaveRepository.save(aeronave);
    }

    @Transactional
    public Aeronave desativar(Long id) {
        Aeronave aeronave = buscarPorId(id);
        aeronave.setAtiva(false);
        return aeronaveRepository.save(aeronave);
    }

    @Transactional
    public List<Aeronave> salvarEmLote(List<CadastrarAeronaveRequest> requests) {
        List<Aeronave> aeronaves = new ArrayList<>();

        for (CadastrarAeronaveRequest request : requests) {
            Aeronave aeronave;

            if (request.getId() != null) {
                Optional<Aeronave> aeronaveExistente = aeronaveRepository.findById(request.getId());

                if (aeronaveExistente.isPresent()) {
                    aeronave = aeronaveExistente.get();

                    if (!aeronave.getNome().equals(request.getNome())) {
                        if (aeronaveRepository.existsByNomeAndIdNot(request.getNome(), request.getId())) {
                            throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
                        }
                        aeronave.setNome(request.getNome());
                    }
                } else {
                    if (aeronaveRepository.existsByNome(request.getNome())) {
                        throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
                    }

                    aeronave = new Aeronave();
                    aeronave.setNome(request.getNome());
                    aeronave.setAtiva(true);
                }
            } else {
                if (aeronaveRepository.existsByNome(request.getNome())) {
                    throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
                }

                aeronave = new Aeronave();
                aeronave.setNome(request.getNome());
                aeronave.setAtiva(true);
            }

            Aeronave aeronaveSalva = aeronaveRepository.save(aeronave);
            aeronaves.add(aeronaveSalva);
        }

        return aeronaves;
    }
}