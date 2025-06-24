import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TokenResponse} from '../../auth/auth.interface';
import {AppUser} from '../interfaces/user.interface';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  http = inject(HttpClient)

  private apiUrl = environment.jiraApiUrl;

  baseApiUrl = this.apiUrl;

  constructor() {
  }

  getLoggedUser() {
    return this.http.get<AppUser>(`${this.baseApiUrl}user`)
  }
}
