import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {FileService} from "../../service/file.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {MatDialog} from "@angular/material/dialog";
import {Document} from "../../model/document";
import {UploadFileDialogComponent} from "../../dialogs/upload-file-dialog/upload-file-dialog.component";

@Component({
  selector: 'app-files-with-permissions',
  templateUrl: './files-with-permissions.component.html',
  styleUrls: ['./files-with-permissions.component.css']
})
export class FilesWithPermissionsComponent implements OnInit{
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
    this.fileService.getAccessibleFiles().subscribe({
      next: (files) => {
        this.allFiles = files;
      },
      error: (error) => {
      }
    })
  }

  refresh() {
    this.fetchFiles();
  }
}
