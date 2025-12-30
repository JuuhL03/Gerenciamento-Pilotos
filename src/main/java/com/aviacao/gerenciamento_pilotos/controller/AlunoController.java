package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.dto.request.AlterarStatusTesteRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.AtribuirAvaliadorRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.AtualizacaoAlunoRequest;
import com.aviacao.gerenciamento_pilotos.dto.request.CadastroAlunoRequest;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoAeronaveDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoDTO;
import com.aviacao.gerenciamento_pilotos.dto.response.AlunoResumoDTO;
import com.aviacao.gerenciamento_pilotos.service.AlunoAeronaveService;
import com.aviacao.gerenciamento_pilotos.service.AlunoService;
import com.aviacao.gerenciamento_pilotos.service.TesteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final TesteService testeService;
    private final AlunoAeronaveService alunoAeronaveService;

    @GetMapping
    public ResponseEntity<Page<AlunoResumoDTO>> listar(
            @RequestParam(required = false) String busca,
            @RequestParam(required = false) StatusTeste status,
            Pageable pageable) {

        Page<Aluno> alunos;

        if (busca != null || status != null) {
            alunos = alunoService.listarComFiltros(busca, status, pageable);
        } else {
            alunos = alunoService.listarTodos(pageable);
        }

        Page<AlunoResumoDTO> response = alunos.map(AlunoResumoDTO::fromEntity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @GetMapping("/passaporte/{passaporte}")
    public ResponseEntity<AlunoDTO> buscarPorPassaporte(@PathVariable Integer passaporte) {
        Aluno aluno = alunoService.buscarPorPassaporte(passaporte);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @PostMapping
    public ResponseEntity<AlunoDTO> cadastrar(@Valid @RequestBody CadastroAlunoRequest request) {
        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setPassaporte(request.getPassaporte());
        aluno.setTelefone(request.getTelefone());

        Aluno alunoCriado = alunoService.cadastrar(aluno);
        return ResponseEntity.status(HttpStatus.CREATED).body(AlunoDTO.fromEntity(alunoCriado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody AtualizacaoAlunoRequest request) {

        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setPassaporte(request.getPassaporte());
        aluno.setTelefone(request.getTelefone());
        aluno.setAutorizado(request.getAutorizado());

        Aluno alunoAtualizado = alunoService.atualizar(id, aluno);
        return ResponseEntity.ok(AlunoDTO.fromEntity(alunoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        alunoService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/atribuir-avaliador")
    public ResponseEntity<AlunoDTO> atribuirAvaliador(
            @PathVariable Long id,
            @Valid @RequestBody AtribuirAvaliadorRequest request) {

        Teste teste = testeService.atribuirAvaliador(id, request.getAvaliadorId());
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<AlunoDTO> aprovar(@PathVariable Long id) {
        testeService.aprovar(id);
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<AlunoDTO> reprovar(@PathVariable Long id) {
        testeService.reprovar(id);
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @PatchMapping("/{id}/alterar-status")
    public ResponseEntity<AlunoDTO> alterarStatus(
            @PathVariable Long id,
            @Valid @RequestBody AlterarStatusTesteRequest request) {

        testeService.alterarStatus(id, request.getStatus());
        Aluno aluno = alunoService.buscarPorId(id);
        return ResponseEntity.ok(AlunoDTO.fromEntity(aluno));
    }

    @PatchMapping("/{id}/autorizar")
    public ResponseEntity<AlunoDTO> autorizar(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);
        aluno.setAutorizado(true);
        Aluno alunoAtualizado = alunoService.atualizar(id, aluno);
        return ResponseEntity.ok(AlunoDTO.fromEntity(alunoAtualizado));
    }

    @PatchMapping("/{id}/desautorizar")
    public ResponseEntity<AlunoDTO> desautorizar(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorId(id);
        aluno.setAutorizado(false);
        Aluno alunoAtualizado = alunoService.atualizar(id, aluno);
        return ResponseEntity.ok(AlunoDTO.fromEntity(alunoAtualizado));
    }

    @GetMapping("/{alunoId}/aeronaves")
    public ResponseEntity<List<AlunoAeronaveDTO>> listarAeronavesDoAluno(@PathVariable Long alunoId) {
        List<AlunoAeronaveDTO> aeronaves = alunoAeronaveService.listarAeronavesDoAluno(alunoId);
        return ResponseEntity.ok(aeronaves);
    }

    @PostMapping("/{alunoId}/aeronaves/{aeronaveId}/autorizar")
    public ResponseEntity<Void> autorizarAeronave(
            @PathVariable Long alunoId,
            @PathVariable Long aeronaveId) {

        alunoAeronaveService.autorizarAluno(alunoId, aeronaveId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{alunoId}/aeronaves/{aeronaveId}/desautorizar")
    public ResponseEntity<Void> desautorizarAeronave(
            @PathVariable Long alunoId,
            @PathVariable Long aeronaveId) {

        alunoAeronaveService.desautorizarAluno(alunoId, aeronaveId);
        return ResponseEntity.ok().build();
    }
}