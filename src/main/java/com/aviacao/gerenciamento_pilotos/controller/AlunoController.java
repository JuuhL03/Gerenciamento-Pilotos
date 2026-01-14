package com.aviacao.gerenciamento_pilotos.controller;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import com.aviacao.gerenciamento_pilotos.dto.request.*;
import com.aviacao.gerenciamento_pilotos.dto.response.*;
import com.aviacao.gerenciamento_pilotos.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/alunos")
@RequiredArgsConstructor
public class AlunoController {

    private final AlunoService alunoService;
    private final TesteService testeService;
    private final AlunoAeronaveService alunoAeronaveService;
    private final AlunoLocalPousoService alunoLocalPousoService;
    private final LocalPousoService localPousoService;

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

        Page<AlunoResumoDTO> response = alunos.map(aluno -> {
            Teste testeAtual = aluno.getTestes() != null && !aluno.getTestes().isEmpty()
                    ? aluno.getTestes().stream()
                    .filter(Teste::getAtivo)
                    .max(Comparator.comparing(Teste::getId))
                    .orElse(null)
                    : null;

            return AlunoResumoDTO.fromEntity(aluno, testeAtual);
        });

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Long id) {
        Aluno aluno = alunoService.buscarPorIdComTestes(id);
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

    @GetMapping("/com-aeronaves")
    public ResponseEntity<Page<AlunoComAeronavesDTO>> listarTodosComAeronaves(Pageable pageable) {
        Page<Aluno> alunos = alunoService.listarTodos(pageable);

        Page<AlunoComAeronavesDTO> response = alunos.map(aluno -> {
            List<AlunoAeronaveDTO> aeronaves = alunoAeronaveService.listarAeronavesDoAluno(aluno.getId());
            return AlunoComAeronavesDTO.fromEntity(aluno, aeronaves);
        });

        return ResponseEntity.ok(response);
    }

    /**
     * Lista todos os alunos com seus instrutores e locais de pouso
     */
    @GetMapping("/locais-pouso")
    public ResponseEntity<Page<AlunoComLocaisEInstrutorDTO>> listarAlunosComLocaisPouso(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome") String sort,
            @RequestParam(defaultValue = "asc") String direction) {

        if (page < 0) page = 0;
        if (size < 1) size = 10;
        if (size > 100) size = 100;

        String sortField = switch (sort.toLowerCase()) {
            case "nome", "passaporte", "id" -> sort;
            default -> "nome";
        };

        Sort sortObj = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortField).descending()
                : Sort.by(sortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<AlunoComLocaisEInstrutorDTO> alunos = alunoLocalPousoService.listarTodosAlunosComLocaisPouso(pageable);

        return ResponseEntity.ok(alunos);
    }

    /**
     * Lista os locais de pouso de um aluno específico
     */
    @GetMapping("/{alunoId}/locais-pouso")
    public ResponseEntity<List<LocalPousoDTO>> listarLocaisPorAluno(@PathVariable Long alunoId) {
        List<LocalPouso> locais = localPousoService.listarPorAluno(alunoId);
        List<LocalPousoDTO> dtos = locais.stream()
                .map(LocalPousoDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    /**
     * Cadastra um novo local de pouso para um aluno
     */
    @PostMapping("/{alunoId}/locais-pouso")
    public ResponseEntity<LocalPousoDTO> cadastrarLocalPouso(
            @PathVariable Long alunoId,
            @Valid @RequestBody CadastrarLocalPousoRequest request) {

        LocalPouso localPouso = LocalPouso.builder()
                .nome(request.getNome())
                .build();

        LocalPouso saved = localPousoService.cadastrar(localPouso, alunoId, request.getImagemUrl());

        // ✅ RETORNAR DTO
        return ResponseEntity.status(HttpStatus.CREATED).body(LocalPousoDTO.fromEntity(saved));
    }

    /**
     * Atualiza um local de pouso existente
     */
    @PutMapping("/locais-pouso/{localId}")
    public ResponseEntity<LocalPousoDTO> atualizarLocalPouso(
            @PathVariable Long localId,
            @Valid @RequestBody AtualizarLocalPousoRequest request) {

        LocalPouso atualizado = localPousoService.atualizar(
                localId,
                request.getNome(),
                request.getImagemUrl()
        );

        // ✅ RETORNAR DTO
        return ResponseEntity.ok(LocalPousoDTO.fromEntity(atualizado));
    }

    /**
     * Deleta (soft delete) um local de pouso
     */
    @DeleteMapping("/locais-pouso/{localId}")
    public ResponseEntity<Void> deletarLocalPouso(@PathVariable Long localId) {
        localPousoService.deletar(localId);
        return ResponseEntity.noContent().build();
    }
}