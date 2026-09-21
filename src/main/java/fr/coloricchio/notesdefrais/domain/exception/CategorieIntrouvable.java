package fr.coloricchio.notesdefrais.domain.exception;

public final class CategorieIntrouvable extends RegleMetierViolee {
    public CategorieIntrouvable(String code) {
        super("Aucune catégorie de dépense avec le code %s".formatted(code));
    }
}
