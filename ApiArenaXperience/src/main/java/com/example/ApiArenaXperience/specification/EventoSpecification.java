package com.example.ApiArenaXperience.specification;

import com.example.ApiArenaXperience.model.event.Evento;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventoSpecification {
    public static Specification<Evento> filtrarEventos(String name, LocalDate date, Integer capacity) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null && !name.isEmpty()) {
                predicates.add(criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                ));
            }
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), date));
            }
            if (capacity != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("capacity"), capacity));
            }

            if (!predicates.isEmpty()) {
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

            return criteriaBuilder.conjunction();
        };
    }
}
