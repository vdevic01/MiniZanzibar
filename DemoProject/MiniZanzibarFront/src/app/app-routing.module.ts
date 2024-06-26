import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {authGuard} from "./core/guards/auth.guard";

function loggedInGuard() {

}

const routes: Routes = [
  {
    path: '',
    redirectTo: 'login-register',
    pathMatch: 'full'
  },
  {
    path: 'login-register',
    loadChildren: () => import('./feature/authentication/authentication.module').then(m => m.AuthenticationModule),
    canActivate: [loggedInGuard]
  },
  {
    path: 'home',
    loadChildren: () => import('./feature/home/home.module').then(m => m.HomeModule),
    canActivate: [authGuard]
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
