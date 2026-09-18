package fr.coloricchio.notesdefrais.domain.exception;

import fr.coloricchio.notesdefrais.domain.model.Periode;

import java.time.LocalDate;

public final class DateHorsPeriode extends RegleMetierViolee {
    public DateHorsPeriode(String reference, LocalDate date, Periode periode) {
        super("Note %s : la dépense du %s n'appartient pas à la période %s"
                .formatted(reference, date, periode.valeur()));
    }
}
