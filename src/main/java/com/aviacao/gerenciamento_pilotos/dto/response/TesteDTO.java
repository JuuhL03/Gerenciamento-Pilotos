package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TesteDTO {

    private Long id;
    private Long alunoId;
    private String alunoNome;
    private Integer alunoPassaporte;
    private String alunoTelefone;
    private StatusTeste status;
    private Long avaliadorId;
    private String avaliadorNome;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataFinalizacao;
    private PagamentoDTO pagamento;

    public static TesteDTO fromEntity(Teste teste) {
        return fromEntity(teste, false);
    }

    public static TesteDTO fromEntity(Teste teste, boolean incluirComprovante) {
        if (teste == null) {
            return null;
        }

        return TesteDTO.builder()
                .id(teste.getId())
                .alunoId(teste.getAluno() != null ? teste.getAluno().getId() : null)
                .alunoNome(teste.getAluno() != null ? teste.getAluno().getNome() : null)
                .alunoPassaporte(teste.getAluno() != null ? teste.getAluno().getPassaporte() : null)
                .alunoTelefone(teste.getAluno() != null ? teste.getAluno().getTelefone() : null)
                .status(teste.getStatus())
                .avaliadorId(teste.getAvaliador() != null ? teste.getAvaliador().getId() : null)
                .avaliadorNome(teste.getAvaliador() != null ? teste.getAvaliador().getNome() : null)
                .dataCriacao(teste.getDataCriacao())
                .dataFinalizacao(teste.getDataFinalizacao())
                .pagamento(teste.getPagamento() != null
                        ? PagamentoDTO.fromEntity(teste.getPagamento(), incluirComprovante)
                        : null)
                .build();
    }
}