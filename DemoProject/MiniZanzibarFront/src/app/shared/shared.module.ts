import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {BackButtonComponent} from './components/back-button/back-button.component';
import {FormButtonComponent} from './components/form-button/form-button.component';
import {FormFieldComponent} from "./components/form-field/form-field.component";
import {DateFormatPipe} from "./pipes/date-format.pipe";
import {ReactiveFormsModule} from "@angular/forms";


@NgModule({
  declarations: [
    FormButtonComponent,
    FormFieldComponent,
    BackButtonComponent,
    DateFormatPipe
  ],
  exports: [
    FormFieldComponent,
    FormButtonComponent,
    BackButtonComponent,
    DateFormatPipe
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
  ],
  providers: [DateFormatPipe]
})
export class SharedModule {
}
