package fr.coloricchio.notesdefrais.domain;

import fr.coloricchio.notesdefrais.domain.exception.*;
import fr.coloricchio.notesdefrais.domain.model.LigneDeFrais;
import fr.coloricchio.notesdefrais.domain.model.Montant;
import fr.coloricchio.notesdefrais.domain.model.NoteDeFrais;
import fr.coloricchio.notesdefrais.domain.model.StatutNote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static fr.coloricchio.notesdefrais.domain.Fixtures.*;
import static org.assertj.core.api.Assertions.*;

@DisplayName("Agrégat NoteDeFrais")
class NoteDeFraisTest {

    @Nested
    @DisplayName("A la création")
    class Creation {

        @Test
        @DisplayName("la note est en brouillon, vide et à zéro euro")
        void etatInitial() {
            NoteDeFrais note = noteVierge();
            assertThat(note.statut()).isEqualTo(StatutNote.BROUILLON);
            assertThat(note.lignes()).isEmpty();
            assertThat(note.montantTotal()).isEqualTo(Montant.ZERO);
            assertThat(note.soumiseLe()).isNull();
        }
    }

    @Nested
    @DisplayName("Ajout de lignes")
    class AjoutLigne {

        @Test
        @DisplayName("accepte une ligne conforme")
        void ligneConforme() {
            NoteDeFrais note = noteVierge();
            note.ajouterLigne(ligneValide());
            assertThat(note.lignes()).hasSize(1);
        }

        @Test
        @DisplayName("refuse une dépense hors de la période de la note")
        void horsPeriode() {
            NoteDeFrais note = noteVierge();
            LigneDeFrais aout = ligne(peage(), "10.00", LocalDate.of(2026,8,31), false);

            assertThatThrownBy(() -> note.ajouterLigne(aout))
                    .isInstanceOf(DateHorsPeriode.class);
        }

        @Test
        @DisplayName("refuse un montant au-dessus du plafond de la catégorie")
        void plafondDepasse() {
            NoteDeFrais note = noteVierge();
            LigneDeFrais trop = ligne(repas(), "42.00", LocalDate.of(2026,9,5), true);

            assertThatThrownBy(() -> note.ajouterLigne(trop))
                    .isInstanceOf(PlafondDepasse.class)
                    .hasMessageContaining("REPAS");
        }

        @Test
        @DisplayName("refuse une ligne sans justif quand la catégorie l'exige")
        void justificatifManquant() {
            NoteDeFrais note = noteVierge();
            LigneDeFrais sansJustif = ligne(repas(), "20.00", LocalDate.of(2026,9,5), false);

            assertThatThrownBy(() -> note.ajouterLigne(sansJustif))
                    .isInstanceOf(JustificatifManquant.class);
        }

        @Test
        @DisplayName("la liste exposée n'est pas modifiable de l'extérieur")
        void listeImmuable() {
            NoteDeFrais note = noteVierge();
            assertThatThrownBy(() -> note.lignes().add(ligneValide()))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("le total est la somme des lignes")
        void total() {
            NoteDeFrais note = noteVierge();
            note.ajouterLigne(ligne(peage(), "12.50", LocalDate.of(2026, 9, 3), false));
            note.ajouterLigne(ligne(peage(), "7.30", LocalDate.of(2026, 9, 4), false));

            assertThat(note.montantTotal()).isEqualTo(Montant.de("19.80"));
        }
    }

    @Nested
    @DisplayName("Soumission")
    class Soumission {

        @Test
        @DisplayName("refuse une note sans aucune ligne")
        void noteVide() {
            NoteDeFrais note = noteVierge();
            assertThatThrownBy(() -> note.soumettre(MAINTENANT))
                    .isInstanceOf(NoteVide.class);
        }

        @Test
        @DisplayName("passe en SOUMISE et produit un événement")
        void soumissionValide() {
            NoteDeFrais note = noteVierge();
            note.ajouterLigne(ligneValide());
            note.soumettre(MAINTENANT);

            assertThat(note.statut()).isEqualTo(StatutNote.SOUMISE);
            assertThat(note.soumiseLe()).isEqualTo(MAINTENANT);
            assertThat(note.evenementsNonPublies())
                    .singleElement()
                    .satisfies(e -> {
                        assertThat(e.statutPrecedent()).isEqualTo(StatutNote.BROUILLON);
                        assertThat(e.statutNouveau()).isEqualTo(StatutNote.SOUMISE);
                        assertThat(e.cleDePartitionnement()).isEqualTo(note.id().toString());
                    });
        }

        @Test
        @DisplayName("une note soumise n'est plus modifiable")
        void plusModifiable() {
            NoteDeFrais note = noteVierge();
            note.ajouterLigne(ligneValide());
            note.soumettre(MAINTENANT);

            assertThatThrownBy(() -> note.ajouterLigne(ligneValide()))
                    .isInstanceOf(NoteNonModifiable.class);
        }
    }

    @Nested
    @DisplayName("Validation et rejet")
    class Validation {

        private NoteDeFrais noteSoumise() {
            NoteDeFrais note = noteVierge();
            note.ajouterLigne(ligneValide());
            note.soumettre(MAINTENANT);
            note.marquerEvenementsPublies();
            return note;
        }

        @Test
        @DisplayName("une note soumise peut être validée")
        void validation() {
            NoteDeFrais note = noteSoumise();
            note.valider(MANAGER, MAINTENANT);

            assertThat(note.statut()).isEqualTo(StatutNote.VALIDEE);
            assertThat(note.evenementsNonPublies()).singleElement()
                    .satisfies(e -> assertThat(e.auteurId()).isEqualTo(MANAGER));
        }

        @Test
        @DisplayName("une note en brouillon ne peut pas être validée")
        void validationInterdite() {
            NoteDeFrais note = noteVierge();
            assertThatThrownBy(() -> note.valider(MANAGER, MAINTENANT))
                    .isInstanceOf(TransitionInterdite.class);
        }

        @Test
        @DisplayName("le rejet exige un motif")
        void rejetSansMotif() {
            NoteDeFrais note = noteSoumise();
            assertThatThrownBy(() -> note.rejeter(MANAGER, "   ", MAINTENANT))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("le motif de rejet est conservé dans l'événement")
        void rejetMotive() {
            NoteDeFrais note = noteSoumise();
            note.rejeter(MANAGER, "Justificatif illisible", MAINTENANT);

            assertThat(note.statut()).isEqualTo(StatutNote.REJETEE);
            assertThat(note.evenementsNonPublies()).singleElement()
                    .satisfies(e -> assertThat(e.motif()).isEqualTo("Justificatif illisible"));
        }

        @Test
        @DisplayName("une note rejetée peut repartir en brouillon puis être resoumise")
        void cycleComplet() {
            NoteDeFrais note = noteSoumise();
            note.rejeter(MANAGER, "À corriger", MAINTENANT);

            assertThat(note.statut().peutAllerVers(StatutNote.BROUILLON)).isTrue();
        }

        @Test
        @DisplayName("une note remboursée est dans un état final")
        void remboursement() {
            NoteDeFrais note = noteSoumise();
            note.valider(MANAGER, MAINTENANT);
            note.rembourser(COMPTABLE, MAINTENANT);

            assertThat(note.statut()).isEqualTo(StatutNote.REMBOURSEE);
            assertThatThrownBy(() -> note.valider(MANAGER, MAINTENANT))
                    .isInstanceOf(TransitionInterdite.class);
        }
    }
}
