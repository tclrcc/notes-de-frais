package fr.coloricchio.notesdefrais.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Catégorie d'une dépense ainsi que ses règles
 * plafond unitaire est optionnel
 */
public record CategorieDepense(
        UUID id,
        String code,
        String libelle,
        Montant plafondUnitaire,
        boolean justificatifObligatoire
) {

    public CategorieDepense {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        Objects.requireNonNull(code, "Le code est obligatoire");
        Objects.requireNonNull(libelle, "Le libellé est obligatoire");
    }

    public boolean sansPlafond() {
        return plafondUnitaire == null;
    }

    /**
     * Vérifie que le plafond n'est pas dépassé si existant
     */
    public boolean depassePlafond(Montant montant) {
        return !sansPlafond() && montant.estSuperieurA(plafondUnitaire);
    }

}
