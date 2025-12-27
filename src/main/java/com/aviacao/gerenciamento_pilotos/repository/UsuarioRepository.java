package com.aviacao.gerenciamento_pilotos.repository;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, JpaSpecificationExecutor<Usuario> {

    Optional<Usuario> findByLogin(String login);

    Optional<Usuario> findByLoginAndAtivoTrue(String login);

    boolean existsByLogin(String login);

    boolean existsByPassaporte(String passaporte);

    Page<Usuario> findByCargo(Cargo cargo, Pageable pageable);

    Page<Usuario> findByAtivoTrue(Pageable pageable);
}