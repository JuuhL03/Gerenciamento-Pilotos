package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
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
    private TesteDTO testeAtual;  // ← Apenas o teste mais recente
    private LocalDateTime dataCriacao;

    public static AlunoResumoDTO fromEntity(Aluno aluno) {
        TesteDTO testeAtual = null;

        if (aluno.getTestes() != null && !aluno.getTestes().isEmpty()) {
            // Pegar o teste mais recente (maior ID)
            testeAtual = TesteDTO.fromEntity(
                    aluno.getTestes().stream()
                            .max((t1, t2) -> t1.getId().compareTo(t2.getId()))
                            .orElse(null)
            );
        }

        return AlunoResumoDTO.builder()
                .id(aluno.getId())
                .nome(aluno.getNome())
                .passaporte(aluno.getPassaporte())
                .telefone(aluno.getTelefone())
                .autorizado(aluno.getAutorizado())
                .testeAtual(testeAtual)
                .dataCriacao(aluno.getDataCriacao())
                .build();
    }
}