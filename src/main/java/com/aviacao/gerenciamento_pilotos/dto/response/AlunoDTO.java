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
    private Boolean autorizado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;
    private List<TesteDTO> testes;
    private List<AlunoAeronaveDTO> aeronaves;

    public static AlunoDTO fromEntity(Aluno aluno) {
        if (aluno == null) {
            return null;
        }

        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .autorizado(aluno.getAutorizado())
                .dataCriacao(aluno.getDataCriacao())
                .dataUltimaAtualizacao(aluno.getDataUltimaAtualizacao())
                .build();
    }

    public static AlunoDTO fromEntity(Aluno aluno, boolean incluirTestes, boolean incluirComprovante, boolean incluirAeronaves) {
        if (aluno == null) {
            return null;
        }

        AlunoDTOBuilder builder = AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .autorizado(aluno.getAutorizado())
                .dataCriacao(aluno.getDataCriacao())
                .dataUltimaAtualizacao(aluno.getDataUltimaAtualizacao());

        if (incluirTestes && aluno.getTestes() != null) {
            List<TesteDTO> testesDTO = aluno.getTestes().stream()
                    .filter(t -> t.getAtivo())
                    .map(t -> TesteDTO.fromEntity(t, incluirComprovante))
                    .collect(Collectors.toList());
            builder.testes(testesDTO);
        }

        return builder.build();
    }
}