import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {match} from "../../../../shared/utilities/match.validator";
import {UserService} from "../../../../core/services/user.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {AuthService} from "../../../../core/services/auth.service";
import {Credentials} from "../../../../core/model/credentials";

@Component({
  selector: 'app-account-creation-form',
  templateUrl: './account-creation-form.component.html',
  styleUrls: ['./account-creation-form.component.scss']
})
export class AccountCreationFormComponent implements OnInit {
  activeTab = 'signup';
  passwordPattern = '^(?=.*[A-Z])(?=.*\\d).+$';

  signupForm = new FormGroup({
    username: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required, Validators.minLength(6)]),
    confirmPassword: new FormControl('', [Validators.required,])
  }, {validators: [match('password', 'confirmPassword')]});

  @Output() onFormSubmit = new EventEmitter<void>();

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService) {
  }

  ngOnInit(): void {
  }

  submitForm() {
    let accountData: Credentials = {
      email: this.signupForm.value['username'] ?? "",
      password: this.signupForm.value['password'] ?? ""
    };

    this.authService.registerUser(accountData).subscribe({
      next: () => {
        this.notificationService.showSuccess("Account created!", "Account created successfully!", 'topLeft');
      },
      error: (error) => {
        if (error.status == 409) {
          this.notificationService.showWarning("Username taken", "Username is already taken!", "topLeft");
        }
      }
    })
  }

  reset() {
    this.signupForm.reset();
  }
}
