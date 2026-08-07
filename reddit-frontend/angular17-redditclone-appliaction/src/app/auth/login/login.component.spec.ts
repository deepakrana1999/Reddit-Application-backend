import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoninComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoninComponent;
  let fixture: ComponentFixture<LoninComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoninComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LoninComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
