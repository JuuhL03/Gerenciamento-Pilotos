package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.AlunoAeronave;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoAeronaveDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Long aeronaveId;
    private String aeronaveNome;
    private LocalDateTime dataAutorizacao;

    public static AlunoAeronaveDTO fromEntity(AlunoAeronave alunoAeronave) {
        return AlunoAeronaveDTO.builder()
                .id(alunoAeronave.getId())
                .alunoId(alunoAeronave.getAluno().getId())
                .alunoNome(alunoAeronave.getAluno().getNome())
                .aeronaveId(alunoAeronave.getAeronave().getId())
                .aeronaveNome(alunoAeronave.getAeronave().getNome())
                .dataAutorizacao(alunoAeronave.getDataAutorizacao())
                .build();
    }
}