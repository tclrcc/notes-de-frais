package fr.coloricchio.notesdefrais.domain.model;

import java.util.Objects;
import java.util.UUID;

/**
 * Un acteur du process de validation.
 * Référencé par identifiant depuis NoteDeFrais
 */
public record Collaborateur(
        UUID id,
        String email,
        String nom,
        String prenom,
        RoleCollaborateur role,
        UUID managerId
) {

    public Collaborateur {
        Objects.requireNonNull(id, "id");
        Objects.requireNonNull(email, "email");
        Objects.requireNonNull(nom, "nom");
        Objects.requireNonNull(prenom, "prenom");
        Objects.requireNonNull(role, "role");
        // managerId reste nullable : un dirigeant n'a pas de manager
    }

    public String nomComplet() {
        return prenom + " " + nom;
    }

    public boolean aPourManager(UUID candidatId) {
        return managerId != null && managerId.equals(candidatId);
    }

    public boolean estManager() {
        return role == RoleCollaborateur.MANAGER;
    }

    public boolean estComptable() {
        return role == RoleCollaborateur.COMPTABLE;
    }
}
