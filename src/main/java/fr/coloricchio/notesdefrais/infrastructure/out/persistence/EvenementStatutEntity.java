package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "evenement_statut")
public class EvenementStatutEntity {

    @Id
    private UUID id;

    @Column(name = "note_id")
    private UUID noteId;

    @Column(name = "statut_precedent")
    private String statutPrecedent;

    @Column(name = "statut_nouveau")
    private String statutNouveau;

    @Column(name = "auteur_id")
    private UUID auteurId;

    @Column(name = "survenu_le")
    private Instant survenuLe;

    private String motif;

    protected EvenementStatutEntity() {}

    public EvenementStatutEntity(UUID id, UUID noteId, String statutPrecedent, String statutNouveau, UUID auteurId, Instant survenuLe, String motif) {
        this.id = id;
        this.noteId = noteId;
        this.statutPrecedent = statutPrecedent;
        this.statutNouveau = statutNouveau;
        this.auteurId = auteurId;
        this.survenuLe = survenuLe;
        this.motif = motif;
    }
}
