import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { FormulaireLigne } from './formulaire-ligne';

const NOTE_ID = '11111111-1111-1111-1111-111111111111';

describe('FormulaireLigne', () => {

  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormulaireLigne],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  /** Les inputs requis doivent être posés avant le premier rendu. */
  function creer() {
    const fixture = TestBed.createComponent(FormulaireLigne);
    fixture.componentRef.setInput('noteId', NOTE_ID);
    fixture.componentRef.setInput('periode', '2026-09');
    return fixture;
  }

  it('charge le catalogue des catégories au démarrage', async () => {
    const fixture = creer();
    await fixture.whenStable();

    const requete = httpMock.expectOne(r => r.url.endsWith('/api/categories'));
    expect(requete.request.method).toBe('GET');
    requete.flush([]);

    expect(fixture.componentInstance).toBeTruthy();
  });

  it('borne le sélecteur de date sur la période de la note', async () => {
    const fixture = creer();
    await fixture.whenStable();

    httpMock.expectOne(r => r.url.endsWith('/api/categories')).flush([]);
    await fixture.whenStable();

    const champ = (fixture.nativeElement as HTMLElement)
      .querySelector('#date') as HTMLInputElement;

    expect(champ.min).toBe('2026-09-01');
    expect(champ.max).toBe('2026-09-30');
  });
});
