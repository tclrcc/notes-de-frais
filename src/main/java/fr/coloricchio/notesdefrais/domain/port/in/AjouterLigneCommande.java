package fr.coloricchio.notesdefrais.domain.port.in;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record AjouterLigneCommande(
   UUID noteId, String codeCategorie, LocalDate dateDepense,
   BigDecimal montantTtc, BigDecimal tva, String libelle,
   boolean justificatifFourni) {}
