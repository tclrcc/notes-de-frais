package fr.coloricchio.notesdefrais.infrastructure.out.messaging;

import fr.coloricchio.notesdefrais.domain.model.EvenementNote;
import fr.coloricchio.notesdefrais.domain.port.out.PublicateurEvenements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicateurJournal implements PublicateurEvenements {

    private static final Logger log = LoggerFactory.getLogger(PublicateurJournal.class);

    @Override
    public void publier(List<EvenementNote> evenements) {
        evenements.forEach(e -> log.info("Événement de domaine : {} {} -> {}",
                e.reference(), e.statutPrecedent(), e.statutNouveau()));
    }
}
