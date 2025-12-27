package com.aviacao.gerenciamento_pilotos.dto.response;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private Long id;
    private String passaporte;
    private String nome;
    private String telefone;
    private String login;
    private Cargo cargo;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataUltimaAtualizacao;

    public static UsuarioDTO fromEntity(Usuario usuario) {
        return UsuarioDTO.builder()
                .id(usuario.getId())
                .passaporte(usuario.getPassaporte())
                .nome(usuario.getNome())
                .telefone(usuario.getTelefone())
                .login(usuario.getLogin())
                .cargo(usuario.getCargo())
                .ativo(usuario.getAtivo())
                .dataCriacao(usuario.getDataCriacao())
                .dataUltimaAtualizacao(usuario.getDataUltimaAtualizacao())
                .build();
    }
}