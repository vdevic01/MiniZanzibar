import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FileRowComponent } from './file-row.component';

describe('FileRowComponent', () => {
  let component: FileRowComponent;
  let fixture: ComponentFixture<FileRowComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FileRowComponent]
    });
    fixture = TestBed.createComponent(FileRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
