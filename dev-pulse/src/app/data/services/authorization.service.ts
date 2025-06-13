import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class AuthorizationService {
  http = inject(HttpClient)

  baseApiUrl = 'http://localhost:8080/'

  constructor() { }
}
