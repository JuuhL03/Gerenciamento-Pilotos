package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Autorizacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutorizacaoDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private String alunoPassaporte;
    private LocalDateTime dataAutorizacao;
    private LocalDate dataValidade;
    private Boolean ativa;
    private String observacoes;

    public static AutorizacaoDTO fromEntity(Autorizacao autorizacao) {
        return AutorizacaoDTO.builder()
                .id(autorizacao.getId())
                .alunoId(autorizacao.getAluno().getId())
                .alunoNome(autorizacao.getAluno().getNome())
                .alunoPassaporte(autorizacao.getAluno().getPassaporte())
                .dataAutorizacao(autorizacao.getDataAutorizacao())
                .dataValidade(autorizacao.getDataValidade())
                .ativa(autorizacao.getAtiva())
                .observacoes(autorizacao.getObservacoes())
                .build();
    }
}