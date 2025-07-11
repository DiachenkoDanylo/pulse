import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MetricsService {


  http = inject(HttpClient)
  private apiUrl = environment.jiraApiUrl;

  baseApiUrl = this.apiUrl+ 'metrics'

  getTasksFromUser(id: number, key: string) {
    return this.http.get<Record<string, number>>(`${this.baseApiUrl}/${id}/${key}/tasks-per-user`)
  }

  getTasksStatusDistribution(id: number, key: string) {
    return this.http.get<Record<string, number>>(`${this.baseApiUrl}/${id}/${key}/task-status-distribution`)
  }

  getTaskResolveByMonth(id: number, key: string, month: number){
    return this.http.get<Record<string, number>>(`${this.baseApiUrl}/${id}/${key}/task-resolve-month?value=${month}`)
  }

  getTaskResolveByWeek(id: number, key: string, week: number){
    return this.http.get<Record<string, number>>(`${this.baseApiUrl}/${id}/${key}/task-resolve-week?value=${week}`)
  }

  constructor() {
  }
}
