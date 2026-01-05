package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoComAeronavesDTO {
    private Long id;
    private String nome;
    private Integer passaporte;
    private String telefone;
    private List<AlunoAeronaveDTO> aeronaves;

    public static AlunoComAeronavesDTO fromEntity(Aluno aluno, List<AlunoAeronaveDTO> aeronaves) {
        return AlunoComAeronavesDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .aeronaves(aeronaves)
                .build();
    }
}