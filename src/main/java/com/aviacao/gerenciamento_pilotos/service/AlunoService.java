package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.specification.AlunoSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;

    @Transactional(readOnly = true)
    public Page<Aluno> listarTodos(Pageable pageable) {
        return alunoRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Page<Aluno> listarComFiltros(String busca, StatusTeste status, Pageable pageable) {
        return alunoRepository.findAll(AlunoSpecification.comFiltros(busca, status), pageable);
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorId(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public Aluno buscarPorPassaporte(String passaporte) {
        return alunoRepository.findByPassaporte(passaporte)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com passaporte: " + passaporte));
    }

    @Transactional
    public Aluno cadastrar(Aluno aluno) {
        validarPassaporteUnico(aluno.getPassaporte());

        // Criar teste automaticamente com status EM_ANDAMENTO
        Teste teste = new Teste();
        teste.setAluno(aluno);
        teste.setStatus(StatusTeste.EM_ANDAMENTO);

        aluno.getTestes().add(teste);

        return alunoRepository.save(aluno);
    }

    @Transactional
    public Aluno atualizar(Long id, Aluno alunoAtualizado) {
        Aluno aluno = buscarPorId(id);

        if (alunoAtualizado.getNome() != null) {
            aluno.setNome(alunoAtualizado.getNome());
        }

        if (alunoAtualizado.getPassaporte() != null && !alunoAtualizado.getPassaporte().equals(aluno.getPassaporte())) {
            validarPassaporteUnico(alunoAtualizado.getPassaporte());
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

    @Transactional
    public void deletar(Long id) {
        Aluno aluno = buscarPorId(id);
        alunoRepository.delete(aluno);
    }

    private void validarPassaporteUnico(String passaporte) {
        if (alunoRepository.existsByPassaporte(passaporte)) {
            throw new BusinessException("Passaporte já cadastrado");
        }
    }
}