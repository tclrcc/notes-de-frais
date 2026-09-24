package fr.coloricchio.notesdefrais.application;

import fr.coloricchio.notesdefrais.domain.model.*;
import fr.coloricchio.notesdefrais.domain.port.out.*;

import java.util.*;

class NotesEnMemoire implements NoteDeFraisRepository {
    final Map<UUID, NoteDeFrais> stockage = new LinkedHashMap<>();

    @Override
    public Optional<NoteDeFrais> parId(UUID id) {return Optional.ofNullable(stockage.get(id));}

    @Override
    public void enregistrer(NoteDeFrais note) {stockage.put(note.id(), note);}

    @Override
    public List<NoteDeFrais> parCollaborateur(UUID collaborateurId) {
        return stockage.values().stream()
                .filter(n -> n.collaborateurId().equals(collaborateurId))
                .toList();
    }
}

class CategoriesEnMemoire implements CategorieDepenseRepository {
    final Map<String, CategorieDepense> stockage = new LinkedHashMap<>();

    void ajouter(CategorieDepense c) {stockage.put(c.code(), c);}

    @Override
    public Optional<CategorieDepense> parCode(String code) {return Optional.ofNullable(stockage.get(code));}

    @Override
    public List<CategorieDepense> toutes() {return List.copyOf(stockage.values());}
}

class CollaborateursEnMemoire implements CollaborateurRepository {
    final Map<UUID, Collaborateur> stockage = new LinkedHashMap<>();

    void ajouter(Collaborateur c) {stockage.put(c.id(), c);}

    @Override
    public Optional<Collaborateur> parId(UUID id) {return Optional.ofNullable(stockage.get(id));}

    @Override
    public List<Collaborateur> tous() {return List.copyOf(stockage.values());}
}

class PublicateurEspion implements PublicateurEvenements {
    final List<EvenementNote> publies = new ArrayList<>();

    @Override
    public void publier(List<EvenementNote> evenements) {publies.addAll(evenements);}
}

class ReferenceFixe implements GenerateurReference {
    @Override
    public String pour(Periode periode) {return "NDF-TEST-001";}
}
