import {NgModule} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {LoginComponent} from './components/login/login.component';
import {RegisterComponent} from './components/register/register.component';
import {LoginRegisterComponent} from './components/login-register/login-register.component';
import {CredentialsFormComponent} from './forms/credentials-form/credentials-form.component';
import {AccountCreationFormComponent} from './forms/account-creation-form/account-creation-form.component';
import {AuthenticationRoutingModule} from "./authentication-routing.module";
import {ReactiveFormsModule} from "@angular/forms";
import {SharedModule} from "../../shared/shared.module";


@NgModule({
  declarations: [
    LoginComponent,
    RegisterComponent,
    LoginRegisterComponent,
    CredentialsFormComponent,
    AccountCreationFormComponent
  ],
  imports: [
    CommonModule,
    AuthenticationRoutingModule,
    NgOptimizedImage,
    ReactiveFormsModule,
    SharedModule
  ]
})
export class AuthenticationModule {
}
