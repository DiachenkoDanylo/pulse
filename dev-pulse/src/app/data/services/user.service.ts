import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TokenResponse} from '../../auth/auth.interface';
import {AppUser} from '../interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  http = inject(HttpClient)

  baseApiUrl = 'http://localhost:8080/'

  constructor() {
  }

  getLoggedUser() {
    return this.http.get<AppUser>(`${this.baseApiUrl}user`)
  }
}
