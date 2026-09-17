--liquibase formatted sql

--changeset tony:001-collaborateur
CREATE TABLE collaborateur (
                               id         UUID PRIMARY KEY,
                               email      VARCHAR(255) NOT NULL UNIQUE,
                               nom        VARCHAR(100) NOT NULL,
                               prenom     VARCHAR(100) NOT NULL,
                               role       VARCHAR(30)  NOT NULL,
                               manager_id UUID REFERENCES collaborateur (id)
);

--changeset tony:002-categorie-depense
CREATE TABLE categorie_depense (
                                   id                       UUID PRIMARY KEY,
                                   code                     VARCHAR(30)  NOT NULL UNIQUE,
                                   libelle                  VARCHAR(100) NOT NULL,
                                   plafond_unitaire         NUMERIC(10, 2),
                                   justificatif_obligatoire BOOLEAN      NOT NULL DEFAULT TRUE
);

--changeset tony:003-note-de-frais
CREATE TABLE note_de_frais (
                               id               UUID PRIMARY KEY,
                               reference        VARCHAR(30)    NOT NULL UNIQUE,
                               collaborateur_id UUID           NOT NULL REFERENCES collaborateur (id),
                               statut           VARCHAR(20)    NOT NULL,
                               periode          DATE           NOT NULL,
                               montant_total    NUMERIC(12, 2) NOT NULL DEFAULT 0,
                               creee_le         TIMESTAMPTZ    NOT NULL,
                               soumise_le       TIMESTAMPTZ
);
CREATE INDEX idx_ndf_collaborateur_statut ON note_de_frais (collaborateur_id, statut);

--changeset tony:004-ligne-de-frais
CREATE TABLE ligne_de_frais (
                                id           UUID PRIMARY KEY,
                                note_id      UUID           NOT NULL REFERENCES note_de_frais (id) ON DELETE CASCADE,
                                categorie_id UUID           NOT NULL REFERENCES categorie_depense (id),
                                date_depense DATE           NOT NULL,
                                montant_ttc  NUMERIC(10, 2) NOT NULL CHECK (montant_ttc > 0),
                                tva          NUMERIC(10, 2) NOT NULL DEFAULT 0,
                                libelle      VARCHAR(255)   NOT NULL
);
CREATE INDEX idx_ligne_note ON ligne_de_frais (note_id);

--changeset tony:005-evenement-statut
CREATE TABLE evenement_statut (
                                  id               UUID PRIMARY KEY,
                                  note_id          UUID        NOT NULL REFERENCES note_de_frais (id) ON DELETE CASCADE,
                                  statut_precedent VARCHAR(20),
                                  statut_nouveau   VARCHAR(20) NOT NULL,
                                  auteur_id        UUID        NOT NULL REFERENCES collaborateur (id),
                                  survenu_le       TIMESTAMPTZ NOT NULL,
                                  motif            VARCHAR(500)
);
CREATE INDEX idx_evenement_note ON evenement_statut (note_id, survenu_le);