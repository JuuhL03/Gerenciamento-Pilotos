package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoAeronaveDTO;
import com.aviacao.gerenciamento_pilotos.repository.AlunoAeronaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoAeronaveService {

    private final AlunoAeronaveRepository alunoAeronaveRepository;
    private final AlunoService alunoService;
    private final AeronaveService aeronaveService;

    @Transactional(readOnly = true)
    public List<AlunoAeronaveDTO> listarAeronavesDoAluno(Long alunoId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);

        List<Aeronave> todasAeronaves = aeronaveService.listarAtivas();

        List<AlunoAeronave> autorizacoes = alunoAeronaveRepository.findByAlunoId(alunoId);
        Map<Long, Boolean> autorizacoesMap = autorizacoes.stream()
                .collect(Collectors.toMap(
                        aa -> aa.getAeronave().getId(),
                        AlunoAeronave::getAutorizado
                ));

        return todasAeronaves.stream()
                .map(aeronave -> AlunoAeronaveDTO.builder()
                        .aeronaveId(aeronave.getId())
                        .aeronaveNome(aeronave.getNome())
                        .autorizado(autorizacoesMap.getOrDefault(aeronave.getId(), false))
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void autorizarAluno(Long alunoId, Long aeronaveId) {
        Aluno aluno = alunoService.buscarPorId(alunoId);
        Aeronave aeronave = aeronaveService.buscarPorId(aeronaveId);

        AlunoAeronave alunoAeronave = alunoAeronaveRepository
                .findByAlunoIdAndAeronaveId(alunoId, aeronaveId)
                .orElse(new AlunoAeronave());

        alunoAeronave.setAluno(aluno);
        alunoAeronave.setAeronave(aeronave);
        alunoAeronave.setAutorizado(true);

        alunoAeronaveRepository.save(alunoAeronave);
    }

    @Transactional
    public void desautorizarAluno(Long alunoId, Long aeronaveId) {
        AlunoAeronave alunoAeronave = alunoAeronaveRepository
                .findByAlunoIdAndAeronaveId(alunoId, aeronaveId)
                .orElse(null);

        if (alunoAeronave != null) {
            alunoAeronave.setAutorizado(false);
            alunoAeronaveRepository.save(alunoAeronave);
        }
    }
}