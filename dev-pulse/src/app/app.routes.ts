import {Routes} from '@angular/router';
import {LoginPageComponent} from './pages/login-page/login-page.component';
import {MainComponent} from './common-ui/main/main.component';
import {LayoutComponent} from './common-ui/layout/layout.component';
import {RegisterPageComponent} from './pages/register-page/register-page.component';
import {canActivateAuth} from './auth/access.guard';
import {CabinetComponent} from './pages/cabinet/cabinet.component';
import {ProjectsComponent} from './pages/projects/projects.component';
import {ProjectsAddComponent} from './pages/projects-add/projects-add.component';
import {JiraCallbackComponent} from './data/jira-callback/jira-callback.component';
import {ProjectPageComponent} from './pages/project-page/project-page.component';
import {IssuePageComponent} from './pages/issue-page/issue-page.component';
import {DashboardPageComponent} from './pages/dashboard/page/dashboard-page.component';
import {GithubCallbackComponent} from './data/github-callback/github-callback.component';

export const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      {path: '', component: MainComponent},
      {
        path: '',
        canActivateChild: [canActivateAuth],
        children: [
          {path: 'cabinet', component: CabinetComponent},
          {path: 'dashboard', children: [
              { path: ':composite', component: DashboardPageComponent },
            ]
          },
          {
            path: 'projects',
            children: [
              {path: '', component: ProjectsComponent},
              {path: 'add', component: ProjectsAddComponent},
              { path: ':composite', component: ProjectPageComponent },
              { path: ':composite/issue/:issueId', component: IssuePageComponent }
              // { path: ':composite/team/:issueId', component: TeamPageComponent }
            ]
          },
          {path: 'oauth/jira/code', component: JiraCallbackComponent},
          {path: 'oauth/github', component: GithubCallbackComponent}

        ]
      }
    ]
  },
  {path: 'login', component: LoginPageComponent},
  {path: 'register', component: RegisterPageComponent}
];
