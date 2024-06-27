import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FilesWithPermissionsComponent } from './files-with-permissions.component';

describe('FilesWithPermissionsComponent', () => {
  let component: FilesWithPermissionsComponent;
  let fixture: ComponentFixture<FilesWithPermissionsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FilesWithPermissionsComponent]
    });
    fixture = TestBed.createComponent(FilesWithPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
