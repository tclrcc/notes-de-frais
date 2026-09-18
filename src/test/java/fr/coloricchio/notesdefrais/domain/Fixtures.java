package fr.coloricchio.notesdefrais.domain;

import fr.coloricchio.notesdefrais.domain.model.*;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Jeux de données pour les tests du package domain
 */
final class Fixtures {

    static final UUID COLLABORATEUR = UUID.randomUUID();
    static final UUID MANAGER = UUID.randomUUID();
    static final UUID COMPTABLE = UUID.randomUUID();
    static final Instant MAINTENANT = Instant.parse("2026-09-18T10:00:00Z");
    static final Periode SEPTEMBRE = Periode.de(2026,9);

    private Fixtures() {}

    /** Plafond de 25€ pour un repas, avec justif obligatoire */
    static CategorieDepense repas() {
        return new CategorieDepense(UUID.randomUUID(), "REPAS", "Repas d'affaires",
                Montant.de("25.00"), true);
    }

    /** Pas de plafond pour un péage, ni de justif obligatoire */
    static CategorieDepense peage() {
        return new CategorieDepense(UUID.randomUUID(), "PEAGE", "Péage autoroute",
                null, false);
    }

    /** Création d'une note de frais vierge, sans ligne de frais */
    static NoteDeFrais noteVierge() {
        return NoteDeFrais.creer(UUID.randomUUID(), "NDF-2026-09-001",
                COLLABORATEUR, SEPTEMBRE, MAINTENANT);
    }

    /** Création d'une ligne de frais d'un déjuner */
    static LigneDeFrais ligne(CategorieDepense categorie, String montantTtc,
                              LocalDate date, boolean justificatif) {
        return new LigneDeFrais(UUID.randomUUID(), categorie, date,
                Montant.de(montantTtc), Montant.ZERO, "Déjeuner client", justificatif);
    }

    static LigneDeFrais ligneValide() {
        return ligne(peage(), "12.50", LocalDate.of(2026, 9, 10), false);
    }
}
