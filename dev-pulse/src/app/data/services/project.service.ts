import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {AppUser} from '../interfaces/user.interface';
import {Project} from '../interfaces/project.interface';
import {JiraProject} from '../interfaces/jira-project.interface';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  http = inject(HttpClient)

  baseApiUrl = 'http://localhost:8080/'

  constructor() { }

  getProjectsFromUser() {
    return this.http.get<Project[]>(`${this.baseApiUrl}project/`)
  }

  syncProjectsFromProjectId(id : number) {
    return this.http.get<JiraProject[]>(`${this.baseApiUrl}project/${id}`)
  }

  addProjectToUser(payload: {
    name: string,
    url: string,
  }) {
    return this.http.post<{ redirect: string }>(`${this.baseApiUrl}project/add-project`,payload)
  }

}
