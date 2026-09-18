package fr.coloricchio.notesdefrais.domain.exception;

import fr.coloricchio.notesdefrais.domain.model.Montant;

public final class JustificatifManquant extends RegleMetierViolee {
    public JustificatifManquant(String categorie, Montant montant, String libelle) {
        super("Justificatif obligatoire pour la catégorie %s (%s - %s)"
                .formatted(categorie, montant, libelle));
    }
}
