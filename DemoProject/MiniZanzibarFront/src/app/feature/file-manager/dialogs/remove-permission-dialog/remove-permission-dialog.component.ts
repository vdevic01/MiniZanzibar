import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FileService} from "../../service/file.service";
import {NotificationService} from "../../../../core/services/notification.service";

@Component({
  selector: 'app-remove-permission-dialog',
  templateUrl: './remove-permission-dialog.component.html',
  styleUrls: ['./remove-permission-dialog.component.css']
})
export class RemovePermissionDialogComponent implements OnInit {
  private readonly fileId: string = "";
  selected: string | undefined;

  constructor(private dialogRef: MatDialogRef<RemovePermissionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fileService: FileService,
              private notificationService: NotificationService) {
    this.fileId = data.fileId;
  }

  ngOnInit() {
  }

  onEnter(event: any) {
    const email = event.target.value;

    console.log(this.fileId, email, this.selected);

    if (email != '' && this.selected) {
      this.removePermission(this.fileId, email, this.selected);
    }
  }

  removePermission(fileId: string, email:string, permission: string) {
    this.fileService.removePermission(fileId, email, permission)
      .subscribe({
        next: () => {
          this.notificationService.showSuccess("Permission removed!", "Permisson removed successfully", "topRight")
        },
        error: (error) => {
          if (error.status == 400) {
            this.notificationService.showWarning("File already shared", "File already shared with user", "topRight");
          } else if (error.status == 404) {
            this.notificationService.showWarning("User not found", "User is not found.", "topRight");
          } else {
            this.notificationService.showDefaultError("topRight");
          }
        }
      });
  }
}
