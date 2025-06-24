import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {AppUser} from '../interfaces/user.interface';
import {Project} from '../interfaces/project.interface';
import {JiraProject} from '../interfaces/jira-project.interface';
import {map} from 'rxjs';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  http = inject(HttpClient)
  private apiUrl = environment.jiraApiUrl;

  baseApiUrl = this.apiUrl;

  constructor() { }

  getProjectsFromUser() {
    return this.http.get<Project[]>(`${this.baseApiUrl}project/`)
  }


  updateProjectsFromUser() {
    return this.http.get<HttpResponse<any>>(`${this.baseApiUrl}project/?update=true`)
  }

  syncProjectsFromProjectId(id : number) {
    return this.http.get<JiraProject[]>(`${this.baseApiUrl}project/${id}`)
  }

  addProjectToUser(payload: {
    name: string,
    url: string,
  }) {
    return this.http.post<{ redirect: string }>(`${this.baseApiUrl}/project/add-project`,payload)
  }

}
