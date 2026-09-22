package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record DecisionRequest(
    @NotNull(message = "Le décideur est obligatoire")
    UUID decideurId,
    String motif
) {}
