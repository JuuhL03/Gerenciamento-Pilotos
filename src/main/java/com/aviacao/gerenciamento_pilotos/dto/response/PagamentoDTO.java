package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Base64;

@Data
public class PagamentoDTO {
    private Long id;
    private Long alunoId; // ← ADICIONE!
    private Long testeId; // ← ADICIONE TAMBÉM!
    private Boolean pago;
    private BigDecimal valor;
    private String comprovanteNome;
    private String comprovanteTipo;
    private Long comprovanteTamanho;
    private String comprovanteBase64;
    private LocalDateTime dataPagamento;

    public static PagamentoDTO fromEntity(Pagamento pagamento, boolean incluirComprovante) {
        if (pagamento == null) {
            return null;
        }

        PagamentoDTO dto = new PagamentoDTO();
        dto.setId(pagamento.getId());
        dto.setAlunoId(pagamento.getAluno() != null ? pagamento.getAluno().getId() : null); // ← ADICIONE!
        dto.setTesteId(pagamento.getTeste() != null ? pagamento.getTeste().getId() : null); // ← ADICIONE!
        dto.setPago(pagamento.getPago());
        dto.setValor(pagamento.getValor());
        dto.setComprovanteNome(pagamento.getComprovanteNome());
        dto.setComprovanteTipo(pagamento.getComprovanteTipo());
        dto.setComprovanteTamanho(pagamento.getComprovanteTamanho());
        dto.setDataPagamento(pagamento.getDataPagamento());

        if (incluirComprovante && pagamento.getComprovanteDados() != null) {
            String base64 = Base64.getEncoder().encodeToString(pagamento.getComprovanteDados());
            dto.setComprovanteBase64(base64);
        }

        return dto;
    }
}