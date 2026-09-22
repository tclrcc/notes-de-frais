package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AjouterLigneRequest(
    @NotBlank(message = "Le code catégorie est obligatoire")
    String codeCategorie,

    @NotNull(message = "La date de dépense est obligatoire")
    @PastOrPresent(message = "Une dépense ne peut pas être future")
    LocalDate dateDepense,

    @NotNull @DecimalMin(value = "0.01", message = "Le montant doit être strictement positif")
    BigDecimal montantTtc,

    @NotNull @DecimalMin("0.00")
    BigDecimal tva,

    @NotBlank @Size(max = 255)
    String libelle,

    boolean justificatifFourni
) {}
