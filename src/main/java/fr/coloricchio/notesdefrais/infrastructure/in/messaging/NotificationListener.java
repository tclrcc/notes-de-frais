package fr.coloricchio.notesdefrais.infrastructure.in.messaging;

import fr.coloricchio.notesdefrais.infrastructure.out.messaging.EvenementNoteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/** Notifie le collaborateur ou le manager selon la transition */
@Component
public class NotificationListener {

    private static final Logger log = LoggerFactory.getLogger(NotificationListener.class);

    @KafkaListener(topics = "notes-de-frais.evenements", groupId = "notification")
    public void surEvenement(EvenementNoteMessage message) {
        String destinataire = switch (message.statutNouveau()) {
            case "SOUMISE"    -> "le manager - une note attend sa validation";
            case "VALIDEE"    -> "le collaborateur - sa note est validée";
            case "REJETEE"    -> "le collaborateur - sa note est rejetée : " + message.motif();
            case "REMBOURSEE" -> "le collaborateur - sa note est remboursée";
            default           -> null;
        };
        if (destinataire != null) {
            log.info("[NOTIFICATION] {} -> prévenir {}", message.reference(), destinataire);
        }
    }
}