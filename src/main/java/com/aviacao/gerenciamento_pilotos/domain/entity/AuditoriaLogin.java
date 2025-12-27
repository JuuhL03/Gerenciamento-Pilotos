package com.aviacao.gerenciamento_pilotos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria_login")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "login_tentado", nullable = false, length = 50)
    private String loginTentado;

    @Column(nullable = false)
    private Boolean sucesso;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "data_tentativa", updatable = false)
    private LocalDateTime dataTentativa;

    @PrePersist
    protected void onCreate() {
        dataTentativa = LocalDateTime.now();
    }
}