import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListeNotes } from './liste-notes';

describe('ListeNotes', () => {
  let component: ListeNotes;
  let fixture: ComponentFixture<ListeNotes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListeNotes],
    }).compileComponents();

    fixture = TestBed.createComponent(ListeNotes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
