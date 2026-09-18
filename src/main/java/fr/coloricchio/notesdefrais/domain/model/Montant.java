package fr.coloricchio.notesdefrais.domain.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Montant en euros. Toujours positif ou nul, arrondi à 2 décimales.
 * BigDecimal et non double : les flottants binaires ne représentent pas exactement 0.10€.
 */
public record Montant(BigDecimal valeur) implements Comparable<Montant> {

    public static final Montant ZERO = new Montant(BigDecimal.ZERO);

    public Montant {
        Objects.requireNonNull(valeur, "Le montant est obligatoire");
        if (valeur.signum() < 0) {
            throw new IllegalArgumentException("Un montant ne peut pas être négatif : " + valeur);
        }
        valeur = valeur.setScale(2, RoundingMode.HALF_UP);
    }

    public static Montant de(String valeur) {
        return new Montant(new BigDecimal(valeur));
    }

    public Montant plus(Montant autre) {
        return new Montant(valeur.add(autre.valeur));
    }

    public boolean estSuperieurA(Montant autre) {
        return valeur.compareTo(autre.valeur) > 0;
    }

    public boolean estNul() {
        return valeur.signum() == 0;
    }

    @Override
    public int compareTo(Montant autre) {
        return valeur.compareTo(autre.valeur);
    }

    @Override
    public String toString() {
        return valeur + " €";
    }
}
