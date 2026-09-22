package fr.coloricchio.notesdefrais.infrastructure.in.web;

import fr.coloricchio.notesdefrais.domain.exception.NoteIntrouvable;
import fr.coloricchio.notesdefrais.domain.model.Periode;
import fr.coloricchio.notesdefrais.domain.port.in.*;
import fr.coloricchio.notesdefrais.domain.port.out.NoteDeFraisRepository;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.AjouterLigneRequest;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.CreerNoteRequest;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.DecisionRequest;
import fr.coloricchio.notesdefrais.infrastructure.in.web.dto.NoteDeFraisResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notes-de-frais")
public class NoteDeFraisController {

    private final CreerNote creerNote;
    private final AjouterLigne ajouterLigne;
    private final SoumettreNote soumettreNote;
    private final DeciderSurNote deciderSurNote;
    private final NoteDeFraisRepository notes;

    public NoteDeFraisController(CreerNote creerNote, AjouterLigne ajouterLigne, SoumettreNote soumettreNote, DeciderSurNote deciderSurNote, NoteDeFraisRepository notes) {
        this.creerNote = creerNote;
        this.ajouterLigne = ajouterLigne;
        this.soumettreNote = soumettreNote;
        this.deciderSurNote = deciderSurNote;
        this.notes = notes;
    }

    @PostMapping
    public ResponseEntity<NoteDeFraisResponse> creer(@Valid @RequestBody CreerNoteRequest requete,
                                                     UriComponentsBuilder uri) {
        UUID id = creerNote.executer(new CreerNoteCommande(
                requete.collaborateurId(), Periode.de(requete.annee(), requete.mois())));

        URI location = uri.path("/api/notes-de-frais/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(lire(id));
    }

    @GetMapping("/{id}")
    public NoteDeFraisResponse lire(@PathVariable UUID id) {
        return notes.parId(id).map(NoteDeFraisResponse::depuis)
                .orElseThrow(() -> new NoteIntrouvable(id));
    }

    @GetMapping
    public List<NoteDeFraisResponse> lister(@RequestParam UUID collaborateurId) {
        return notes.parCollaborateur(collaborateurId).stream()
                .map(NoteDeFraisResponse::depuis).toList();
    }

    @PostMapping("/{id}/lignes")
    public NoteDeFraisResponse ajouterLigne(@PathVariable UUID id,
                                            @Valid @RequestBody AjouterLigneRequest r) {
        ajouterLigne.executer(new AjouterLigneCommande(
                id, r.codeCategorie(), r.dateDepense(), r.montantTtc(),
                r.tva(), r.libelle(), r.justificatifFourni()));
        return lire(id);
    }

    @PostMapping("/{id}/soumission")
    public NoteDeFraisResponse soumettre(@PathVariable UUID id) {
        soumettreNote.executer(new SoumettreNoteCommande(id));
        return lire(id);
    }

    @PostMapping("/{id}/validation")
    public NoteDeFraisResponse valider(@PathVariable UUID id, @Valid @RequestBody DecisionRequest r) {
        deciderSurNote.valider(new DeciderSurNoteCommande(id, r.decideurId(), r.motif()));
        return lire(id);
    }

    @PostMapping("/{id}/rejet")
    public NoteDeFraisResponse rejeter(@PathVariable UUID id, @Valid @RequestBody DecisionRequest r) {
        deciderSurNote.rejeter(new DeciderSurNoteCommande(id, r.decideurId(), r.motif()));
        return lire(id);
    }

    @PostMapping("/{id}/remboursement")
    public NoteDeFraisResponse rembourser(@PathVariable UUID id, @Valid @RequestBody DecisionRequest r) {
        deciderSurNote.rembourser(new DeciderSurNoteCommande(id, r.decideurId(), r.motif()));
        return lire(id);
    }
}
