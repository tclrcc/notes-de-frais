package fr.coloricchio.notesdefrais.application.usecase;

import fr.coloricchio.notesdefrais.domain.exception.CategorieIntrouvable;
import fr.coloricchio.notesdefrais.domain.exception.NoteIntrouvable;
import fr.coloricchio.notesdefrais.domain.model.CategorieDepense;
import fr.coloricchio.notesdefrais.domain.model.LigneDeFrais;
import fr.coloricchio.notesdefrais.domain.model.Montant;
import fr.coloricchio.notesdefrais.domain.model.NoteDeFrais;
import fr.coloricchio.notesdefrais.domain.port.in.*;
import fr.coloricchio.notesdefrais.domain.port.out.CategorieDepenseRepository;
import fr.coloricchio.notesdefrais.domain.port.out.GenerateurReference;
import fr.coloricchio.notesdefrais.domain.port.out.NoteDeFraisRepository;
import fr.coloricchio.notesdefrais.domain.port.out.PublicateurEvenements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.function.BiConsumer;

@Service
public class GestionNoteDeFraisService implements AjouterLigne, CreerNote, DeciderSurNote, SoumettreNote {

    private final NoteDeFraisRepository notes;
    private final CategorieDepenseRepository categories;
    private final PublicateurEvenements evenements;
    private final GenerateurReference references;
    private final Clock horloge;

    public GestionNoteDeFraisService(NoteDeFraisRepository notes, CategorieDepenseRepository categories,
                                     PublicateurEvenements evenements, GenerateurReference references,
                                     Clock horloge) {
        this.notes = notes;
        this.categories = categories;
        this.evenements = evenements;
        this.references = references;
        this.horloge = horloge;
    }

    @Override
    @Transactional
    public UUID executer(CreerNoteCommande commande) {
        NoteDeFrais note = NoteDeFrais.creer(
            UUID.randomUUID(),
            references.pour(commande.periode()),
            commande.collaborateurId(),
            commande.periode(),
            Instant.now(horloge));

        notes.enregistrer(note);
        return note.id();
    }

    @Override
    @Transactional
    public UUID executer(AjouterLigneCommande commande) {
        NoteDeFrais note = charger(commande.noteId());
        CategorieDepense categorie = categories.parCode(commande.codeCategorie())
                .orElseThrow(() -> new CategorieIntrouvable(commande.codeCategorie()));

        note.ajouterLigne(new LigneDeFrais(
                UUID.randomUUID(), categorie, commande.dateDepense(),
                new Montant(commande.montantTtc()), new Montant(commande.tva()),
                commande.libelle(), commande.justificatifFourni()));

        notes.enregistrer(note);
        return note.id();
    }

    @Override
    @Transactional
    public void executer(SoumettreNoteCommande commande) {
        appliquer(commande.noteId(), NoteDeFrais::soumettre);
    }

    @Override
    @Transactional
    public void valider(DeciderSurNoteCommande c) {
        appliquer(c.noteId(), (note, t) -> note.valider(c.decideurId(), t));
    }

    @Override
    @Transactional
    public void rejeter(DeciderSurNoteCommande c) {
        appliquer(c.noteId(), (note, t) -> note.rejeter(c.decideurId(), c.motif(), t));
    }

    @Override
    @Transactional
    public void rembourser(DeciderSurNoteCommande c) {
        appliquer(c.noteId(), (note, t) -> note.rembourser(c.decideurId(), t));
    }

    private void appliquer(UUID noteId, BiConsumer<NoteDeFrais, Instant> action) {
        NoteDeFrais note = charger(noteId);
        action.accept(note, Instant.now(horloge));
        notes.enregistrer(note);
        evenements.publier(note.evenementsNonPublies());
        note.marquerEvenementsPublies();
    }

    private NoteDeFrais charger(UUID id) {
        return notes.parId(id).orElseThrow(() -> new NoteIntrouvable(id));
    }
}
