import { Injectable } from '@angular/core';
import {environment} from "../../environment/environment";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {catchError, Observable, throwError} from "rxjs";
import {UserRegistrationData} from "../../feature/authentication/model/user-registration-data";

@Injectable({
  providedIn: 'root'
})
@Injectable({
  providedIn: 'root'
})
export class UserService {
  private readonly apiUrl = `${environment.apiUrl}`;

  constructor(private http: HttpClient) {
  }

  registerUser(user: UserRegistrationData): Observable<any> {
    let api = `${this.apiUrl}/register`;
    return this.http.post(api, user, { responseType: 'text'}).pipe(catchError(this.handleError));
  }

  checkUsernameAvailability(username: string): Observable<any> {
    let api = `${this.apiUrl}/authentication/check_username_availability`;
    const httpOptions = {
      params: new HttpParams().set('username', username)
    };
    return this.http.get(api, httpOptions).pipe(catchError(this.handleError));
  }

  handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }
}

