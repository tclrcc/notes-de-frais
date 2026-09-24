package fr.coloricchio.notesdefrais.infrastructure.in.web;

import fr.coloricchio.notesdefrais.domain.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(NoteIntrouvable.class)
    public ProblemDetail noteIntrouvable(NoteIntrouvable e) {
        return probleme(HttpStatus.NOT_FOUND, "Note de frais introuvable", e.getMessage(), "note-introuvable");
    }

    @ExceptionHandler(CategorieIntrouvable.class)
    public ProblemDetail categorieIntrouvable(CategorieIntrouvable e) {
        return probleme(HttpStatus.BAD_REQUEST, "Catégorie inconnue", e.getMessage(), "categorie-introuvable");
    }

    @ExceptionHandler(TransitionInterdite.class)
    public ProblemDetail transitionInterdite(TransitionInterdite e) {
        return probleme(HttpStatus.CONFLICT, "Transition de statut interdite", e.getMessage(), "transition-interdite");
    }

    @ExceptionHandler(NoteNonModifiable.class)
    public ProblemDetail noteNonModifiable(NoteNonModifiable e) {
        return probleme(HttpStatus.CONFLICT, "Note non modifiable", e.getMessage(), "note-non-modifiable");
    }

    @ExceptionHandler(CollaborateurIntrouvable.class)
    public ProblemDetail collaborateurIntrouvable(CollaborateurIntrouvable e) {
        return probleme(HttpStatus.BAD_REQUEST, "Collaborateur inconnu", e.getMessage(), "collaborateur-introuvable");
    }

    /** Toutes les autres violations de règles métier */
    @ExceptionHandler(RegleMetierViolee.class)
    public ProblemDetail regleMetier(RegleMetierViolee e) {
        return probleme(HttpStatus.UNPROCESSABLE_CONTENT, "Règle métier non respectée", e.getMessage(), "regle-metier");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + " : " + f.getDefaultMessage())
                .collect(Collectors.joining(" ; "));
        return probleme(HttpStatus.BAD_REQUEST, "Requête invalide", details, "requete-invalide");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail argumentInvalide(IllegalArgumentException e) {
        return probleme(HttpStatus.BAD_REQUEST, "Argument invalide", e.getMessage(), "argument-invalide");
    }

    private ProblemDetail probleme(HttpStatus statut, String titre, String detail, String type) {
        ProblemDetail p = ProblemDetail.forStatusAndDetail(statut, detail);
        p.setTitle(titre);
        p.setType(URI.create("https://notes-de-frais/erreurs/" + type));
        return p;
    }
}
