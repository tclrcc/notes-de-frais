import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormulaireLigne } from './formulaire-ligne';

describe('FormulaireLigne', () => {
  let component: FormulaireLigne;
  let fixture: ComponentFixture<FormulaireLigne>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormulaireLigne],
    }).compileComponents();

    fixture = TestBed.createComponent(FormulaireLigne);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
