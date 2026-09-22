package fr.coloricchio.notesdefrais.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Montant")
class MontantTest {

    @Test
    @DisplayName("arrondit à deux décimales au demi supérieur")
    void arrondit() {
        assertThat(Montant.de("12.345").valeur()).isEqualByComparingTo("12.35");
        assertThat(Montant.de("12.344").valeur()).isEqualByComparingTo("12.34");
    }

    @Test
    @DisplayName("refuse une valeur négative")
    void refuseNegatif() {
        assertThatThrownBy(() -> Montant.de("-1.00"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("négatif");
    }

    @Test
    @DisplayName("additionne sans perte de précision")
    void additionne() {
        Montant somme = Montant.de("0.10").plus(Montant.de("0.20"));
        assertThat(somme.valeur()).isEqualByComparingTo("0.30");
    }

    @Test
    @DisplayName("deux montants de même valeur sont égaux")
    void egalite() {
        assertThat(Montant.de("10.00")).isEqualTo(Montant.de("10.00"));
    }

    @Test
    @DisplayName("deux montants de même valeur mais d'échelle différente sont égaux")
    void egaliteIndependanteDeLEchelle() {
        assertThat(Montant.de("10.0")).isEqualTo(Montant.de("10.00"));
        assertThat(Montant.de("10.0")).hasSameHashCodeAs(Montant.de("10.00"));
    }
}
