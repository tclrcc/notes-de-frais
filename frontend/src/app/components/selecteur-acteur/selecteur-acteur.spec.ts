import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SelecteurActeur } from './selecteur-acteur';

describe('SelecteurActeur', () => {
  let component: SelecteurActeur;
  let fixture: ComponentFixture<SelecteurActeur>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SelecteurActeur],
    }).compileComponents();

    fixture = TestBed.createComponent(SelecteurActeur);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
