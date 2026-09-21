package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import fr.coloricchio.notesdefrais.domain.model.*;

import java.time.LocalDate;
import java.time.YearMonth;

final class NoteDeFraisMapper {

    private NoteDeFraisMapper() {}

    static CategorieDepense versDomaine(CategorieDepenseEntity e) {
        return new CategorieDepense(
                e.getId(), e.getCode(), e.getLibelle(),
                e.getPlafondUnitaire() == null ? null : new Montant(e.getPlafondUnitaire()),
                e.isJustificatifObligatoire());
    }

    static NoteDeFrais versDomaine(NoteDeFraisEntity e) {
        var lignes = e.getLignes().stream()
                .map(l -> new LigneDeFrais(
                        l.getId(), versDomaine(l.getCategorie()), l.getDateDepense(),
                        new Montant(l.getMontantTtc()), new Montant(l.getTva()),
                        l.getLibelle(), l.isJustificatifFourni()))
                .toList();

        return NoteDeFrais.reconstituer(
                e.getId(), e.getReference(), e.getCollaborateurId(),
                new Periode(YearMonth.from(e.getPeriode())),
                e.getCreeeLe(), StatutNote.valueOf(e.getStatut()),
                e.getSoumiseLe(), lignes);
    }

    static LocalDate versColonne(Periode periode) {
        return periode.valeur().atDay(1);
    }
}
