import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-jira-callback',
  standalone: true,
  imports: [],
  templateUrl: './jira-callback.component.html',
  styleUrl: './jira-callback.component.scss'
})
export class JiraCallbackComponent implements OnInit {

  baseApiUrl = 'http://localhost:8080'
  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];
      const state = params['state'];

      if (code && state) {
        this.http.post(`${this.baseApiUrl}/oauth/jira/code`, {
          code,
          state
        }).subscribe({
          next: () => {
            this.router.navigate(['/projects']); // або будь-який маршрут
          },
          error: err => {
            console.error('Authorization failed', err);
            this.router.navigate(['/error']);
          }
        });
      }
    });
  }
}
