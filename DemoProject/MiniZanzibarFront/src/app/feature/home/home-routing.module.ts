import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {HomeComponent} from "./components/home/home.component";
import {MyFilesComponent} from "../file-manager/components/my-files/my-files.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [],
    children: [
      { path: '', redirectTo: 'my-files', pathMatch: 'full' },
      { path: 'my-files', component: MyFilesComponent},
      // { path: 'shared-with-me', component: SharedWithMeComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
