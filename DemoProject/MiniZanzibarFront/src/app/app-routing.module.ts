import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

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
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
