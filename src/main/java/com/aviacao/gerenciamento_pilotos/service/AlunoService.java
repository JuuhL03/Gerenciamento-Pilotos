package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.TesteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final TesteRepository testeRepository;

    @Transactional(readOnly = true)
    public Page<Aluno> listarTodos(Pageable pageable) {
        return alunoRepository.findAllWithTestes(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Aluno> listarComFiltros(String busca, StatusTeste status, Pageable pageable) {
        if (busca != null && status != null) {
            return alunoRepository.findByBuscaAndStatusWithTestes(busca, status, pageable);
        } else if (busca != null) {
            return alunoRepository.findByBuscaWithTestes(busca, pageable);
        } else if (status != null) {
            return alunoRepository.findByStatusWithTestes(status, pageable);
        }
        return alunoRepository.findAllWithTestes(pageable);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorPassaporte(Integer passaporte) {
        return alunoRepository.findByPassaporte(passaporte)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com passaporte: " + passaporte));
    }

    @Transactional
    public Aluno cadastrar(Aluno aluno) {
        if (alunoRepository.existsByPassaporte(aluno.getPassaporte())) {
            throw new BusinessException("Já existe um aluno com o passaporte: " + aluno.getPassaporte());
        }

        aluno.setAutorizado(false);
        aluno.setAtivo(true);

        Aluno alunoSalvo = alunoRepository.save(aluno);

        Teste primeiroTeste = new Teste();
        primeiroTeste.setAluno(alunoSalvo);
        primeiroTeste.setStatus(StatusTeste.EM_ANDAMENTO);
        primeiroTeste.setAtivo(true);
        testeRepository.save(primeiroTeste);

        return alunoSalvo;
    }

    @Transactional
    public Aluno atualizar(Long id, Aluno alunoAtualizado) {
        Aluno aluno = buscarPorId(id);

        if (alunoAtualizado.getNome() != null) {
            aluno.setNome(alunoAtualizado.getNome());
        }

        if (alunoAtualizado.getPassaporte() != null) {
            if (alunoRepository.existsByPassaporteAndIdNot(alunoAtualizado.getPassaporte(), id)) {
                throw new BusinessException("Já existe um aluno com o passaporte: " + alunoAtualizado.getPassaporte());
            }
            aluno.setPassaporte(alunoAtualizado.getPassaporte());
        }

        if (alunoAtualizado.getTelefone() != null) {
            aluno.setTelefone(alunoAtualizado.getTelefone());
        }

        if (alunoAtualizado.getAutorizado() != null) {
            aluno.setAutorizado(alunoAtualizado.getAutorizado());
        }

        return alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorIdComTestes(Long id) {
        return alunoRepository.findByIdWithTestes(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        Aluno aluno = buscarPorId(id);

        if (aluno.getTestes() != null && !aluno.getTestes().isEmpty()) {
            aluno.getTestes().forEach(teste -> teste.setAtivo(false));
        }

        aluno.setAtivo(false);
        alunoRepository.save(aluno);
    }

    @Transactional(readOnly = true)
    public List<Aluno> listarTodos() {
        return alunoRepository.findAll();
    }
}