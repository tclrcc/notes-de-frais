package fr.coloricchio.notesdefrais.domain.port.out;

import fr.coloricchio.notesdefrais.domain.model.CategorieDepense;

import java.util.List;
import java.util.Optional;

public interface CategorieDepenseRepository {
    Optional<CategorieDepense> parCode(String code);
    List<CategorieDepense> toutes();
}
