package fr.coloricchio.notesdefrais.domain.model;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum StatutNote {

    BROUILLON,
    SOUMISE,
    VALIDEE,
    REJETEE,
    REMBOURSEE;

    private static final Map<StatutNote, Set<StatutNote>> TRANSITIONS = Map.of(
        BROUILLON, EnumSet.of(SOUMISE),
        SOUMISE, EnumSet.of(VALIDEE, REJETEE),
        VALIDEE, EnumSet.of(REMBOURSEE),
        REJETEE, EnumSet.of(BROUILLON),
        REMBOURSEE, EnumSet.noneOf(StatutNote.class)
    );

    public boolean peutAllerVers(StatutNote cible) {
        return TRANSITIONS.get(this).contains(cible);
    }

    /** Seule une note de frais au statut "BROUILLON" peut être modifiée */
    public boolean estModifiable() {
        return this == BROUILLON;
    }

    public boolean estFinal() {
        return TRANSITIONS.get(this).isEmpty();
    }
}
