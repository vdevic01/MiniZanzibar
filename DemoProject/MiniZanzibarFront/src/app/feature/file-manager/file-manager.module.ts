import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MyFilesComponent} from './components/my-files/my-files.component';
import { FileRowComponent } from './components/file-row/file-row.component';
import {SharedModule} from "../../shared/shared.module";
import {MaterialModule} from "../../material/material.module";
import { UploadFileDialogComponent } from './dialogs/upload-file-dialog/upload-file-dialog.component';
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    MyFilesComponent,
    FileRowComponent,
    UploadFileDialogComponent
  ],
  imports: [
    CommonModule,
    NgOptimizedImage,
    SharedModule,
    MaterialModule,
    ReactiveFormsModule
  ]
})
export class FileManagerModule {
}
