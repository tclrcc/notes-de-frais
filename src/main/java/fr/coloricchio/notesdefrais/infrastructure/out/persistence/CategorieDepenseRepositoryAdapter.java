package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import fr.coloricchio.notesdefrais.domain.model.CategorieDepense;
import fr.coloricchio.notesdefrais.domain.port.out.CategorieDepenseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CategorieDepenseRepositoryAdapter implements CategorieDepenseRepository {

    private final CategorieDepenseJpaRepository jpa;

    public CategorieDepenseRepositoryAdapter(CategorieDepenseJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<CategorieDepense> parCode(String code) {
        return jpa.findByCode(code).map(NoteDeFraisMapper::versDomaine);
    }
}
