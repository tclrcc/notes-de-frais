package fr.coloricchio.notesdefrais.domain.exception;

import fr.coloricchio.notesdefrais.domain.model.Montant;

public final class PlafondDepasse extends RegleMetierViolee {
    public PlafondDepasse(String cateforie, Montant montant, Montant plafond) {
        super("Catégorie %s : montant %s supérieur au plafond %s".formatted(cateforie, montant, plafond));
    }
}
