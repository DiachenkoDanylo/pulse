import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ProjectService} from '../../data/services/project.service';

@Component({
  selector: 'app-projects-add',
  standalone: true,
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './projects-add.component.html',
  styleUrl: './projects-add.component.scss'
})
export class ProjectsAddComponent {

  projectService = inject(ProjectService)

  form = new FormGroup({
    name: new FormControl(null, Validators.required),
    projectUrl: new FormControl(null, Validators.required)
  })

  onSubmit() {
    if (this.form.valid) {
      //@ts-ignore
      this.projectService.addProjectToUser(this.form.value).subscribe({next: res => {
        window.location.href = res.redirect;
      },
      error: err => {
        console.error('Redirect error', err)
      }})
    }
  }
}
