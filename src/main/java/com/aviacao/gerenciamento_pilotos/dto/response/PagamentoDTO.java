package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoDTO {

    private Long id;
    private Boolean pago;
    private String comprovanteNome;
    private String comprovanteTipo;
    private Long comprovanteTamanho;
    private String comprovanteBase64;  // Imagem em base64 (opcional - só quando solicitado)
    private LocalDateTime dataPagamento;

    public static PagamentoDTO fromEntity(Pagamento pagamento, boolean incluirComprovante) {
        if (pagamento == null) {
            return null;
        }

        PagamentoDTOBuilder builder = PagamentoDTO.builder()
                .id(pagamento.getId())
                .pago(pagamento.getPago())
                .comprovanteNome(pagamento.getComprovanteNome())
                .comprovanteTipo(pagamento.getComprovanteTipo())
                .comprovanteTamanho(pagamento.getComprovanteTamanho())
                .dataPagamento(pagamento.getDataPagamento());

        // Incluir imagem em base64 apenas quando solicitado
        if (incluirComprovante && pagamento.getComprovanteDados() != null) {
            builder.comprovanteBase64(java.util.Base64.getEncoder().encodeToString(pagamento.getComprovanteDados()));
        }

        return builder.build();
    }

    public static PagamentoDTO fromEntity(Pagamento pagamento) {
        return fromEntity(pagamento, false);  // Por padrão, NÃO inclui a imagem
    }
}