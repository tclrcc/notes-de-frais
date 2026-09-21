package fr.coloricchio.notesdefrais.infrastructure.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "categorie_depense")
public class CategorieDepenseEntity {

    @Id
    private UUID id;
    private String code;
    private String libelle;

    @Column(name = "plafond_unitaire")
    private BigDecimal plafondUnitaire;

    @Column(name = "justificatif_obligatoire")
    private boolean justificatifObligatoire;

    protected CategorieDepenseEntity() {}

    public UUID getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getLibelle() {
        return libelle;
    }

    public BigDecimal getPlafondUnitaire() {
        return plafondUnitaire;
    }

    public boolean isJustificatifObligatoire() {
        return justificatifObligatoire;
    }
}
