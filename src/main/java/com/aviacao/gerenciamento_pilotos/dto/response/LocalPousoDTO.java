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
public class LocalPousoDTO {

    private Long id;
    private String nome;
    private String imagem; // Base64
    private Boolean ativo;
    private LocalDateTime dataCriacao;

    public static LocalPousoDTO fromEntity(LocalPouso localPouso) {
        return fromEntity(localPouso, true);
    }

    public static LocalPousoDTO fromEntity(LocalPouso localPouso, boolean incluirImagem) {
        if (localPouso == null) {
            return null;
        }

        return LocalPousoDTO.builder()
                .id(localPouso.getId())
                .nome(localPouso.getNome())
                .imagem(incluirImagem ? localPouso.getImagem() : null)
                .ativo(localPouso.getAtivo())
                .dataCriacao(localPouso.getDataCriacao())
                .build();
    }
}