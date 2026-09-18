package fr.coloricchio.notesdefrais.domain.model;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Objects;

/** Mois d'une note de frais */
public record Periode(YearMonth valeur) {

    public Periode {
        Objects.requireNonNull(valeur, "La période est obligatoire");
    }

    public static Periode de(int annee, int mois) {
        return new Periode(YearMonth.of(annee, mois));
    }

    public boolean contient(LocalDate date) {
        return YearMonth.from(date).equals(valeur);
    }
}
