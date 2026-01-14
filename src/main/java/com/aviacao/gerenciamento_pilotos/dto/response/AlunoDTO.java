package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {
    private Long id;
    private String nome;
    private Integer passaporte;
    private String telefone;
    private List<TesteDTO> testes;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public static AlunoDTO fromEntity(Aluno aluno) {
        if (aluno == null) {
            return null;
        }

        List<TesteDTO> testesDTO = null;
        if (aluno.getTestes() != null) {
            testesDTO = aluno.getTestes().stream()
                    .filter(t -> t.getAtivo())
                    .map(t -> TesteDTO.fromEntity(t, false))
                    .collect(Collectors.toList());
        }

        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .testes(testesDTO)
                .dataCriacao(aluno.getDataCriacao())
                .dataUltimaAtualizacao(aluno.getDataUltimaAtualizacao())
                .build();
    }
}