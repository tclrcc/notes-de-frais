import { Component, computed, inject, input, signal, OnInit } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { NoteDeFraisService } from '../../services/note-de-frais';
import { ActeurCourant } from '../../services/acteur-courant';
import { FormulaireLigne } from '../../components/formulaire-ligne/formulaire-ligne';
import { NoteDeFrais, ProblemDetail, StatutNote } from '../../models/note-de-frais';

@Component({
  imports: [CurrencyPipe, DatePipe, RouterLink, FormulaireLigne],
  selector: 'app-detail-note',
  styleUrl: './detail-note.css',
  templateUrl: './detail-note.html',
})
export class DetailNote implements OnInit {

  /** Paramètre de route injecté comme signal - nécessite withComponentInputBinding(). */
  readonly id = input.required<string>();

  private readonly service = inject(NoteDeFraisService);
  private readonly router = inject(Router);
  private readonly acteurCourant = inject(ActeurCourant);

  protected readonly note = signal<NoteDeFrais | null>(null);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);
  protected readonly succes = signal<string | null>(null);

  /** Saisie du motif lors d'un rejet */
  protected readonly motifRejet = signal('');
  protected readonly formulaireRejetVisible = signal(false);

  /**
   * Actions disponibles selon l'état courant ET le rôle de l'acteur.
   * Le serveur reste l'autorité : ces conditions ne servent qu'à
   * éviter d'afficher des boutons voués à échouer.
   */
  protected readonly estProprietaire = computed(() =>
    this.note()?.collaborateurId === this.acteurCourant.acteur()?.id
  );

  protected readonly peutModifier = computed(() =>
    this.note()?.statut === 'BROUILLON' && this.estProprietaire()
  );

  protected readonly peutSoumettre = computed(() =>
    this.peutModifier() && (this.note()?.lignes.length ?? 0) > 0
  );

  protected readonly peutDecider = computed(() =>
    this.note()?.statut === 'SOUMISE' && this.acteurCourant.estManager()
  );

  protected readonly peutRembourser = computed(() =>
    this.note()?.statut === 'VALIDEE' && this.acteurCourant.estComptable()
  );

  protected readonly estTerminee = computed(() => this.note()?.statut === 'REMBOURSEE');

  protected readonly nombreLignes = computed(() => this.note()?.lignes.length ?? 0);

  ngOnInit() {
    this.charger();
  }

  protected charger() {
    this.executer(() => this.service.parId(this.id()));
  }

  protected soumettre() {
    this.executer(() => this.service.soumettre(this.id()), 'Note soumise pour validation.');
  }

  protected valider() {
    const decideur = this.acteurCourant.acteur();
    if (!decideur) {
      return;
    }
    this.executer(() => this.service.valider(this.id(), decideur.id), 'Note validée.');
  }

  protected confirmerRejet() {
    const decideur = this.acteurCourant.acteur();
    const motif = this.motifRejet().trim();

    if (!motif) {
      this.erreur.set('Le motif de rejet est obligatoire.');
      return;
    }
    if (!decideur) {
      return;
    }

    this.executer(
      () => this.service.rejeter(this.id(), decideur.id, motif),
      'Note rejetée.'
    );
    this.formulaireRejetVisible.set(false);
    this.motifRejet.set('');
  }

  protected rembourser() {
    const comptable = this.acteurCourant.acteur();
    if (!comptable) {
      return;
    }
    this.executer(
      () => this.service.rembourser(this.id(), comptable.id),
      'Note remboursée.'
    );
  }

  protected basculerFormulaireRejet() {
    this.formulaireRejetVisible.update(v => !v);
    this.erreur.set(null);
  }

  protected majMotif(evenement: Event) {
    this.motifRejet.set((evenement.target as HTMLTextAreaElement).value);
  }

  protected retour() {
    this.router.navigate(['/']);
  }

  protected classeStatut(statut: StatutNote) {
    return `statut statut--${statut.toLowerCase()}`;
  }

  /** Le formulaire renvoie la note complète : on remplace l'état local */
  protected surLigneAjoutee(note: NoteDeFrais) {
    this.note.set(note);
    this.succes.set('Ligne ajoutée');
    this.erreur.set(null);
  }

  /**
   * Toutes les actions suivent le même schéma
   * API renvoie la note à jour, l'état local est remplacé par la réponse server
   * Pas de MAJ optimiste : l'état est toujours celui que le domaine a validé
   */
  private executer(
    appel: () => Observable<NoteDeFrais>,
    messageSucces?: string
  ) {
    this.chargement.set(true);
    this.erreur.set(null);
    this.succes.set(null);

    appel().subscribe({
      next: note => {
        this.note.set(note);
        this.chargement.set(false);
        if (messageSucces) {
          this.succes.set(messageSucces);
        }
      },
      error: (e: HttpErrorResponse) => {
        this.erreur.set(this.messageDErreur(e));
        this.chargement.set(false);
      }
    });
  }

  private messageDErreur(e: HttpErrorResponse): string {
    const probleme = e.error as ProblemDetail | null;
    if (probleme?.detail) {
      return probleme.detail;
    }
    if (e.status === 0) {
      return "L'API est injoignable. Vérifiez qu'elle est démarrée sur le port 8080.";
    }
    return `Erreur ${e.status} - ${e.message}`;
  }
}
