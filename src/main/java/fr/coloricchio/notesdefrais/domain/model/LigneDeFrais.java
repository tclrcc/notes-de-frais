package fr.coloricchio.notesdefrais.domain.model;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public record LigneDeFrais(
        UUID id,
        CategorieDepense categorie,
        LocalDate dateDepense,
        Montant montantTtc,
        Montant tva,
        String libelle,
        boolean justificatifFourni
) {

    public LigneDeFrais {
        Objects.requireNonNull(id, "L'identifiant est obligatoire");
        Objects.requireNonNull(categorie, "La catégorie est obligatoire");
        Objects.requireNonNull(dateDepense, "La date de dépense est obligatoire");
        Objects.requireNonNull(montantTtc, "Le montant TTC est obligatoire");
        Objects.requireNonNull(tva, "La TVA est obligatoire");

        if (montantTtc.estNul()) {
            throw new IllegalArgumentException("Le montant d'une ligne doit être strictement positif");
        }
        if (tva.estSuperieurA(montantTtc)) {
            throw new IllegalArgumentException("La TVA ne peut pas excéder le montant TTC");
        }
        if (libelle == null || libelle.isBlank()) {
            throw new IllegalArgumentException("Le libellé est obligatoire");
        }
        libelle = libelle.strip();
    }

    /**
     * Renvoie le montant hors taxe d'une note de frais
     */
    public Montant montantHt() {
        return new Montant(montantTtc.valeur().subtract(tva.valeur()));
    }
}
