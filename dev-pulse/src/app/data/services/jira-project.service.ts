import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {JiraProject} from '../interfaces/jira-project.interface';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class JiraProjectService {

  http = inject(HttpClient)
  private apiUrl = environment.jiraApiUrl;

  baseApiUrl = this.apiUrl;

  getJiraProjectByKey(projectId: number, key: string) {
    return this.http.get<JiraProject>(`${this.baseApiUrl}project-jira/${projectId}/${key}`);
  }

  updateJiraProjectByKey(projectId: number, key: string) {
    return this.http.get<JiraProject>(`${this.baseApiUrl}project-jira/${projectId}/${key}?update=true`);
  }

  constructor() { }
}
