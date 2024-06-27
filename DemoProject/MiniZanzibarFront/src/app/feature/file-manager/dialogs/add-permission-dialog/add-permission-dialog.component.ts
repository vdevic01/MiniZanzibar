import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {FileService} from "../../service/file.service";
import {NotificationService} from "../../../../core/services/notification.service";

@Component({
  selector: 'app-add-permission-dialog',
  templateUrl: './add-permission-dialog.component.html',
  styleUrls: ['./add-permission-dialog.component.css']
})
export class AddPermissionDialogComponent implements OnInit{
  private readonly fileId: string = "";
  selected: string | undefined;

  constructor(private dialogRef: MatDialogRef<AddPermissionDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private fileService: FileService,
              private notificationService: NotificationService) {
    this.fileId = data.fileId;
  }

  ngOnInit() {
  }

  onEnter(event: any) {
    const email = event.target.value;
    if (email != '' && this.selected) {
      this.addPermission(this.fileId, email, this.selected);
    }
  }

  addPermission(fileId: string, email:string, permission: string) {
    this.fileService.addPermission(fileId, email, permission)
      .subscribe({
        next: () => {
          this.notificationService.showSuccess("Permission added!", "Permission added successfully", "topRight")
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
