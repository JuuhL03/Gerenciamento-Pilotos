package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoAeronaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlunoAeronaveService {

    private final AlunoAeronaveRepository alunoAeronaveRepository;
    private final AlunoService alunoService;
    private final AeronaveService aeronaveService;

    @Transactional(readOnly = true)
    public List<AlunoAeronave> listarPorAluno(Long alunoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        return alunoAeronaveRepository.findByAluno(aluno);
    }

    @Transactional
    public AlunoAeronave vincularAeronave(Long alunoId, Long aeronaveId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        Aeronave aeronave = aeronaveService.buscarPorId(aeronaveId);

        if (alunoAeronaveRepository.existsByAlunoAndAeronave(aluno, aeronave)) {
            throw new BusinessException("Aluno já está vinculado a esta aeronave");
        }

        AlunoAeronave alunoAeronave = new AlunoAeronave();
        alunoAeronave.setAluno(aluno);
        alunoAeronave.setAeronave(aeronave);

        return alunoAeronaveRepository.save(alunoAeronave);
    }

    @Transactional
    public void desvincularAeronave(Long alunoId, Long aeronaveId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        Aeronave aeronave = aeronaveService.buscarPorId(aeronaveId);

        AlunoAeronave alunoAeronave = alunoAeronaveRepository.findByAlunoAndAeronave(aluno, aeronave)
                .orElseThrow(() -> new NotFoundException("Vínculo não encontrado"));

        alunoAeronaveRepository.delete(alunoAeronave);
    }
}