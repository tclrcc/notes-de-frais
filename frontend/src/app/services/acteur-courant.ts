import { Injectable, computed, inject, signal } from '@angular/core';
import { NoteDeFraisService } from './note-de-frais';
import { Collaborateur } from '../models/note-de-frais';

/**
 * Identité de l'utilisateur courant, en l'absence d'authent
 */
@Injectable({ providedIn: 'root' })
export class ActeurCourant {

  private readonly service = inject(NoteDeFraisService);

  private readonly _acteur = signal<Collaborateur | null>(null);
  private readonly _disponibles = signal<Collaborateur[]>([]);

  /** Lecture seule pour les consommateurs */
  readonly acteur = this._acteur.asReadonly();
  readonly disponibles = this._disponibles.asReadonly();

  readonly estCharge = computed(() => this._acteur() !== null);
  readonly estManager = computed(() => this._acteur()?.role === 'MANAGER');
  readonly estComptable = computed(() => this._acteur()?.role === 'COMPTABLE');

  /** Appelé une fois au démarrage */
  charger(): void {
    this.service.listerCollaborateurs().subscribe({
      next: collaborateurs => {
        this._disponibles.set(collaborateurs);
        const parDefaut = collaborateurs.find(c => c.role === 'COLLABORATEUR');
        this._acteur.set(parDefaut ?? collaborateurs[0] ?? null);
      },
      error: () => {
        this._disponibles.set([]);
        this._acteur.set(null);
      }
    });
  }

  changer(id: string): void {
    const trouve = this._disponibles().find(c => c.id === id);
    if (trouve) {
      this._acteur.set(trouve);
    }
  }
}
