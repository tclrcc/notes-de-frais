package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import fr.coloricchio.notesdefrais.domain.model.LigneDeFrais;
import fr.coloricchio.notesdefrais.domain.model.NoteDeFrais;
import fr.coloricchio.notesdefrais.domain.port.out.NoteDeFraisRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NoteDeFraisRepositoryAdapter implements NoteDeFraisRepository {

    private final NoteDeFraisJpaRepository notes;
    private final CategorieDepenseJpaRepository categories;
    private final EvenementStatutJpaRepository evenements;

    public NoteDeFraisRepositoryAdapter(NoteDeFraisJpaRepository notes, CategorieDepenseJpaRepository categories, EvenementStatutJpaRepository evenements) {
        this.notes = notes;
        this.categories = categories;
        this.evenements = evenements;
    }

    @Override
    public Optional<NoteDeFrais> parId(UUID id) {
        return notes.findWithLignesById(id).map(NoteDeFraisMapper::versDomaine);
    }

    @Override
    public List<NoteDeFrais> parCollaborateur(UUID collaborateurId) {
        return notes.findByCollaborateurId(collaborateurId).stream()
                .map(NoteDeFraisMapper::versDomaine)
                .toList();
    }

    @Override
    public void enregistrer(NoteDeFrais note) {
        NoteDeFraisEntity entite = notes.findWithLignesById(note.id())
                .orElseGet(() -> new NoteDeFraisEntity(
                        note.id(), note.reference(), note.collaborateurId(),
                        note.statut().name(), NoteDeFraisMapper.versColonne(note.periode()),
                        note.montantTotal().valeur(), note.creeeLe(), note.soumiseLe()));

        entite.mettreAJour(note.statut().name(), note.montantTotal().valeur(), note.soumiseLe());

        entite.getLignes().clear();
        for (LigneDeFrais ligne : note.lignes()) {
            CategorieDepenseEntity categorie = categories.getReferenceById(ligne.categorie().id());
            entite.getLignes().add(new LigneDeFraisEntity(
                 ligne.id(), entite, categorie, ligne.dateDepense(),
                 ligne.montantTtc().valeur(), ligne.tva().valeur(),
                 ligne.libelle(), ligne.justificatifFourni()));
        }

        notes.save(entite);

        note.evenementsNonPublies().forEach(e -> evenements.save(new EvenementStatutEntity(
                e.id(), e.noteId(),
                e.statutPrecedent() == null ? null : e.statutPrecedent().name(),
                e.statutNouveau().name(), e.auteurId(), e.survenuLe(), e.motif())));
    }
}
