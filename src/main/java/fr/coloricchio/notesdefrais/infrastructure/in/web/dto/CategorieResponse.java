package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import fr.coloricchio.notesdefrais.domain.model.CategorieDepense;

import java.math.BigDecimal;

public record CategorieResponse(
    String code,
    String libelle,
    BigDecimal plafondUnitaire,
    boolean justificatifObligatoire
) {
    public static CategorieResponse depuis(CategorieDepense c) {
        return new CategorieResponse(
                c.code(), c.libelle(),
                c.plafondUnitaire() == null ? null : c.plafondUnitaire().valeur(),
                c.justificatifObligatoire());
    }
}
