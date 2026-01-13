package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalPousoSimplesDTO {

    private Long id;
    private String nome;
    private String imagemUrl;
    private Boolean ativo;
    private LocalDateTime dataCriacao;

    public static LocalPousoSimplesDTO fromEntity(LocalPouso localPouso) {
        return LocalPousoSimplesDTO.builder()
                .id(localPouso.getId())
                .nome(localPouso.getNome())
                .imagemUrl(localPouso.getImagemUrl())
                .ativo(localPouso.getAtivo())
                .dataCriacao(localPouso.getDataCriacao())
                .build();
    }
}