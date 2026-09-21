package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "note_de_frais")
public class NoteDeFraisEntity {

    @Id
    private UUID id;
    private String reference;

    @Column(name = "collaborateur_id")
    private UUID collaborateurId;

    private String statut;
    private LocalDate periode;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Column(name = "creee_le")
    private Instant creeeLe;

    @Column(name = "soumise_le")
    private Instant soumiseLe;

    @OneToMany(mappedBy = "note", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LigneDeFraisEntity> lignes = new ArrayList<>();

    protected NoteDeFraisEntity() {}

    public NoteDeFraisEntity(UUID id, String reference, UUID collaborateurId, String statut, LocalDate periode, BigDecimal montantTotal, Instant creeeLe, Instant soumiseLe) {
        this.id = id;
        this.reference = reference;
        this.collaborateurId = collaborateurId;
        this.statut = statut;
        this.periode = periode;
        this.montantTotal = montantTotal;
        this.creeeLe = creeeLe;
        this.soumiseLe = soumiseLe;
    }

    public void mettreAJour(String statut, BigDecimal montantTotal, Instant soumiseLe) {
        this.statut = statut;
        this.montantTotal = montantTotal;
        this.soumiseLe = soumiseLe;
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public UUID getCollaborateurId() {
        return collaborateurId;
    }

    public String getStatut() {
        return statut;
    }

    public LocalDate getPeriode() {
        return periode;
    }

    public BigDecimal getMontantTotal() {
        return montantTotal;
    }

    public Instant getCreeeLe() {
        return creeeLe;
    }

    public Instant getSoumiseLe() {
        return soumiseLe;
    }

    public List<LigneDeFraisEntity> getLignes() {
        return lignes;
    }
}
