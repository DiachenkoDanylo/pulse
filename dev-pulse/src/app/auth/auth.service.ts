import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {catchError, filter, map, Observable, Subject, take, tap, throwError} from 'rxjs';
import {TokenResponse} from './auth.interface';
import {CookieService} from 'ngx-cookie-service';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  http = inject(HttpClient)
  baseApiUrl = 'http://localhost:8080/auth/'
  cookieService = inject(CookieService)

  get isAuth(): boolean {
    return !!this.token;
  }

  get token(): string | null {
    return this.cookieService.get('token') || null;
  }

  get refresh_token(): string | null {
    return this.cookieService.get('refreshToken') || null;
  }

  refreshInProgress = false;
  refreshSubject = new Subject<string>();

  refresh(): Observable<string> {
    if (this.refreshInProgress) {
      return this.refreshSubject.pipe(
        filter(token => token != null),
        take(1)
      );
    }

    this.refreshInProgress = true;

    return this.http.post<TokenResponse>(`${this.baseApiUrl}token`, { refresh_token: this.refresh_token })
      .pipe(
        tap(response => {
          this.cookieService.set('token', response.access_token, { sameSite: 'Lax', secure: true, path: '/', domain: 'localhost' });
          this.cookieService.set('refreshToken', response.refresh_token, { sameSite: 'Lax', secure: true, path: '/', domain: 'localhost' });
          this.refreshSubject.next(response.access_token);

          this.refreshInProgress = false;
        }),
        map(response => response.access_token),
        catchError(err => {
          this.refreshInProgress = false;
          return throwError(() => err);
        })
      );
  }

  login(payload: { username: string, password: string }) {
    return this.http.post<TokenResponse>(`${this.baseApiUrl}login`, payload, {withCredentials: true}).pipe(
      tap(value => {
        this.cookieService.set('token', value.access_token, { sameSite: 'Lax', secure: true, path: '/', domain: 'localhost' });
        this.cookieService.set('refreshToken', value.refresh_token, { sameSite: 'Lax', secure: true, path: '/', domain: 'localhost' });
      })
    );
  }

  logout() {
    this.cookieService.delete('refreshToken',  '/','localhost', true, 'Lax');
    this.cookieService.delete('token',  '/','localhost', true, 'Lax');
  }

  register(payload: {
    username: string,
    jiraEmail: string,
    firstName: string,
    lastName: string,
    password: string
  }) {
    return this.http.post(`${this.baseApiUrl}register`, payload);
  }

  constructor() {
  }
}
