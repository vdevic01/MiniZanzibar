import {RouterModule, Routes} from "@angular/router";
import {NgModule} from "@angular/core";
import {HomeComponent} from "./components/home/home.component";
import {MyFilesComponent} from "../file-manager/components/my-files/my-files.component";
import {
  FilesWithPermissionsComponent
} from "../file-manager/components/files-with-permissions/files-with-permissions.component";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    canActivate: [],
    children: [
      { path: '', redirectTo: 'my-files', pathMatch: 'full' },
      { path: 'my-files', component: MyFilesComponent},
      { path: 'files-with-permissions', component: FilesWithPermissionsComponent },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class HomeRoutingModule {
}
