package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreerNoteRequest(
        @NotNull(message = "Le collaborateur est obligatoire")
        UUID collaborateurId,

        @NotNull @Min(2020) @Max(2100)
        Integer annee,

        @NotNull @Min(1) @Max(12)
        Integer mois
) {}
