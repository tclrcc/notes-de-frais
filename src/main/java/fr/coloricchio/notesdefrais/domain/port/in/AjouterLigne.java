package fr.coloricchio.notesdefrais.domain.port.in;

import java.util.UUID;

public interface AjouterLigne {
    UUID executer(AjouterLigneCommande commande);
}
