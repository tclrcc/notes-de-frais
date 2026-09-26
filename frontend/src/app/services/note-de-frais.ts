import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {
  NoteDeFrais, CreerNoteRequest, AjouterLigneRequest, CategorieDepense, Collaborateur
} from '../models/note-de-frais';

@Injectable({ providedIn: 'root' })
export class NoteDeFraisService {

  private readonly http = inject(HttpClient);
  private readonly baseUrl = '/api/notes-de-frais';

  lister(collaborateurId: string): Observable<NoteDeFrais[]> {
    return this.http.get<NoteDeFrais[]>(this.baseUrl, { params: { collaborateurId } });
  }

  parId(id: string): Observable<NoteDeFrais> {
    return this.http.get<NoteDeFrais>(`${this.baseUrl}/${id}`);
  }

  creer(requete: CreerNoteRequest): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(this.baseUrl, requete);
  }

  ajouterLigne(id: string, requete: AjouterLigneRequest): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(`${this.baseUrl}/${id}/lignes`, requete);
  }

  soumettre(id: string): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(`${this.baseUrl}/${id}/soumission`, {});
  }

  valider(id: string, decideurId: string): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(`${this.baseUrl}/${id}/validation`, { decideurId });
  }

  rejeter(id: string, decideurId: string, motif: string): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(`${this.baseUrl}/${id}/rejet`, { decideurId, motif });
  }

  rembourser(id: string, decideurId: string): Observable<NoteDeFrais> {
    return this.http.post<NoteDeFrais>(`${this.baseUrl}/${id}/remboursement`, { decideurId });
  }

  listerCategories(): Observable<CategorieDepense[]> {
    return this.http.get<CategorieDepense[]>('/api/categories');
  }

  listerCollaborateurs(): Observable<Collaborateur[]> {
    return this.http.get<Collaborateur[]>('/api/collaborateurs');
  }
}
