import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../../../core/services/auth.service";
import {NotificationService} from "../../../../core/services/notification.service";
import {Credentials} from "../../../../core/model/credentials";
import {Router} from "@angular/router";

@Component({
  selector: 'app-credentials-form',
  templateUrl: './credentials-form.component.html',
  styleUrls: ['./credentials-form.component.scss']
})
export class CredentialsFormComponent implements OnInit {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required]),
    password: new FormControl('', [Validators.required])
  });

  @Output() onBack = new EventEmitter<void>();
  @Output() onLoginSubmit = new EventEmitter<void>();

  constructor(
    private authService: AuthService,
    private notificationService: NotificationService,
    private router: Router) {
  }

  ngOnInit(): void {
  }

  onFormSubmit() {
    if (this.loginForm.valid) {
      const credentials: Credentials = {
        email: this.loginForm.controls['email'].value ?? '',
        password: this.loginForm.controls['password'].value ?? ''
      };

      this.authService.loginUser(credentials).subscribe({
        next: () => {
          this.router.navigate(['home']);
        },
        error: (error) => {
          if (error.status == 400) {
            this.notificationService.showWarning("Invalid Credentials", "The credentials you entered are invalid!", 'topRight');
          } else {
            this.notificationService.showDefaultError('topRight');
          }
        }
      });
    }
  }

  reset() {
    this.loginForm.reset();
  }
}
