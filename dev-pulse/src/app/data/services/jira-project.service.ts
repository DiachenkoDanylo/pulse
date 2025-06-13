import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JiraProject} from '../interfaces/jira-project.interface';

@Injectable({
  providedIn: 'root'
})
export class JiraProjectService {

  http = inject(HttpClient)

  baseApiUrl = 'http://localhost:8080/'

  getJiraProjectByKey(projectId: number, key: string) {
    return this.http.get<JiraProject>(`${this.baseApiUrl}project-jira/${projectId}/${key}`);
  }

  constructor() { }
}
