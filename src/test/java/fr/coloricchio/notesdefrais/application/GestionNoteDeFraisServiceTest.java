package fr.coloricchio.notesdefrais.application;

import fr.coloricchio.notesdefrais.application.usecase.GestionNoteDeFraisService;
import fr.coloricchio.notesdefrais.domain.exception.*;
import fr.coloricchio.notesdefrais.domain.model.*;
import fr.coloricchio.notesdefrais.domain.port.in.*;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.time.*;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Cas d'usage - gestion d'une note de frais")
class GestionNoteDeFraisServiceTest {

    private static final UUID COLLABORATEUR = UUID.randomUUID();
    private static final UUID MANAGER = UUID.randomUUID();
    private static final Clock HORLOGE =
            Clock.fixed(Instant.parse("2026-09-18T10:00:00Z"), ZoneOffset.UTC);

    private NotesEnMemoire notes;
    private CategoriesEnMemoire categories;
    private CollaborateursEnMemoire collaborateurs;
    private PublicateurEspion publicateur;
    private GestionNoteDeFraisService service;

    @BeforeEach
    void setUp() {
        notes = new NotesEnMemoire();
        categories = new CategoriesEnMemoire();
        collaborateurs = new CollaborateursEnMemoire();
        publicateur = new PublicateurEspion();

        categories.ajouter(new CategorieDepense(
                UUID.randomUUID(), "PEAGE", "Péage", null, false));

        collaborateurs.ajouter(new Collaborateur(MANAGER, "marie.dubois@exemple.fr",
                "Dubois", "Marie", RoleCollaborateur.MANAGER, null));
        collaborateurs.ajouter(new Collaborateur(COLLABORATEUR, "tony.coloricchio@exemple.fr",
                "Coloricchio", "Tony", RoleCollaborateur.COLLABORATEUR, MANAGER));

        service = new GestionNoteDeFraisService(
                notes, categories, collaborateurs, publicateur, new ReferenceFixe(), HORLOGE);
    }

    private UUID creerNoteAvecUneLigne() {
        UUID id = service.executer(new CreerNoteCommande(COLLABORATEUR, Periode.de(2026, 9)));
        service.executer(new AjouterLigneCommande(id, "PEAGE",
                LocalDate.of(2026,9,10), new BigDecimal("12.50"),
                BigDecimal.ZERO, "Péage A42", false));
        return id;
    }

    @Test
    @DisplayName("créer une note l'enregistre en brouillon avec une référence")
    void creation() {
        UUID id = service.executer(new CreerNoteCommande(COLLABORATEUR, Periode.de(2026,9)));

        assertThat(notes.parId(id)).hasValueSatisfying(note -> {
            assertThat(note.statut()).isEqualTo(StatutNote.BROUILLON);
            assertThat(note.reference()).isEqualTo("NDF-TEST-001");
            assertThat(note.creeeLe()).isEqualTo(Instant.parse("2026-09-18T10:00:00Z"));
        });
    }

    @Test
    @DisplayName("créer une note pour un collaborateur inconnu échoue")
    void collaborateurInconnu() {
        assertThatThrownBy(() -> service.executer(
                new CreerNoteCommande(UUID.randomUUID(), Periode.de(2026, 9))))
                .isInstanceOf(CollaborateurIntrouvable.class);

        assertThat(notes.stockage).isEmpty();
    }

    @Test
    @DisplayName("ajouter une ligne sur une catégorie inconnue échoue")
    void categorieInconnue() {
        UUID id = service.executer(new CreerNoteCommande(COLLABORATEUR, Periode.de(2026, 9)));

        assertThatThrownBy(() -> service.executer(new AjouterLigneCommande(
                id, "INCONNUE", LocalDate.of(2026, 9, 10),
                new BigDecimal("10.00"), BigDecimal.ZERO, "X", false)))
                .isInstanceOf(CategorieIntrouvable.class);
    }

    @Test
    @DisplayName("agir sur une note inexistante échoue")
    void noteInconnue() {
        assertThatThrownBy(() -> service.executer(new SoumettreNoteCommande(UUID.randomUUID())))
                .isInstanceOf(NoteIntrouvable.class);
    }

    @Test
    @DisplayName("la soumission publie l'évènement correspondant")
    void soumissionPublieEvenement() {
        UUID id = creerNoteAvecUneLigne();
        service.executer(new SoumettreNoteCommande(id));

        assertThat(publicateur.publies).singleElement().satisfies(e -> {
           assertThat(e.statutNouveau()).isEqualTo(StatutNote.SOUMISE);
           assertThat(e.survenuLe()).isEqualTo(Instant.parse("2026-09-18T10:00:00Z"));
        });
    }

    @Test
    @DisplayName("un évènement n'est publié qu'une seule fois")
    void publicationUnique() {
        UUID id = creerNoteAvecUneLigne();
        service.executer(new SoumettreNoteCommande(id));
        service.valider(new DeciderSurNoteCommande(id, MANAGER, null));

        assertThat(publicateur.publies).hasSize(2);
        assertThat(publicateur.publies.get(1).statutNouveau()).isEqualTo(StatutNote.VALIDEE);
    }

    @Test
    @DisplayName("le cycle complet jusqu'au remboursement")
    void cycleComplet() {
        UUID id = creerNoteAvecUneLigne();
        service.executer(new SoumettreNoteCommande(id));
        service.valider(new DeciderSurNoteCommande(id, MANAGER, null));
        service.rembourser(new DeciderSurNoteCommande(id, MANAGER, null));

        assertThat(notes.parId(id)).hasValueSatisfying(n ->
                assertThat(n.statut()).isEqualTo(StatutNote.REMBOURSEE));
        assertThat(publicateur.publies).hasSize(3);
    }
}
