package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import fr.coloricchio.notesdefrais.domain.model.LigneDeFrais;
import fr.coloricchio.notesdefrais.domain.model.NoteDeFrais;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record NoteDeFraisResponse(
    UUID id,
    String reference,
    UUID collaborateurId,
    String statut,
    String periode,
    BigDecimal montantTotal,
    Instant creeeLe,
    Instant soumiseLe,
    List<LigneResponse> lignes
) {
    public static NoteDeFraisResponse depuis(NoteDeFrais note) {
        return new NoteDeFraisResponse(
                note.id(), note.reference(), note.collaborateurId(),
                note.statut().name(), note.periode().valeur().toString(),
                note.montantTotal().valeur(), note.creeeLe(), note.soumiseLe(),
                note.lignes().stream().map(LigneResponse::depuis).toList());
    }

    public record LigneResponse(
        UUID id, String categorie, LocalDate dateDepense,
        BigDecimal montantTtc, BigDecimal tva, BigDecimal montantHt,
        String libelle, boolean justificatifFourni
    ) {
        static LigneResponse depuis(LigneDeFrais l) {
            return new LigneResponse(
                    l.id(), l.categorie().code(), l.dateDepense(),
                    l.montantTtc().valeur(), l.tva().valeur(), l.montantHt().valeur(),
                    l.libelle(), l.justificatifFourni());
        }
    }
}
