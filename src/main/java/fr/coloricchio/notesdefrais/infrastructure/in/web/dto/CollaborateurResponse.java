package fr.coloricchio.notesdefrais.infrastructure.in.web.dto;

import fr.coloricchio.notesdefrais.domain.model.Collaborateur;

import java.util.UUID;

public record CollaborateurResponse(
        UUID id,
        String nom,
        String prenom,
        String nomComplet,
        String email,
        String role,
        UUID managerId
) {
    public static CollaborateurResponse depuis(Collaborateur c) {
        return new CollaborateurResponse(
                c.id(), c.nom(), c.prenom(), c.nomComplet(),
                c.email(), c.role().name(), c.managerId());
    }
}
