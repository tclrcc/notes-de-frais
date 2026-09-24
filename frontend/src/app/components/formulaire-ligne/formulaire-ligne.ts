import { Component, computed, inject, input, output, signal, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { HttpErrorResponse } from '@angular/common/http';
import {
  FormBuilder, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors
} from '@angular/forms';
import { NoteDeFraisService } from '../../services/note-de-frais';
import { CategorieDepense, NoteDeFrais, ProblemDetail } from '../../models/note-de-frais';

/** la TVA ne peut pas excéder le montant TTC */
function tvaCoherente(groupe: AbstractControl): ValidationErrors | null {
  const ttc = groupe.get('montantTtc')?.value;
  const tva = groupe.get('tva')?.value;
  if (ttc == null || tva == null) return null;
  return Number(tva) > Number(ttc) ? { tvaSuperieureAuTtc: true } : null;
}

@Component({
  imports: [],
  selector: 'app-formulaire-ligne',
  styleUrl: './formulaire-ligne.css',
  templateUrl: './formulaire-ligne.html',
})
export class FormulaireLigne implements OnInit {

  readonly noteId = input.required<string>();
  readonly periode = input.required<string>();

  /** Émis quand l'API a accepté la ligne : le parent remplace sa note. */
  readonly ligneAjoutee = output<NoteDeFrais>();

  private readonly service = inject(NoteDeFraisService);
  private readonly fb = inject(FormBuilder);

  protected readonly categories = signal<CategorieDepense[]>([]);
  protected readonly envoiEnCours = signal(false);
  protected readonly erreur = signal<string | null>(null);
  protected readonly codeSelectionne = signal<string>('');

  protected readonly formulaire = this.fb.nonNullable.group({
    codeCategorie: ['', Validators.required],
    dateDepense: ['', Validators.required],
    montantTtc: [0, [Validators.required, Validators.min(0.01)]],
    tva: [0, [Validators.required, Validators.min(0)]],
    libelle: ['', [Validators.required, Validators.maxLength(255)]],
    justificatifFourni: [false]
  }, { validators: tvaCoherente });

  /** Catégorie couramment sélectionnée, pour afficher ses contraintes */
  protected readonly categorieCourante = computed(() =>
    this.categories().find(c => c.code === this.codeSelectionne()) ?? null
  );

  /** Bornes du sélecteur de date : la dépense doit appartenir à la période. */
  protected readonly dateMin = computed(() => `${this.periode()}-01`);
  protected readonly dateMax = computed(() => {
    const [annee, mois] = this.periode().split('-').map(Number);
    const dernierJour = new Date(annee, mois, 0).getDate();
    return `${this.periode()}-${String(dernierJour).padStart(2, '0')}`;
  });

  ngOnInit() {
    this.service.listerCategories().subscribe({
      next: cats => this.categories.set(cats),
      error: () => this.erreur.set('Impossible de charger les catégories')
    });

    this.formulaire.controls.codeCategorie.valueChanges.subscribe(code =>
      this.codeSelectionne.set(code)
    );
  }

  protected soumettre() {
    if (this.formulaire.invalid) {
      this.formulaire.markAllAsTouched();
      return;
    }

    this.envoiEnCours.set(true);
    this.erreur.set(null);

    this.service.ajouterLigne(this.noteId(), this.formulaire.getRawValue()).subscribe({
      next: note => {
        this.ligneAjoutee.emit(note);
        this.formulaire.reset({ montantTtc: 0, tva: 0, justificatifFourni: false});
        this.codeSelectionne.set('');
        this.envoiEnCours.set(false);
      },
      error: (e: HttpErrorResponse) => {
        this.erreur.set(this.messageDErreur(e));
        this.envoiEnCours.set(false);
      }
    });
  }

  /** Un contrôle en erreur n'affiche son message qu'après interaction. */
  protected enErreur(nom: string): boolean {
    const controle = this.formulaire.get(nom);
    return !!controle && controle.invalid && (controle.dirty || controle.touched);
  }

  private messageDErreur(e: HttpErrorResponse): string {
    const probleme = e.error as ProblemDetail | null;
    return probleme?.detail ?? `Erreur ${e.status}`;
  }
}
