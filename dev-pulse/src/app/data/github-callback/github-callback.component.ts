import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';

@Component({
  selector: 'app-github-callback',
  standalone: true,
  imports: [],
  templateUrl: './github-callback.component.html',
  styleUrl: './github-callback.component.scss'
})
export class GithubCallbackComponent implements OnInit {

  private apiUrl = environment.gitApiUrl;
  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const code = params['code'];

      console.log(code)
      if (code ) {
        this.http.post(`${this.apiUrl}oauth/github/code`, {
          code
          // state
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
