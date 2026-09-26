import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { App } from './app';

const COLLABORATEURS = [
  {
    id: '22222222-2222-2222-2222-222222222222',
    nom: 'Coloricchio',
    prenom: 'Tony',
    nomComplet: 'Tony Coloricchio',
    email: 'tony.coloricchio@example.fr',
    role: 'COLLABORATEUR',
    managerId: '11111111-1111-1111-1111-111111111111'
  },
  {
    id: '11111111-1111-1111-1111-111111111111',
    nom: 'Dubois',
    prenom: 'Marie',
    nomComplet: 'Marie Dubois',
    email: 'marie.dubois@example.fr',
    role: 'MANAGER',
    managerId: null
  }
];

describe('App', () => {

  let httpMock: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    }).compileComponents();

    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it('crée le composant racine', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTruthy();
  });

  it('charge le catalogue des acteurs au démarrage', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    const requete = httpMock.expectOne(r => r.url.endsWith('/api/collaborateurs'));
    expect(requete.request.method).toBe('GET');
    requete.flush(COLLABORATEURS);
  });

  it('expose un router-outlet pour les pages', async () => {
    const fixture = TestBed.createComponent(App);
    await fixture.whenStable();

    httpMock.expectOne(r => r.url.endsWith('/api/collaborateurs')).flush(COLLABORATEURS);
    await fixture.whenStable();

    const element = fixture.nativeElement as HTMLElement;
    expect(element.querySelector('router-outlet')).not.toBeNull();
  });
});
