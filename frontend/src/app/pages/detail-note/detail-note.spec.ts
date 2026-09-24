import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetailNote } from './detail-note';

describe('DetailNote', () => {
  let component: DetailNote;
  let fixture: ComponentFixture<DetailNote>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailNote],
    }).compileComponents();

    fixture = TestBed.createComponent(DetailNote);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
