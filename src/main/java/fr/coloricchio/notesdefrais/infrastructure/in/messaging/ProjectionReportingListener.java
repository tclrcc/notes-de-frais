package fr.coloricchio.notesdefrais.infrastructure.in.messaging;

import fr.coloricchio.notesdefrais.infrastructure.out.messaging.EvenementNoteMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * Projection de lecture : compte les transitions par statut
 * Distinct de la notification - les deux reçoivent tous les messages
 */
@Component
public class ProjectionReportingListener {

    private static final Logger log = LoggerFactory.getLogger(ProjectionReportingListener.class);
    private final Map<String, AtomicLong> compteurs = new ConcurrentHashMap<>();

    @KafkaListener(topics = "notes-de-frais.evenements", groupId = "reporting")
    public void surEvenement(EvenementNoteMessage message) {
        long total = compteurs
                .computeIfAbsent(message.statutNouveau(), s -> new AtomicLong())
                .incrementAndGet();
        log.info("[REPORTING] {} : {} occurence(s)", message.statutNouveau(), total);
    }

    public Map<String, Long> statistiques() {
        return compteurs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, e -> e.getValue().get()));
    }
}
