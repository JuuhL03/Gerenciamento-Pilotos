package com.aviacao.gerenciamento_pilotos.config;

import com.aviacao.gerenciamento_pilotos.domain.entity.Usuario;
import com.aviacao.gerenciamento_pilotos.domain.enums.Cargo;
import com.aviacao.gerenciamento_pilotos.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        criarUsuarioAdminPadrao();
    }

    private void criarUsuarioAdminPadrao() {
        String loginAdmin = "admin";

        if (!usuarioRepository.existsByLogin(loginAdmin)) {
            Usuario admin = new Usuario();
            admin.setPassaporte("001");
            admin.setNome("Admin");
            admin.setTelefone("000-000");
            admin.setLogin(loginAdmin);
            admin.setSenhaHash(passwordEncoder.encode("Gam@CPX"));
            admin.setCargo(Cargo.ADMIN);
            admin.setAtivo(true);

            usuarioRepository.save(admin);

            System.out.println("✅ Usuário ADMIN criado com sucesso!");
            System.out.println("   Passaporte: 001");
            System.out.println("   Nome: Admin");
            System.out.println("   Login: admin");
            System.out.println("   Senha: Gam@CPX");
        } else {
            System.out.println("ℹ️ Usuário ADMIN já existe no banco de dados.");
        }
    }
}