import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./pages/liste-notes/liste-notes').then(m => m.ListeNotes)
  },
  {
    path: 'notes/:id',
    loadComponent: () => import('./pages/detail-note/detail-note').then(m => m.DetailNote)
  },
  { path: '**', redirectTo: '' }
];
