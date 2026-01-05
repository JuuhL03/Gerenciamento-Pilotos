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
    private Long alunoId;
    private String alunoNome;
    private String nome;
    private String fotoNome;
    private String fotoTipo;
    private Long fotoTamanho;
    private String fotoBase64;
    private LocalDateTime dataCadastro;

    public static LocalPousoDTO fromEntity(LocalPouso localPouso, boolean incluirFoto) {
        LocalPousoDTOBuilder builder = LocalPousoDTO.builder()
                .id(localPouso.getId())
                .alunoId(localPouso.getAluno().getId())
                .alunoNome(localPouso.getAluno().getNome())
                .nome(localPouso.getNome())
                .fotoNome(localPouso.getFotoNome())
                .fotoTipo(localPouso.getFotoTipo())
                .fotoTamanho(localPouso.getFotoTamanho())
                .dataCadastro(localPouso.getDataCadastro());

        if (incluirFoto && localPouso.getFotoDados() != null) {
            builder.fotoBase64(java.util.Base64.getEncoder().encodeToString(localPouso.getFotoDados()));
        }

        return builder.build();
    }

    public static LocalPousoDTO fromEntity(LocalPouso localPouso) {
        return fromEntity(localPouso, false);
    }
}