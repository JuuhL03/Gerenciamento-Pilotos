package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {

    private Long id;
    private Long testeId;
    private Long alunoId;
    private String alunoNome;
    private Boolean pago;
    private BigDecimal valor;
    private String comprovanteUrl;
    private LocalDateTime dataCriacao;

    /**
     * Converte entidade para DTO sem incluir comprovante
     */
    public static PagamentoDTO fromEntity(Pagamento pagamento) {
        return fromEntity(pagamento, false);
    }

    /**
     * Converte entidade para DTO com opção de incluir comprovante
     * @param pagamento Entidade Pagamento
     * @param incluirComprovante Se true, inclui a URL do comprovante
     */
    public static PagamentoDTO fromEntity(Pagamento pagamento, boolean incluirComprovante) {
        if (pagamento == null) {
            return null;
        }

        return PagamentoDTO.builder()
                .id(pagamento.getId())
                .testeId(pagamento.getTeste() != null ? pagamento.getTeste().getId() : null)
                .alunoId(pagamento.getAluno() != null ? pagamento.getAluno().getId() : null)
                .alunoNome(pagamento.getAluno() != null ? pagamento.getAluno().getNome() : null)
                .pago(pagamento.getPago())
                .valor(pagamento.getValor())
                .comprovanteUrl(incluirComprovante ? pagamento.getComprovanteUrl() : null)
                .dataCriacao(pagamento.getDataCriacao())
                .build();
    }
}