package fr.coloricchio.notesdefrais.domain.port.out;

import fr.coloricchio.notesdefrais.domain.model.EvenementNote;

import java.util.List;

public interface PublicateurEvenements {
    void publier(List<EvenementNote> evenements);
}
