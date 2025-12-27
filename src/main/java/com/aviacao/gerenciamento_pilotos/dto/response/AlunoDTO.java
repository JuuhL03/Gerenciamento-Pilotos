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
    private String passaporte;
    private String telefone;
    private Boolean autorizado;
    private List<TesteDTO> testes;  // ← TODOS os testes, ordenados
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public static AlunoDTO fromEntity(Aluno aluno) {
        // Ordenar testes do mais recente para o mais antigo
        List<TesteDTO> testesOrdenados = aluno.getTestes().stream()
                .sorted((t1, t2) -> t2.getId().compareTo(t1.getId()))  // DESC
                .map(TesteDTO::fromEntity)
                .collect(Collectors.toList());

        return AlunoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .autorizado(aluno.getAutorizado())
                .testes(testesOrdenados)
                .dataCriacao(aluno.getDataCriacao())
                .dataUltimaAtualizacao(aluno.getDataUltimaAtualizacao())
                .build();
    }
}