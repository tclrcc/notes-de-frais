import { Component, OnInit, inject } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { ActeurCourant } from './services/acteur-courant';
import { SelecteurActeur } from './components/selecteur-acteur/selecteur-acteur';

@Component({
  imports: [RouterOutlet, RouterLink, SelecteurActeur],
  selector: 'app-root',
  styleUrl: './app.css',
  templateUrl: './app.html',
})
export class App implements OnInit {

  private readonly acteurCourant = inject(ActeurCourant);

  ngOnInit(): void {
    this.acteurCourant.charger();
  }
}
