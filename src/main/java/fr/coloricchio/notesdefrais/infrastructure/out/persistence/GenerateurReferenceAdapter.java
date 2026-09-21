package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import fr.coloricchio.notesdefrais.domain.model.Periode;
import fr.coloricchio.notesdefrais.domain.port.out.GenerateurReference;
import org.springframework.stereotype.Component;

@Component
public class GenerateurReferenceAdapter implements GenerateurReference {

    private final NoteDeFraisJpaRepository notes;

    public GenerateurReferenceAdapter(NoteDeFraisJpaRepository notes) {
        this.notes = notes;
    }

    @Override
    public String pour(Periode periode) {
        long rang = notes.compterSurPeriode(NoteDeFraisMapper.versColonne(periode)) + 1;
        return "NDF-%d-%02d-%03d".formatted(
                periode.valeur().getYear(), periode.valeur().getMonthValue(), rang);
    }
}
