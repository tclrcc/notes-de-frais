package fr.coloricchio.notesdefrais.domain.port.in;

import fr.coloricchio.notesdefrais.domain.model.Collaborateur;

import java.util.List;

public interface ListerCollaborateurs {
    List<Collaborateur> executer();
}
