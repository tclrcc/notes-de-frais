package fr.coloricchio.notesdefrais.application;

import fr.coloricchio.notesdefrais.domain.model.*;
import fr.coloricchio.notesdefrais.domain.port.out.*;

import java.util.*;

class NotesEnMemoire implements NoteDeFraisRepository {
    final Map<UUID, NoteDeFrais> stockage = new LinkedHashMap<>();

    public Optional<NoteDeFrais> parId(UUID id) {return Optional.ofNullable(stockage.get(id));}
    public void enregistrer(NoteDeFrais note) {stockage.put(note.id(), note);}

    public List<NoteDeFrais> parCollaborateur(UUID collaborateurId) {
        return stockage.values().stream()
                .filter(n -> n.collaborateurId().equals(collaborateurId))
                .toList();
    }
}

class CategoriesEnMemoire implements CategorieDepenseRepository {
    final Map<String, CategorieDepense> stockage = new HashMap<>();

    void ajouter(CategorieDepense c) {stockage.put(c.code(), c);}
    public Optional<CategorieDepense> parCode(String code) {return Optional.ofNullable(stockage.get(code));}
}

class PublicateurEspion implements PublicateurEvenements {
    final List<EvenementNote> publies = new ArrayList<>();
    public void publier(List<EvenementNote> evenements) {publies.addAll(evenements);}
}

class ReferenceFixe implements GenerateurReference {
    public String pour(Periode periode) {return "NDF-TEST-001";}
}
