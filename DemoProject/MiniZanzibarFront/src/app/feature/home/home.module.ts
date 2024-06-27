import { NgModule } from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import { NavigationComponent } from './components/navigation/navigation.component';
import {HomeComponent} from "./components/home/home.component";
import {HomeRoutingModule} from "./home-routing.module";
import {FileManagerModule} from "../file-manager/file-manager.module";



@NgModule({
  declarations: [
    HomeComponent,
    NavigationComponent,
  ],
  imports: [
    CommonModule,
    HomeRoutingModule,
    NgOptimizedImage,
    FileManagerModule
  ]
})
export class HomeModule { }
