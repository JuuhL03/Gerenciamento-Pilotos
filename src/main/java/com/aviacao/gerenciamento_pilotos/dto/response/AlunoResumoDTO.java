package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResumoDTO {
    private Long id;
    private String nome;
    private Integer passaporte;
    private String telefone;
    private Boolean autorizado;
    private TesteResumoDTO teste;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public static AlunoResumoDTO fromEntity(Aluno aluno) {
        if (aluno == null) {
            return null;
        }

        Teste testeAtual = aluno.getTesteAtual();

        return AlunoResumoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .autorizado(aluno.getAutorizado())
                .teste(testeAtual != null ? TesteResumoDTO.fromEntity(testeAtual) : null)
                .dataCriacao(aluno.getDataCriacao())
                .dataUltimaAtualizacao(aluno.getDataUltimaAtualizacao())
                .build();
    }
}