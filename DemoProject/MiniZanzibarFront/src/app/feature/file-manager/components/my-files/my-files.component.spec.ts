import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyFilesComponent } from './my-files.component';

describe('MyFilesComponent', () => {
  let component: MyFilesComponent;
  let fixture: ComponentFixture<MyFilesComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MyFilesComponent]
    });
    fixture = TestBed.createComponent(MyFilesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
