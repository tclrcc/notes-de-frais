package fr.coloricchio.notesdefrais.application.usecase;

import fr.coloricchio.notesdefrais.domain.model.Collaborateur;
import fr.coloricchio.notesdefrais.domain.port.in.ListerCollaborateurs;
import fr.coloricchio.notesdefrais.domain.port.out.CollaborateurRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Cas d'usage de consultation.
 */
@Service
public class ConsultationService implements ListerCollaborateurs {

    private final CollaborateurRepository collaborateurs;

    public ConsultationService(CollaborateurRepository collaborateurs) {
        this.collaborateurs = collaborateurs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Collaborateur> executer() {
        return collaborateurs.tous();
    }
}
