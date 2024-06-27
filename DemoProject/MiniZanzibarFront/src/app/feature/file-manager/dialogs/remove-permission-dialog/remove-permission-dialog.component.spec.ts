import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemovePermissionDialogComponent } from './remove-permission-dialog.component';

describe('RemovePermissionDialogComponent', () => {
  let component: RemovePermissionDialogComponent;
  let fixture: ComponentFixture<RemovePermissionDialogComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RemovePermissionDialogComponent]
    });
    fixture = TestBed.createComponent(RemovePermissionDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
