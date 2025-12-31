package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TesteResumoDTO {
    private Long id;
    private StatusTeste status;
    private Long avaliadorId;
    private String avaliadorNome;

    public static TesteResumoDTO fromEntity(Teste teste) {
        if (teste == null) {
            return null;
        }

        return TesteResumoDTO.builder()
                .id(teste.getId())
                .status(teste.getStatus())
                .avaliadorId(teste.getAvaliador() != null ? teste.getAvaliador().getId() : null)
                .avaliadorNome(teste.getAvaliador() != null ? teste.getAvaliador().getNome() : null)
                .build();
    }
}