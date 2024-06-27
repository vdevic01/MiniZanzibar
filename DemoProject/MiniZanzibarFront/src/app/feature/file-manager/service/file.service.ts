import {Injectable} from '@angular/core';
import {catchError, Observable, throwError} from "rxjs";
import {HttpClient, HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {environment} from "../../../environment/environment";
import {Document} from "../model/document";

@Injectable({
  providedIn: 'root'
})
export class FileService {
  private readonly apiUrl = `${environment.apiUrl}/files`;

  constructor(private http: HttpClient) {
  }

  getOwnedFiles(): Observable<Document[]> {
    const api = `${this.apiUrl}/owned-files`;
    return this.http.get<Document[]>(api).pipe(
      catchError(this.handleError)
    );
  }

  downloadFile(fileId: string): Observable<HttpResponse<Blob>> {
    const url = `${this.apiUrl}/download/${fileId}`;
    return this.http.get(url, {
      observe: 'response',
      responseType: 'blob'
    });
  }

  uploadFile(file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post(`${this.apiUrl}/upload`, formData, {responseType: 'text'});
  }

  editFile(fileId: string, file: File): Observable<any> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    formData.append('name', file.name);
    return this.http.put(`${this.apiUrl}/edit/${fileId}`, formData, {responseType: 'text'});
  }

  deleteFile(fileId: string): Observable<any> {
    const url = `${this.apiUrl}/delete/${fileId}`;
    return this.http.delete(url, {responseType: 'text'})
  }

  addPermission(fileId: string, email: string, permission: string): Observable<any> {
    const url = `${this.apiUrl}/permissions/${fileId}?email=${email}&relation=${permission}`;
    return this.http.post(url, null, {responseType: 'text'});
  }

  removePermission(fileId: string, email: string, permission: string): Observable<any> {
    const url = `${this.apiUrl}/permissions/${fileId}?email=${email}&relation=${permission}`;
    return this.http.delete(url, {responseType: 'text'});
  }

  private handleError(error: HttpErrorResponse) {
    return throwError(() => error);
  }
}
