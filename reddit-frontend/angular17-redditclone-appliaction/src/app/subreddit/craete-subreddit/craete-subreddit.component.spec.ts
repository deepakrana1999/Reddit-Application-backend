import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CraeteSubredditComponent } from './craete-subreddit.component';

describe('CraeteSubredditComponent', () => {
  let component: CraeteSubredditComponent;
  let fixture: ComponentFixture<CraeteSubredditComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CraeteSubredditComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CraeteSubredditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
