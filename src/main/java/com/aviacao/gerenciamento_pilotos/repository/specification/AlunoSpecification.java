package com.aviacao.gerenciamento_pilotos.repository.specification;

import com.aviacao.gerenciamento_pilotos.domain.entity.Aluno;
import com.aviacao.gerenciamento_pilotos.domain.entity.Teste;
import com.aviacao.gerenciamento_pilotos.domain.enums.StatusTeste;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AlunoSpecification {

    public static Specification<Aluno> comFiltros(String busca, StatusTeste status) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro de busca (nome, passaporte, telefone)
            if (busca != null && !busca.isBlank()) {
                String buscaPattern = "%" + busca.toLowerCase() + "%";
                Predicate nomePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("nome")), buscaPattern
                );
                Predicate passaportePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("passaporte")), buscaPattern
                );
                Predicate telefonePredicate = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("telefone")), buscaPattern
                );

                predicates.add(criteriaBuilder.or(nomePredicate, passaportePredicate, telefonePredicate));
            }

            // Filtro por status do teste MAIS RECENTE
            if (status != null) {
                // Subquery para pegar o teste mais recente de cada aluno
                Subquery<Long> subquery = query.subquery(Long.class);
                Root<Teste> testeRoot = subquery.from(Teste.class);

                subquery.select(criteriaBuilder.max(testeRoot.get("id")))
                        .where(criteriaBuilder.equal(testeRoot.get("aluno"), root));

                // Join com testes
                Join<Aluno, Teste> testeJoin = root.join("testes");

                // Filtrar: teste.id = (subquery) E teste.status = status
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.equal(testeJoin.get("id"), subquery),
                        criteriaBuilder.equal(testeJoin.get("status"), status)
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}