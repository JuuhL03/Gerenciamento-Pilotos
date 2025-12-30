package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AeronaveDTO {

    private Long id;
    private String nome;
    private Boolean ativa;
    private LocalDateTime dataCriacao;

    public static AeronaveDTO fromEntity(Aeronave aeronave) {
        if (aeronave == null) {
            return null;
        }

        return AeronaveDTO.builder()
                .id(aeronave.getId())
                .nome(aeronave.getNome())
                .ativa(aeronave.getAtiva())
                .dataCriacao(aeronave.getDataCriacao())
                .build();
    }
}