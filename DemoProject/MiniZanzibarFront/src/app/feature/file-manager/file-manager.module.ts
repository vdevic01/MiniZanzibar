import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MyFilesComponent} from './components/my-files/my-files.component';
import { FileRowComponent } from './components/file-row/file-row.component';
import {SharedModule} from "../../shared/shared.module";
import {MaterialModule} from "../../material/material.module";
import { UploadFileDialogComponent } from './dialogs/upload-file-dialog/upload-file-dialog.component';
import {ReactiveFormsModule} from "@angular/forms";
import { AddPermissionDialogComponent } from './dialogs/add-permission-dialog/add-permission-dialog.component';
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import { EditFileDialogComponent } from './dialogs/edit-file-dialog/edit-file-dialog.component';
import { RemovePermissionDialogComponent } from './dialogs/remove-permission-dialog/remove-permission-dialog.component';


@NgModule({
  declarations: [
    MyFilesComponent,
    FileRowComponent,
    UploadFileDialogComponent,
    AddPermissionDialogComponent,
    EditFileDialogComponent,
    RemovePermissionDialogComponent
  ],
  imports: [
    CommonModule,
    NgOptimizedImage,
    SharedModule,
    MaterialModule,
    ReactiveFormsModule,
    MatInputModule,
    MatSelectModule
  ]
})
export class FileManagerModule {
}
