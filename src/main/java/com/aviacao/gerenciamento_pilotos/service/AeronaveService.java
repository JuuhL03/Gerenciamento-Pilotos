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
    public Aeronave cadastrar(CadastrarAeronaveRequest request) {
        if (aeronaveRepository.existsByNome(request.getNome())) {
            throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
        }

        Aeronave aeronave = new Aeronave();
        aeronave.setNome(request.getNome());
        aeronave.setAtiva(true);

        return aeronaveRepository.save(aeronave);
    }

    @Transactional
    public Aeronave atualizar(Long id, AtualizarAeronaveRequest request) {
        Aeronave aeronave = buscarPorId(id);

        if (request.getNome() != null) {
            if (aeronaveRepository.existsByNomeAndIdNot(request.getNome(), id)) {
                throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
            }
            aeronave.setNome(request.getNome());
        }

        if (request.getAtiva() != null) {
            aeronave.setAtiva(request.getAtiva());
        }

        return aeronaveRepository.save(aeronave);
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
    public List<Aeronave> cadastrarEmLote(List<CadastrarAeronaveRequest> requests) {
        List<Aeronave> aeronaves = new ArrayList<>();

        for (CadastrarAeronaveRequest request : requests) {
            if (aeronaveRepository.existsByNome(request.getNome())) {
                throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
            }

            Aeronave aeronave = new Aeronave();
            aeronave.setNome(request.getNome());
            aeronave.setAtiva(true);

            Aeronave aeronaveSalva = aeronaveRepository.save(aeronave);
            aeronaves.add(aeronaveSalva);
        }

        return aeronaves;
    }

    @Transactional
    public List<Aeronave> atualizarEmLote(List<AtualizarAeronaveRequest> requests) {
        List<Aeronave> aeronaves = new ArrayList<>();

        for (AtualizarAeronaveRequest request : requests) {
            if (request.getId() == null) {
                throw new BusinessException("ID é obrigatório para atualização");
            }

            Aeronave aeronave = buscarPorId(request.getId());

            if (request.getNome() != null) {
                if (aeronaveRepository.existsByNomeAndIdNot(request.getNome(), request.getId())) {
                    throw new BusinessException("Já existe uma aeronave com o nome: " + request.getNome());
                }
                aeronave.setNome(request.getNome());
            }

            if (request.getAtiva() != null) {
                aeronave.setAtiva(request.getAtiva());
            }

            Aeronave aeronaveAtualizada = aeronaveRepository.save(aeronave);
            aeronaves.add(aeronaveAtualizada);
        }

        return aeronaves;
    }
}