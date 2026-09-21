package fr.coloricchio.notesdefrais.domain.port.out;

import fr.coloricchio.notesdefrais.domain.model.NoteDeFrais;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteDeFraisRepository {
    Optional<NoteDeFrais> parId(UUID id);
    List<NoteDeFrais> parCollaborateur(UUID collaborateurId);
    void enregistrer(NoteDeFrais note);
}
