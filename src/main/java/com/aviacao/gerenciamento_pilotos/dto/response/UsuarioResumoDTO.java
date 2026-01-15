package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioResumoDTO {

    private Long id;
    private String nome;

    public static UsuarioResumoDTO fromEntity(Usuario usuario) {
        return UsuarioResumoDTO.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .build();
    }
}