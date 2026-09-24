import { Component, computed, inject, signal, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import { NoteDeFraisService } from '../../services/note-de-frais';
import { NoteDeFrais, ProblemDetail, StatutNote } from '../../models/note-de-frais';

@Component({
  imports: [CurrencyPipe, DatePipe, RouterLink],
  selector: 'app-liste-notes',
  styleUrl: './liste-notes.css',
  templateUrl: './liste-notes.html',
})
export class ListeNotes implements OnInit {

  private readonly service = inject(NoteDeFraisService);

  /** Collaborateur de démo, inséré par Liquibase au 1er démarrage */
  private readonly collaborateurId = '22222222-2222-2222-2222-222222222222';

  protected readonly notes = signal<NoteDeFrais[]>([]);
  protected readonly chargement = signal(false);
  protected readonly erreur = signal<string | null>(null);

  /** Valeurs recalculées automatiquement quand notes() change */
  protected readonly nombreNotes = computed(() => this.notes().length);

  protected readonly montantCumule = computed(() =>
    this.notes().reduce((total, note) => total + note.montantTotal, 0)
  );

  protected readonly aDesNotes = computed(() => this.notes().length > 0);

  ngOnInit(): void {
    this.charger();
  }

  protected charger(): void {
    this.chargement.set(true);
    this.erreur.set(null);

    this.service.lister(this.collaborateurId).subscribe({
      next: notes => {
        this.notes.set(notes);
        this.chargement.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.erreur.set(this.messageDErreur(e));
        this.chargement.set(false);
      }
    });
  }

  protected creerNote(): void {
    const maintenant = new Date();
    this.chargement.set(true);
    this.erreur.set(null);

    this.service.creer({
      collaborateurId: this.collaborateurId,
      annee: maintenant.getFullYear(),
      mois: maintenant.getMonth() + 1
    }).subscribe({
      next: note => {
        this.notes.update(liste => [...liste, note]);
        this.chargement.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.erreur.set(this.messageDErreur(e));
        this.chargement.set(false);
      }
    });
  }

  /** Classe CSS associée au statut */
  protected classeStatut(statut: StatutNote): string {
    return `statut statut--${statut.toLowerCase()}`;
  }

  private messageDErreur(e: HttpErrorResponse): string {
    const probleme = e.error as ProblemDetail | null;
    if (probleme?.detail) {
      return probleme.detail;
    }
    if (e.status === 0) {
      return "L'API est injoignable. Vérifiez qu'elle est démarrée sur le port 8080";
    }
    return `Erreur ${e.status} - ${e.message}`;
  }
}
