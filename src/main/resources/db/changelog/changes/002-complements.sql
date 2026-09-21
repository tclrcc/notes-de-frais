--liquibase formatted sql

--changeset tony:006-justificatif-ligne
ALTER TABLE ligne_de_frais
    ADD COLUMN justificatif_fourni BOOLEAN NOT NULL DEFAULT FALSE;

--changeset tony:007-jeu-de-donnees
INSERT INTO collaborateur (id, email, nom, prenom, role, manager_id) VALUES
                                                                         ('11111111-1111-1111-1111-111111111111', 'marie.dubois@example.fr', 'Dubois', 'Marie', 'MANAGER', NULL),
                                                                         ('22222222-2222-2222-2222-222222222222', 'tony.coloricchio@example.fr', 'Coloricchio', 'Tony', 'COLLABORATEUR', '11111111-1111-1111-1111-111111111111'),
                                                                         ('33333333-3333-3333-3333-333333333333', 'paul.martin@example.fr', 'Martin', 'Paul', 'COMPTABLE', NULL);

INSERT INTO categorie_depense (id, code, libelle, plafond_unitaire, justificatif_obligatoire) VALUES
                                                                                                  ('aaaaaaaa-0000-0000-0000-000000000001', 'REPAS',        'Repas d''affaires',      25.00, TRUE),
                                                                                                  ('aaaaaaaa-0000-0000-0000-000000000002', 'PEAGE',        'Péage autoroute',         NULL, FALSE),
                                                                                                  ('aaaaaaaa-0000-0000-0000-000000000003', 'CARBURANT',    'Carburant',             120.00, TRUE),
                                                                                                  ('aaaaaaaa-0000-0000-0000-000000000004', 'HEBERGEMENT',  'Nuitée hôtel',          150.00, TRUE),
                                                                                                  ('aaaaaaaa-0000-0000-0000-000000000005', 'TRANSPORT',    'Train / avion',           NULL, TRUE);