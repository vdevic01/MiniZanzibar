import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {NotificationService} from "../../../../core/services/notification.service";
import {MatDialog} from "@angular/material/dialog";
import {FileService} from "../../service/file.service";
import {Document} from "../../model/document";
import {UploadFileDialogComponent} from "../../dialogs/upload-file-dialog/upload-file-dialog.component";

@Component({
  selector: 'app-my-files',
  templateUrl: './my-files.component.html',
  styleUrls: ['./my-files.component.css']
})
export class MyFilesComponent implements OnInit {
  constructor(private route: ActivatedRoute,
              private router: Router,
              private fileService: FileService,
              private notificationService: NotificationService,
              private dialog: MatDialog) {
  }

  allFiles: Document[] = [];

  ngOnInit(): void {
    this.fetchFiles();
  }

  fetchFiles() {
    this.fileService.getOwnedFiles().subscribe({
      next: (files) => {
        this.allFiles = files;
      },
      error: (error) => {
      }
    })
  }

  openUploadFileDialog() {
    const dialogRef = this.dialog.open(UploadFileDialogComponent, {
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === true) {
        this.refresh();
        this.notificationService.showSuccess("File created!", "File created successfully!", "topRight");
      }
    });
  }

  refresh() {
    this.fetchFiles();
  }
}
