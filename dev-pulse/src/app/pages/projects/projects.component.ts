import {Component, inject} from '@angular/core';
import {ProjectService} from '../../data/services/project.service';
import {Project} from '../../data/interfaces/project.interface';
import {ProfileCardComponent} from '../profile-card/profile-card.component';
import {Router, RouterLink} from '@angular/router';

@Component({
  selector: 'app-projects',
  standalone: true,
  imports: [
    ProfileCardComponent,
    RouterLink
  ],
  templateUrl: './projects.component.html',
  styleUrl: './projects.component.scss'
})
export class ProjectsComponent {
  projectService = inject(ProjectService)
  projects!: Project[]
  router = inject(Router)

  constructor() {
    this.loadData();
    this.updateProjects();
  }

  loadData(): void {
    this.projectService.getProjectsFromUser().subscribe(
      val => {
        this.projects = val;
        this.projects.forEach(project => {
          this.projectService.syncProjectsFromProjectId(project.id).subscribe({
            next: (value) => {
              project.jiraProjectListDto = value
            },
            error: (err) => {
              console.error(`Failed to sync project ${project.id}`, err)
            }
          });
        });
      },
      error => console.error('Failed to load projects', error)
    );
  }

  updateProjects(): void {
    this.projectService.updateProjectsFromUser().subscribe({
      next: (value) => {
        this.loadData();
      },
      error: err => console.error('Failed to update projects', err)
    });
  }

  goToDashboard(event: MouseEvent, key: string, id: number): void {
    event.stopPropagation();
    this.router.navigate(['/dashboard/', `${key}_${id}`]);
  }

}
