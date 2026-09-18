package fr.coloricchio.notesdefrais.domain.model;

import fr.coloricchio.notesdefrais.domain.exception.*;

import java.time.Instant;
import java.time.Period;
import java.util.*;

/**
 * Une note de frais et ses lignes
 * Toute modification passe par cette classe
 */
public class NoteDeFrais {

    private final UUID id;
    private final String reference;
    private final UUID collaborateurId;
    private final Periode periode;
    private final Instant creeeLe;

    private StatutNote statut;
    private Instant soumiseLe;
    private final List<LigneDeFrais> lignes = new ArrayList<>();
    private final List<EvenementNote> evenements = new ArrayList<>();

    private NoteDeFrais(UUID id, String reference, UUID collaborateurId,
                        Periode periode, Instant creeeLe, StatutNote statut) {
        this.id = Objects.requireNonNull(id);
        this.reference = Objects.requireNonNull(reference);
        this.collaborateurId = Objects.requireNonNull(collaborateurId);
        this.periode = Objects.requireNonNull(periode);
        this.creeeLe = Objects.requireNonNull(creeeLe);
        this.statut = Objects.requireNonNull(statut);
    }

    /**
     * Création d'une note neuve, toujours à l'état "BROUILLON"
     */
    public static NoteDeFrais creer(UUID id, String reference, UUID collaborateurId,
                                    Periode periode, Instant maintenant) {
        return new NoteDeFrais(id, reference, collaborateurId, periode, maintenant, StatutNote.BROUILLON);
    }

    /**
     * Reconstitution de la note de frais depuis la persistance
     * Aucune règle métier n'est retestée
     */
    public static NoteDeFrais reconstituer(UUID id, String reference, UUID collaborateurId,
                                           Periode periode, Instant creeeLe, StatutNote statut,
                                           Instant soumiseLe, List<LigneDeFrais> lignes) {
        NoteDeFrais note = new NoteDeFrais(id, reference, collaborateurId,
                periode, creeeLe, statut);
        note.soumiseLe = soumiseLe;
        note.lignes.addAll(lignes);
        return note;
    }

    public void ajouterLigne(LigneDeFrais ligne) {
        exigerModifiable();
        if (!periode.contient(ligne.dateDepense())) {
            throw new DateHorsPeriode(reference, ligne.dateDepense(), periode);
        }

        CategorieDepense categorie = ligne.categorie();
        if (categorie.depassePlafond(ligne.montantTtc())) {
            throw new PlafondDepasse(categorie.code(), ligne.montantTtc(), categorie.plafondUnitaire());
        }
        if (categorie.justificatifObligatoire() && !ligne.justificatifFourni()) {
            throw new JustificatifManquant(categorie.code(), ligne.montantTtc(), ligne.libelle());
        }
        lignes.add(ligne);
    }

    public void retirerLigne(UUID ligneId) {
        exigerModifiable();
        lignes.removeIf(l -> l.id().equals(ligneId));
    }

    public void soumettre(Instant maintenant) {
        if (lignes.isEmpty()) {
            throw new NoteVide(reference);
        }
        changerStatut(StatutNote.SOUMISE, collaborateurId, maintenant, null);
        this.soumiseLe = maintenant;
    }

    public void valider(UUID validateurId, Instant maintenant) {
        changerStatut(StatutNote.VALIDEE, validateurId, maintenant, null);
    }

    public void rejeter(UUID validateurId, String motif, Instant maintenant) {
        if (motif == null || motif.isBlank()) {
            throw new IllegalArgumentException("Un rejet doit être motivé");
        }
        changerStatut(StatutNote.REJETEE, validateurId, maintenant, motif.strip());
    }

    public void rembourser(UUID comptableId, Instant maintenant) {
        changerStatut(StatutNote.REMBOURSEE, comptableId, maintenant, null);
    }

    private void changerStatut(StatutNote cible, UUID auteurId, Instant maintenant, String motif) {
        if (!statut.peutAllerVers(cible)) {
            throw new TransitionInterdite(reference, statut, cible);
        }

        StatutNote precedent = this.statut;
        this.statut = cible;
        evenements.add(new EvenementNote(
                UUID.randomUUID(), id, reference, precedent, cible, auteurId, maintenant, motif
        ));
    }

    private void exigerModifiable() {
        if (!statut.estModifiable()) {
            throw new NoteNonModifiable(reference, statut);
        }
    }

    public Montant montantTotal() {
        return lignes.stream()
                .map(LigneDeFrais::montantTtc)
                .reduce(Montant.ZERO, Montant::plus);
    }

    public List<EvenementNote> evenementsNonPublies() {
        return List.copyOf(evenements);
    }

    public void marquerEvenementsPublies() {
        evenements.clear();
    }

    public UUID id()               { return id; }
    public String reference()      { return reference; }
    public UUID collaborateurId()  { return collaborateurId; }
    public Periode periode()       { return periode; }
    public StatutNote statut()     { return statut; }
    public Instant creeeLe()       { return creeeLe; }
    public Instant soumiseLe()     { return soumiseLe; }
    public List<LigneDeFrais> lignes() { return Collections.unmodifiableList(lignes); }
}
