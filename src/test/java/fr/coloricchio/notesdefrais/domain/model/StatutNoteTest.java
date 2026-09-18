package fr.coloricchio.notesdefrais.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Machine à états d'une note de frais")
class StatutNoteTest {

    @ParameterizedTest(name = "{0} -> {1} est autorisé")
    @CsvSource({
            "BROUILLON, SOUMISE",
            "SOUMISE, VALIDEE",
            "SOUMISE, REJETEE",
            "VALIDEE, REMBOURSEE",
            "REJETEE, BROUILLON"
    })
    void transitionsAutorisees(StatutNote depart, StatutNote cible) {
        assertThat(depart.peutAllerVers(cible)).isTrue();
    }

    @ParameterizedTest(name = "{0} -> {1} est interdit")
    @CsvSource({
            "BROUILLON,  VALIDEE",
            "BROUILLON,  REMBOURSEE",
            "SOUMISE,    REMBOURSEE",
            "VALIDEE,    REJETEE",
            "REMBOURSEE, BROUILLON",
            "REMBOURSEE, VALIDEE"
    })
    void transitionsInterdites(StatutNote depart, StatutNote cible) {
        assertThat(depart.peutAllerVers(cible)).isFalse();
    }

    @Test
    @DisplayName("seul le brouillon est modifiable")
    void seulBrouillonModifiable() {
        assertThat(StatutNote.BROUILLON.estModifiable()).isTrue();
        assertThat(StatutNote.SOUMISE.estModifiable()).isFalse();
        assertThat(StatutNote.VALIDEE.estModifiable()).isFalse();
    }

    @Test
    @DisplayName("remboursé est un état final")
    void etatFinal() {
        assertThat(StatutNote.REMBOURSEE.estFinal()).isTrue();
        assertThat(StatutNote.SOUMISE.estFinal()).isFalse();
    }
}
