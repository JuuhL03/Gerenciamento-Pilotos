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

        try {
            // ✅ USA O MÉTODO SEM FILTRO!
            Usuario adminExistente = usuarioRepository.findByLoginIgnorandoAtivo(loginAdmin).orElse(null);

            if (adminExistente != null) {
                // Admin existe
                if (adminExistente.getAtivo()) {
                    System.out.println("✅ Usuário ADMIN já existe e está ativo.");
                }
            } else {
                // Admin não existe - cria novo
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
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao criar/verificar usuário admin: " + e.getMessage());
        }
    }
}