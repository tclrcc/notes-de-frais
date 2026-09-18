package fr.coloricchio.notesdefrais.domain.exception;

public abstract class RegleMetierViolee extends RuntimeException {
    protected RegleMetierViolee(String message) {
        super(message);
    }
}
