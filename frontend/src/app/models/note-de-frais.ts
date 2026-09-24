export type StatutNote =
  | 'BROUILLON'
  | 'SOUMISE'
  | 'VALIDEE'
  | 'REJETEE'
  | 'REMBOURSEE';

export interface LigneDeFrais {
  readonly id: string;
  readonly categorie: string;
  readonly dateDepense: string;
  readonly montantTtc: number;
  readonly tva: number;
  readonly montantHt: number;
  readonly libelle: string;
  readonly justificatifFourni: boolean;
}

export interface NoteDeFrais {
  readonly id: string;
  readonly reference: string;
  readonly collaborateurId: string;
  readonly statut: StatutNote;
  readonly periode: string;
  readonly montantTotal: number;
  readonly creeeLe: string;
  readonly soumiseLe: string | null;
  readonly lignes: readonly LigneDeFrais[];
}

export interface CategorieDepense {
  readonly code: string;
  readonly libelle: string;
  readonly plafondUnitaire: number | null;
  readonly justificatifObligatoire: boolean;
}

export interface CreerNoteRequest {
  collaborateurId: string;
  annee: number;
  mois: number;
}

export interface AjouterLigneRequest {
  codeCategorie: string;
  dateDepense: string;
  montantTtc: number;
  tva: number;
  libelle: string;
  justificatifFourni: boolean;
}

/** Erreur au format ProblemDetail renvoyée par l'API. */
export interface ProblemDetail {
  type: string;
  title: string;
  status: number;
  detail: string;
  instance?: string;
}
