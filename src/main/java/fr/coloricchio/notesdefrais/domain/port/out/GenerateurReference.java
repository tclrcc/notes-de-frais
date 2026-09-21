package fr.coloricchio.notesdefrais.domain.port.out;

import fr.coloricchio.notesdefrais.domain.model.Periode;

public interface GenerateurReference {
    String pour(Periode periode);
}
