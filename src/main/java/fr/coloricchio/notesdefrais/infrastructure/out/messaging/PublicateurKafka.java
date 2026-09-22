package fr.coloricchio.notesdefrais.infrastructure.out.messaging;

import fr.coloricchio.notesdefrais.domain.model.EvenementNote;
import fr.coloricchio.notesdefrais.domain.port.out.PublicateurEvenements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PublicateurKafka implements PublicateurEvenements {

    private static final Logger log = LoggerFactory.getLogger(PublicateurKafka.class);
    static final String TOPIC = "notes-de-frais.evenements";

    private final KafkaTemplate<String, EvenementNoteMessage> kafka;

    public PublicateurKafka(KafkaTemplate<String, EvenementNoteMessage> kafka) {
        this.kafka = kafka;
    }

    @Override
    public void publier(List<EvenementNote> evenements) {
        for (EvenementNote evenement : evenements) {
            kafka.send(TOPIC, evenement.cleDePartitionnement(), EvenementNoteMessage.depuis(evenement))
                    .whenComplete((resultat, erreur) -> {
                        if (erreur != null) {
                            log.error("Echec de publication de l'évènement {}", evenement.id(), erreur);
                        } else {
                            log.debug("Evènement {} publié sur la partition {}",
                                    evenement.id(), resultat.getRecordMetadata().partition());
                        }
                    });
        }
    }





}
