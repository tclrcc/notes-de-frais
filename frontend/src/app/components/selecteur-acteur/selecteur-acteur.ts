import { Component, inject } from '@angular/core';
import { ActeurCourant } from '../../services/acteur-courant';

@Component({
  imports: [],
  selector: 'app-selecteur-acteur',
  styleUrl: './selecteur-acteur.css',
  templateUrl: './selecteur-acteur.html',
})
export class SelecteurActeur {

  protected readonly acteurCourant = inject(ActeurCourant);

  protected changer(evenement: Event): void {
    this.changerVers((evenement.target as HTMLSelectElement).value);
  }

  private changerVers(id: string): void {
    this.acteurCourant.changer(id);
  }

  protected libelleRole(role: string): string {
    switch (role) {
      case 'MANAGER': return 'Manager';
      case 'COMPTABLE': return 'Comptable';
      case 'COLLABORATEUR': return 'Collaborateur';
      default:        return role;
    }
  }
}
