import {Component, inject} from '@angular/core';
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from '../../auth/auth.service';
import {Router} from '@angular/router';
import {NgIf} from '@angular/common';

@Component({
  selector: 'app-register-page',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    NgIf
  ],
  templateUrl: './register-page.component.html',
  styleUrl: './register-page.component.scss'
})
export class RegisterPageComponent {

  constructor(private router: Router) {
  }

  formSubmitted = false;

  authService = inject(AuthService)

  form = new FormGroup({
    email: new FormControl(null, [Validators.required, Validators.email]),
    jiraEmail: new FormControl(null, [Validators.required, Validators.email]),
    firstName: new FormControl(null, [Validators.required, Validators.minLength(3)]),
    lastName: new FormControl(null, [Validators.required,Validators.minLength(3)]),
    password: new FormControl(null, [Validators.required,Validators.minLength(4)])
  })


  onSubmit() {
    this.formSubmitted = true;

    if (this.form.valid) {
      //@ts-ignore
      this.authService.register(this.form.value)
        .subscribe({
          next: () => {
            alert('Registration successful! Please log in.');
            this.router.navigate(['/login']);
          },
          error: err => {
            console.error('Registration error:', err);
            alert('Registration failed. Please try again.');
          }
        });
    }
  }
}
