package com.aviacao.gerenciamento_pilotos.service;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.LocalPouso;
import com.aviacao.gerenciamento_pilotos.exception.BusinessException;
import com.aviacao.gerenciamento_pilotos.exception.NotFoundException;
import com.aviacao.gerenciamento_pilotos.repository.AlunoRepository;
import com.aviacao.gerenciamento_pilotos.repository.LocalPousoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocalPousoService {

    private final LocalPousoRepository localPousoRepository;
    private final AlunoRepository alunoRepository;
    private final CloudinaryService cloudinaryService;

    @Transactional(readOnly = true)
    public List<LocalPouso> listarPorAluno(Long alunoId) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + alunoId));

        // ✅ NOVA VALIDAÇÃO: Verificar se tem teste AUTORIZADO
        if (!aluno.temTesteAutorizado()) {
            throw new BusinessException("Aluno não possui teste autorizado para ter locais de pouso");
        }

        return localPousoRepository.findByAlunoIdWithAlunoFetched(alunoId);
    }

    @Transactional
    public LocalPouso cadastrar(LocalPouso localPouso, Long alunoId, String imagemBase64) {
        Aluno aluno = alunoRepository.findById(alunoId)
                .orElseThrow(() -> new NotFoundException("Aluno não encontrado com ID: " + alunoId));

        // ✅ NOVA VALIDAÇÃO: Verificar se tem teste AUTORIZADO
        if (!aluno.temTesteAutorizado()) {
            throw new BusinessException("Aluno não possui teste autorizado para cadastrar locais de pouso");
        }

        // Verificar nome duplicado
        if (localPousoRepository.existsByNomeAndAlunoId(localPouso.getNome(), alunoId)) {
            throw new BusinessException("Já existe um local de pouso com o nome '" + localPouso.getNome() + "' para este aluno");
        }

        // Upload da imagem no Cloudinary (se fornecida)
        if (imagemBase64 != null && !imagemBase64.trim().isEmpty()) {
            String alunoFolder = cloudinaryService.sanitizeFolderName(aluno.getNome());
            String imageUrl = cloudinaryService.uploadBase64Image(imagemBase64, "local_pouso", alunoFolder);
            localPouso.setImagemUrl(imageUrl);
            log.info("✅ Imagem enviada para Cloudinary: {}", imageUrl);
        }

        localPouso.setAluno(aluno);
        localPouso.setAtivo(true);

        LocalPouso saved = localPousoRepository.save(localPouso);
        saved.getAluno().getNome();

        return saved;
    }

    @Transactional
    public LocalPouso atualizar(Long id, String nome, String novaImagemBase64) {
        LocalPouso localPouso = localPousoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local de pouso não encontrado com ID: " + id));

        // ✅ NOVA VALIDAÇÃO: Verificar se tem teste AUTORIZADO
        if (!localPouso.getAluno().temTesteAutorizado()) {
            throw new BusinessException("Aluno não possui teste autorizado para atualizar locais de pouso");
        }

        // Atualizar nome se fornecido
        if (nome != null && !nome.trim().isEmpty()) {
            if (localPousoRepository.existsByNomeAndAlunoIdAndIdNot(nome, localPouso.getAluno().getId(), id)) {
                throw new BusinessException("Já existe outro local de pouso com o nome '" + nome + "' para este aluno");
            }
            localPouso.setNome(nome);
        }

        // Atualizar imagem se fornecida
        if (novaImagemBase64 != null && !novaImagemBase64.trim().isEmpty()) {
            if (localPouso.getImagemUrl() != null) {
                cloudinaryService.deleteImage(localPouso.getImagemUrl());
                log.info("✅ Imagem antiga deletada do Cloudinary");
            }

            String alunoFolder = cloudinaryService.sanitizeFolderName(localPouso.getAluno().getNome());
            String novaImagemUrl = cloudinaryService.uploadBase64Image(novaImagemBase64, "local_pouso", alunoFolder);
            localPouso.setImagemUrl(novaImagemUrl);
            log.info("✅ Nova imagem enviada para Cloudinary: {}", novaImagemUrl);
        }

        LocalPouso updated = localPousoRepository.save(localPouso);
        updated.getAluno().getNome();

        return updated;
    }

    @Transactional
    public void deletar(Long id) {
        LocalPouso localPouso = localPousoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local de pouso não encontrado com ID: " + id));

        // ✅ NOVA VALIDAÇÃO: Verificar se tem teste AUTORIZADO (opcional para delete)
        if (!localPouso.getAluno().temTesteAutorizado()) {
            throw new BusinessException("Aluno não possui teste autorizado");
        }

        if (localPouso.getImagemUrl() != null) {
            cloudinaryService.deleteImage(localPouso.getImagemUrl());
            log.info("✅ Imagem deletada do Cloudinary ao deletar local de pouso");
        }

        localPouso.setAtivo(false);
        localPousoRepository.save(localPouso);
    }

    @Transactional(readOnly = true)
    public LocalPouso buscarPorId(Long id) {
        return localPousoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local de pouso não encontrado com ID: " + id));
    }
}