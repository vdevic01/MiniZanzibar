import {Component, Input, OnInit} from '@angular/core';
import {Document} from "../../model/document";
import {DateFormatPipe} from "../../../../shared/pipes/date-format.pipe";
import {FileService} from "../../service/file.service";
import {MatDialog} from "@angular/material/dialog";
import {UploadFileDialogComponent} from "../../dialogs/upload-file-dialog/upload-file-dialog.component";
import {NotificationService} from "../../../../core/services/notification.service";

@Component({
  selector: 'app-file-row',
  templateUrl: './file-row.component.html',
  styleUrls: ['./file-row.component.css']
})
export class FileRowComponent implements OnInit {
  @Input() file: Document | null = null;
  @Input() refresh!: () => void;

  constructor(
    private dateFormatPipe: DateFormatPipe,
    private fileService: FileService,
    private dialog: MatDialog,
    private notificationService: NotificationService,) {
  }

  ngOnInit() {
  }

  getItemType(): string {
    if (!this.file) {
      return 'folder';
    }

    const fileName = this.file.name;
    const fileExtension = this.getFileExtension(fileName);

    switch (fileExtension.toLowerCase()) {
      case 'mp3':
      case 'wav':
      case 'ogg':
        return 'audio';
      case 'mp4':
      case 'avi':
      case 'mov':
        return 'video';
      case 'txt':
      case 'pdf':
      case 'doc':
      case 'docx':
        return 'text';
      case 'jpg':
      case 'jpeg':
      case 'png':
      case 'gif':
        return 'image';
      default:
        return 'text';
    }
  }

  getFileExtension(fileName: string): string {
    return fileName.split('.').pop()?.toLowerCase() || '';
  }

  getImageSrc(): string {
    const itemType = this.getItemType();
    return `assets/images/album/${itemType}.svg`;
  }

  getCreationTime(): string {
    if (!this.file) return '';
    return this.dateFormatPipe.transform(this.file.createdAt);
  }

  getLastModifiedTime(): string {
    if (!this.file) return '';
    return this.dateFormatPipe.transform(this.file.updatedAt);
  }

  downloadFile() {
    if (!this.file) return;

    this.fileService.downloadFile(this.file.id).subscribe(response => {
      let contentDisposition = response.headers.get('content-disposition')
      if (response.body && contentDisposition) {
        this.saveFile(response.body, this.getFilenameFromContentDisposition(contentDisposition));
      }
    });
  }

  deleteFile() {
    if (!this.file) return;
    this.fileService.deleteFile(this.file.id).subscribe({
      next: () => {
        this.notificationService.showSuccess("Deleted successfully!", "File deleted successfully!", "topRight");
        this.refresh();
      },
      error: (error) => {
        this.notificationService.showDefaultError('topRight');
      }
    });
  }

  private getFilenameFromContentDisposition(contentDisposition: string): string {
    const regex = /filename=(?<filename>[^;]+)/;
    const match = regex.exec(contentDisposition);
    if (match && match.groups) {
      return match.groups['filename'].trim();
    }
    return 'download';
  }

  private saveFile(data: Blob, filename: string) {
    const blob = new Blob([data], {type: 'application/octet-stream'});
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.style.display = 'none';
    a.href = url;
    a.download = filename;
    document.body.appendChild(a);
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
