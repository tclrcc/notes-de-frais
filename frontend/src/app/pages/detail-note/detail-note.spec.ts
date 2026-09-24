import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideRouter } from '@angular/router';
import { DetailNote } from './detail-note';

const NOTE_ID = '11111111-1111-1111-1111-111111111111';

/** Note terminée : aucun bouton d'action, pas de formulaire d'ajout de ligne. */
const NOTE_REMBOURSEE = {
  id: NOTE_ID,
  reference: 'NDF-2026-09-001',
  collaborateurId: '22222222-2222-2222-2222-222222222222',
  statut: 'REMBOURSEE',
  periode: '2026-09',
  montantTotal: 42.5,
  creeeLe: '2026-09-01T10:00:00Z',
  soumiseLe: '2026-09-05T09:00:00Z',
  lignes: []
};

describe('DetailNote', () => {

  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailNote],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('charge la note correspondant au paramètre de route', async () => {
    const fixture = TestBed.createComponent(DetailNote);
    fixture.componentRef.setInput('id', NOTE_ID);
    await fixture.whenStable();

    const requete = httpMock.expectOne(r => r.url.endsWith(`/api/notes-de-frais/${NOTE_ID}`));
    expect(requete.request.method).toBe('GET');
    requete.flush(NOTE_REMBOURSEE);

    await fixture.whenStable();

    const texte = (fixture.nativeElement as HTMLElement).textContent ?? '';
    expect(texte).toContain('NDF-2026-09-001');
  });
});
