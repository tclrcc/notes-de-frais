package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "ligne_de_frais")
public class LigneDeFraisEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "note_id")
    private NoteDeFraisEntity note;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categorie_id")
    private CategorieDepenseEntity categorie;

    @Column(name = "date_depense")
    private LocalDate dateDepense;

    @Column(name = "montant_ttc")
    private BigDecimal montantTtc;

    private BigDecimal tva;
    private String libelle;

    @Column(name = "justificatif_fourni")
    private boolean justificatifFourni;

    protected LigneDeFraisEntity() {}

    public LigneDeFraisEntity(UUID id, NoteDeFraisEntity note, CategorieDepenseEntity categorie, LocalDate dateDepense, BigDecimal montantTtc, BigDecimal tva, String libelle, boolean justificatifFourni) {
        this.id = id;
        this.note = note;
        this.categorie = categorie;
        this.dateDepense = dateDepense;
        this.montantTtc = montantTtc;
        this.tva = tva;
        this.libelle = libelle;
        this.justificatifFourni = justificatifFourni;
    }

    public UUID getId() {
        return id;
    }

    public CategorieDepenseEntity getCategorie() {
        return categorie;
    }

    public LocalDate getDateDepense() {
        return dateDepense;
    }

    public BigDecimal getMontantTtc() {
        return montantTtc;
    }

    public BigDecimal getTva() {
        return tva;
    }

    public String getLibelle() {
        return libelle;
    }

    public boolean isJustificatifFourni() {
        return justificatifFourni;
    }
}
