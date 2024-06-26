import {Component, OnInit} from '@angular/core';
import {MatDialogRef} from "@angular/material/dialog";
import {NotificationService} from "../../../../core/services/notification.service";
import {FileService} from "../../service/file.service";
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-upload-file-dialog',
  templateUrl: './upload-file-dialog.component.html',
  styleUrls: ['./upload-file-dialog.component.css']
})
export class UploadFileDialogComponent implements OnInit{
  uploadFileForm = new FormGroup({});

  file: File | null = null;

  constructor(private dialogRef: MatDialogRef<UploadFileDialogComponent>,
              private notificationService: NotificationService,
              private fileService: FileService) {
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
      this.fileService.uploadFile(this.file).subscribe(
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
