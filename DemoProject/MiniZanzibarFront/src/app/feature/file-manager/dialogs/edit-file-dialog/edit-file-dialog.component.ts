import {Component, Inject, OnInit} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../../../core/services/notification.service";
import {FileService} from "../../service/file.service";

@Component({
  selector: 'app-edit-file-dialog',
  templateUrl: './edit-file-dialog.component.html',
  styleUrls: ['./edit-file-dialog.component.css']
})
export class EditFileDialogComponent implements OnInit {
  uploadFileForm = new FormGroup({});

  file: File | null = null;
  fileId: string = "";


  constructor(private dialogRef: MatDialogRef<EditFileDialogComponent>,
              private notificationService: NotificationService,
              private fileService: FileService,
              @Inject(MAT_DIALOG_DATA) public data: any,) {
    this.fileId = data.fileId;
  }

  ngOnInit() {
  }

  cancel() {
    this.dialogRef.close(false);
  }

  create() {
    if (this.file && this.file.size > 2.5 * 1024 * 1024) {
      this.notificationService.showWarning("File too large", "Maximum size is 2.5 Mb", "topRight");
      return;
    }

    if (this.file) {
      this.fileService.editFile(this.fileId, this.file).subscribe(
        response => {
          this.dialogRef.close(true);
        },
        error => {
          this.notificationService.showDefaultError('topRight');
        }
      );
    }
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement)?.files?.[0];
    if (file) {
      this.file = file;
    }
  }
}
