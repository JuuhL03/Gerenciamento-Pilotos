package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aeronave;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AeronaveDTO {

    private Long id;
    private String nome;
    private String categoria;
    private Boolean ativa;

    public static AeronaveDTO fromEntity(Aeronave aeronave) {
        return AeronaveDTO.builder()
                .id(aeronave.getId())
                .nome(aeronave.getNome())
                .categoria(aeronave.getCategoria())
                .ativa(aeronave.getAtiva())
                .build();
    }
}