import { Injectable } from '@angular/core';
import {environment} from "../../environment/environment";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {catchError, map, Observable, throwError} from "rxjs";
import {Credentials} from "../model/credentials";
import {AuthToken} from "../model/auth-token";
import {StorageService} from "./storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly apiUrl = `${environment.apiUrl}/user`;
  private readonly tokenKey = 'auth_token';

  constructor(private http: HttpClient, private storageService: StorageService) {
  }

  loginUser(credentials: Credentials): Observable<AuthToken> {
    let api = `${this.apiUrl}/login`;
    return this.http.post<AuthToken>(api, credentials)
      .pipe(
        map(token => {
          this.setAuthToken(token.token);
          return token;
        }),
        catchError(this.handleError)
      );
  }

  registerUser(user: Credentials): Observable<any> {
    let api = `${this.apiUrl}/register`;
    return this.http.post(api, user, { responseType: 'text'}).pipe(catchError(this.handleError));
  }

  isLoggedIn(): boolean {
    let authToken = this.getAuthToken();
    return authToken !== null;
  }

  logout() {
    this.storageService.clear();
  }

  getUsernameFromToken(): string | null {
    const token = this.getAuthToken();
    if (!token) {
      return null;
    }

    const tokenParts = token.split('.');
    const payloadEncoded = tokenParts[1];
    const payloadDecoded = atob(payloadEncoded);
    const payload = JSON.parse(payloadDecoded);
    return payload.sub;
  }

  setAuthToken(token: string) {
    this.storageService.setItem(this.tokenKey, token);
  }

  getAuthToken() {
    return this.storageService.getItem<string>(this.tokenKey);
  }

  handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }
}
