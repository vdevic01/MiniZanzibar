import {Component, OnInit, ViewChild} from '@angular/core';
import {CredentialsFormComponent} from "../../forms/credentials-form/credentials-form.component";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  @ViewChild(CredentialsFormComponent) loginForm!: CredentialsFormComponent;

  constructor() {
  }

  ngOnInit(): void {
  }
}
