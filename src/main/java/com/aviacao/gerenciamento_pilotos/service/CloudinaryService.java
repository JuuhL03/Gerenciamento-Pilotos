package com.aviacao.gerenciamento_pilotos.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Faz upload de imagem base64 para o Cloudinary
     * @param base64Image String base64 da imagem (com ou sem prefixo data:image)
     * @param folder Pasta principal (ex: "local_pouso" ou "pagamento")
     * @param subFolder Sub-pasta (nome do aluno sanitizado)
     * @return URL da imagem no Cloudinary
     */
    public String uploadBase64Image(String base64Image, String folder, String subFolder) {
        if (base64Image == null || base64Image.trim().isEmpty()) {
            return null;
        }

        try {
            // Remove o prefixo "data:image/...;base64," se existir
            String base64Data = base64Image;
            if (base64Image.contains(",")) {
                base64Data = base64Image.split(",")[1];
            }

            // Decodifica base64 para bytes
            byte[] imageBytes = Base64.getDecoder().decode(base64Data);

            // Gera nome único para a imagem
            String uniqueFileName = UUID.randomUUID().toString();

            // Monta o path completo: folder/subFolder
            String fullPath = folder + "/" + subFolder;

            // Faz upload para o Cloudinary
            Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                    "folder", fullPath,
                    "public_id", uniqueFileName,
                    "resource_type", "image"
            ));

            String imageUrl = (String) uploadResult.get("secure_url");
            log.info("✅ Imagem enviada para Cloudinary: {}", imageUrl);

            return imageUrl;

        } catch (IOException e) {
            log.error("❌ Erro ao fazer upload da imagem para o Cloudinary", e);
            throw new RuntimeException("Erro ao fazer upload da imagem: " + e.getMessage());
        }
    }

    /**
     * Deleta uma imagem do Cloudinary pela URL
     */
    public void deleteImage(String imageUrl) {
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return;
        }

        try {
            String publicId = extractPublicIdFromUrl(imageUrl);
            if (publicId != null) {
                cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                log.info("✅ Imagem deletada do Cloudinary: {}", publicId);
            }
        } catch (IOException e) {
            log.error("❌ Erro ao deletar imagem do Cloudinary", e);
            // Não lançamos exceção para não bloquear a operação principal
        }
    }

    /**
     * Extrai o public_id de uma URL do Cloudinary
     * Exemplo: https://res.cloudinary.com/cloud/image/upload/v123/folder/subfolder/uuid.jpg
     * Retorna: folder/subfolder/uuid
     */
    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            // Padrão: .../upload/v{version}/{public_id}.{extension}
            Pattern pattern = Pattern.compile("/upload/v\\d+/(.+)\\.[a-zA-Z]+$");
            Matcher matcher = pattern.matcher(imageUrl);

            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            log.error("❌ Erro ao extrair public_id da URL: {}", imageUrl, e);
        }
        return null;
    }

    /**
     * Sanitiza o nome do aluno para usar como nome de pasta
     * Remove caracteres especiais, acentos e espaços
     * Exemplo: "João da Silva" -> "joao-da-silva"
     */
    public String sanitizeFolderName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "sem-nome";
        }

        return name.toLowerCase()
                .replaceAll("[áàâãäå]", "a")
                .replaceAll("[éèêë]", "e")
                .replaceAll("[íìîï]", "i")
                .replaceAll("[óòôõö]", "o")
                .replaceAll("[úùûü]", "u")
                .replaceAll("[ç]", "c")
                .replaceAll("[ñ]", "n")
                .replaceAll("[^a-z0-9]+", "-")  // Substitui caracteres não alfanuméricos por hífen
                .replaceAll("^-+|-+$", "")      // Remove hífens do início e fim
                .replaceAll("-{2,}", "-");      // Remove hífens duplicados
    }
}